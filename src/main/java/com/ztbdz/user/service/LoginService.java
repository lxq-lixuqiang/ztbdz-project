package com.ztbdz.user.service;

import com.ztbdz.user.web.util.Result;


public interface LoginService {

    /**
     * 登录校验
     * @param username
     * @param password
     * @return
     */
    Result login(String username, String password);

    /**
     * 登录退出
     * @param token
     * @return
     */
    Result logout(String token);

}
