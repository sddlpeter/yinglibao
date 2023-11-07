package com.powernode.front.controller;

import com.powernode.api.model.User;
import com.powernode.api.pojo.UserAccountInfo;
import com.powernode.common.enums.RCode;
import com.powernode.common.util.CommonUtil;
import com.powernode.common.util.JwtUtil;
import com.powernode.front.service.RealnameServiceImpl;
import com.powernode.front.service.SmsService;
import com.powernode.front.view.RespResult;
import com.powernode.front.vo.RealnameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户功能")
@RestController
@RequestMapping("/v1/user")
public class UserController extends BaseController {

    @Resource(name="smsCodeRegisterImpl")
    private SmsService smsService;

    @Resource(name="smsCodeLoginImpl")
    private SmsService loginSmsService;

    @Resource(name="realnameServiceImpl")
    private RealnameServiceImpl realnameServiceImpl;

    @Resource
    private JwtUtil jwtUtil;

    // 手机号注册用户
    @ApiOperation(value = "手机号注册用户")
    @PostMapping("/register")
    public RespResult userRegister(@RequestParam String phone,
                                   @RequestParam String pword,
                                   @RequestParam String scode) {

        RespResult result = RespResult.fail();
        // 1. 检查参数
        if (CommonUtil.checkPhone(phone)) {
            if (pword != null && pword.length() == 32) {
                // 检查短信验证码
                if (smsService.checkSmsCode(phone, scode)) {
                    // 可以注册
                    int registerResult = userService.userRegister(phone, pword);
                    if (registerResult == 1) {
                        result = RespResult.ok();
                    } else if(registerResult == 2) {
                        result.setRCode(RCode.PHONE_EXISTS);
                    } else {
                        result.setRCode(RCode.PHONE_FORMAT_ERR);
                    }
                } else {
                    // 验证码无效
                    result.setRCode(RCode.SMS_CODE_INVALID);
                }
            } else {
                result.setRCode(RCode.REQUEST_PARAM_ERR);
            }
        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }

    // 手机号是否存在
    @ApiOperation(value = "手机号是否注册过",notes = "判断手机号是否注册过")
    @ApiImplicitParam(name = "phone", value = "手机号")
    @GetMapping("/phone/exists")
    public RespResult phoneExists(@RequestParam("phone") String phone) {
        RespResult result = new RespResult();
        result.setRCode(RCode.PHONE_EXISTS);
        // 1. 检查请求参数是否符合要求
        if (CommonUtil.checkPhone(phone)) {
            // 查询数据库，调用数据服务
            User user = userService.queryByPhone(phone);
            if (user == null) {
                // 可以注册
                result = RespResult.ok();
            }
            // 把查询到的手机号放入Redis，然后检查手机号是否存在，可以先查redis

        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }

    // 登录，获取token-jwt
    @ApiOperation(value = "用户登录-获取访问token")
    @PostMapping("/login")
    public RespResult userLogin(@RequestParam String phone,
                                @RequestParam String pword,
                                @RequestParam String scode) throws Exception{
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone) && pword != null && pword.length() == 32) {
            if(loginSmsService.checkSmsCode(phone, scode)) {
                // 访问data-service
                User user  = userService.userLogin(phone, pword);
                if (user != null) {
                    // 登录成功，生成token
                    Map<String, Object> data = new HashMap<>();
                    data.put("uid", user.getId());

                    String jwtToken = jwtUtil.createJwt(data, 120);

                    result = RespResult.ok();
                    result.setAccessToken(jwtToken);
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("uid", user.getId());
                    userInfo.put("phone", user.getPhone());
                    userInfo.put("name", user.getName());
                    result.setData(userInfo);
                } else {
                    result.setRCode(RCode.PHONE_LOGIN_PASSWORD_INVALID);
                }
            } else {
                result.setRCode(RCode.SMS_CODE_INVALID);
            }

        } else {
            result.setRCode(RCode.REQUEST_PARAM_ERR);
        }

        return result;
    }

    // 实名认证 VO : value object
    @ApiOperation(value = "实名认证", notes = "提供手机号和姓名，身份证号，认证是否一致")
    @PostMapping("/realname")
    public RespResult userRealname(@RequestBody RealnameVO realnameVO) {
        RespResult result = RespResult.fail();
        result.setRCode(RCode.REQUEST_PARAM_ERR);

        // 1. 验证请求参数
        if (CommonUtil.checkPhone(realnameVO.getPhone())) {
            if (StringUtils.isNotBlank(realnameVO.getName()) &&
                    StringUtils.isNotBlank(realnameVO.getIdCard())) {
                // 判断用户已经做过实名认证
                User user = userService.queryByPhone(realnameVO.getPhone());
                if (user != null ) {
                    if (StringUtils.isNotBlank(user.getName())) {
                        result.setRCode(RCode.REALNAME_RETRY);
                    } else {
                        // 调用第三方接口，判断认证结果
                        boolean realnameResult = realnameServiceImpl.handleRealname(realnameVO.getPhone(),
                                realnameVO.getName(), realnameVO.getIdCard());

                        if (realnameResult) {
                            result = RespResult.ok();
                        } else {
                            result.setRCode(RCode.REALNAME_FAIL);
                        }
                    }
                }
            }
        }

        return result;
    }


    // 用户中心
    @ApiOperation(value = "用户中心")
    @GetMapping("/usercenter")
    public RespResult userCenter(@RequestHeader(value = "uid", required = false) Integer uid) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            UserAccountInfo userAccountInfo = userService.queryUserAllInfo(uid);
            if (userAccountInfo != null) {
                result = RespResult.ok();
                Map<String, Object> data = new HashMap<>();
                data.put("name", userAccountInfo.getName());
                data.put("phone", userAccountInfo.getPhone());
                data.put("headerUrl", userAccountInfo.getHeaderImage());
                data.put("money", userAccountInfo.getAvailableMoney());
                if (userAccountInfo.getLastLoginTime() != null) {
                    data.put("loginTime", DateFormatUtils.format(
                            userAccountInfo.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"));
                } else {
                    data.put("loginTime", "-");
                }
                // 这里并没有把userAccountInfo的数据返回给前端，是因为里面包含了敏感数据如密码，这里将必要信息提取出来，放到map里返回给前端，更安全
                result.setData(data);
            }
        }
        return result;
    }
}
