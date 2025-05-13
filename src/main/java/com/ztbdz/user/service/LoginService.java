package com.ztbdz.user.service;

import com.ztbdz.web.util.Result;

import java.util.Map;


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

    /**
     *  业主登录校验
     * @param username
     * @param phone
     * @param code
     */
    Result loginLandlord(String username, String phone, String code);


    /**
     * 校验token时效
     * @param token
     * @return
     */
    Result verifyLogin(String token,String url);


    /**
     *
     * @param memberId
     * @return
     */
    Map<String,Object> getLoginInfo(Long memberId) throws Exception;
}
