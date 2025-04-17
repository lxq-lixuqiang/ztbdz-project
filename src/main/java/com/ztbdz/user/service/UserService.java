package com.ztbdz.user.service;

import com.ztbdz.user.pojo.User;

import java.util.Map;

public interface UserService {

    /**
     * 创建用户
     * @param user
     * @return
     */
    Map create(User user);


    /**
     * 更新用户 密码
     * @param userId
     * @param password
     * @param newPassword
     * @return
     */
    Map update(Long userId,String password,String newPassword);

    /**
     * 获取用户
     * @param id
     * @return
     */
    User get(Long id) throws Exception;
}
