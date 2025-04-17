package com.ztbdz.user.service;

import com.ztbdz.user.pojo.User;

import java.util.Map;

public interface LoginService {

    /**
     * 登录校验
     * @param username
     * @param password
     * @return
     */
    Map login(String username,String password);

    /**
     * 登录退出
     * @param token
     * @return
     */
    Map logout(String token);

}
