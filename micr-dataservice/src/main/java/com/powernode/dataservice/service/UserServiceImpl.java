package com.powernode.dataservice.service;

import com.powernode.api.model.FinanceAccount;
import com.powernode.api.model.User;
import com.powernode.api.pojo.UserAccountInfo;
import com.powernode.api.service.UserService;
import com.powernode.common.util.CommonUtil;
import com.powernode.dataservice.mapper.FinanceAccountMapper;
import com.powernode.dataservice.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class, version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Value("${ylb.config.password-salt")
    private String passwordSalt;

    @Override
    public User queryByPhone(String phone) {
        User user = null;
        if (CommonUtil.checkPhone(phone)) {
            user = userMapper.selectByPhone(phone);
        }
        return user;
    }

    // 用户注册
    @Transactional(rollbackFor = Exception.class)
    @Override
    // 做成同步方法之后，可以保证线程安全
    public synchronized int userRegister(String phone, String password) {
        int result = 0;
        if (CommonUtil.checkPhone(phone) && password.length() == 32) {
            // 判断手机号是否再库中存在
            User queryUser = userMapper.selectByPhone(phone);
            if (queryUser == null) {
                // 注册密码的md5二次加密，给原始的密码加盐(salt)
                String newPassword = DigestUtils.md5Hex(password + passwordSalt);

                // 注册u_user
                User user  = new User();
                user.setPhone(phone);
                user.setLoginPassword(newPassword);
                user.setAddTime(new Date());

                userMapper.insertReturnPrimaryKey(user);

                // 获取主键
                FinanceAccount account = new FinanceAccount();
                account.setUid(user.getId());
                account.setAvailableMoney(new BigDecimal(0));
                financeAccountMapper.insertSelective(account);

                // 成功
                result = 1;
            } else {
                // 手机号存在，已被注册
                result = 2;
            }
        }
        return result;
    }

    // 登录
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User userLogin(String phone, String password) {
        User user = null;
        if (CommonUtil.checkPhone(phone) && password != null && password.length() == 32) {
            // String newPassword = DigestUtils.md5Hex(password + passwordSalt);
            user = userMapper.selectLogin(phone, password);

            // 更新最后的登录时间
            if (user != null) {
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            }
        }

        return user;
    }

    // 更新实名认证的信息
    @Override
    public boolean modifyRealname(String phone, String name, String idCard) {
        int rows = 0;
        if (StringUtils.isAnyBlank(phone, name, idCard)) {
            rows = userMapper.updateRealname(phone, name, idCard);

        }
        return rows > 0;
    }


    // 获取用户和资金的信息
    @Override
    public UserAccountInfo queryUserAllInfo(Integer uid) {
        UserAccountInfo info = null;
        if (uid != null && uid > 0) {
            info = userMapper.selectUserAccountById(uid);
        }
        return info;
    }

    // 查询用户
    @Override
    public User queryById(Integer uid) {
        User user  = null;
        if (uid != null && uid > 0) {
            user = userMapper.selectByPrimaryKey(uid);
        }
        return user;
    }
}
