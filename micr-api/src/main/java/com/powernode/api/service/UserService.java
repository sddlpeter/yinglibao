package com.powernode.api.service;

import com.powernode.api.model.User;
import com.powernode.api.pojo.UserAccountInfo;

public interface UserService {
    // 根据手机号，查询数据
    User queryByPhone(String phone);

    // 用户注册
    int userRegister(String phone, String pword);

    // 登录
    User userLogin(String phone, String pword);


    // 更新实名认证的信息
    boolean modifyRealname(String phone, String name, String idCard);

    // 获取用户和资金的信息
    UserAccountInfo queryUserAllInfo(Integer uid);

    // 查询用户
    User queryById(Integer uid);
}
