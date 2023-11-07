package com.powernode.dataservice.mapper;

import com.powernode.api.model.User;
import com.powernode.api.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    // 统计注册人数
    int selectCountUsers();

    // 查询数据库的手机号，返回一个用户信息
    User selectByPhone(@Param("phone") String phone);

    // 添加记录，获取主键值
    int insertReturnPrimaryKey(User user);

    // 登录
    User selectLogin(@Param("phone") String phone, @Param("loginPassword") String newPassword);

    // 更新实名认证信息
    int updateRealname(@Param("phone") String phone, @Param("name") String name, @Param("idCard") String idCard);

    // 查询用户信息
    UserAccountInfo selectUserAccountById(Integer uid);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);



}