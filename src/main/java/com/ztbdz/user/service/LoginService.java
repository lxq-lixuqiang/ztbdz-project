package com.ztbdz.user.service;

import com.ztbdz.user.pojo.Member;
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
     * 获取登录用户信息
     * @param memberId
     * @return
     */
    Map<String,Object> getLoginInfo(Long memberId) throws Exception;

    /**
     * 删除登录用户缓存信息
     * @param memberId
     * @throws Exception
     */
    void deleteLoginInfo(Long memberId) throws Exception;

    /**
     * 获取登录人信息
     * @throws Exception
     */
    Member getLoginInfo() throws Exception;
}
