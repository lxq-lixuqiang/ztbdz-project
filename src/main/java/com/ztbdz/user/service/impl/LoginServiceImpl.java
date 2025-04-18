package com.ztbdz.user.service.impl;

import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.LoginService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenBlacklistService blacklistService;

    @Override
    public Result login(String username, String password) {
        try{
            User user = new User();
            user.setUsername(username);
            user = userService.select(user);
            String passwordMD5 = MD5.md5String(password);
            if(user == null) return Result.fail("没有查询到登录用户名！");
            if(!user.getUsername().equals(username)) return Result.fail("用户名不正确！");
            if(!user.getPassword().equals(passwordMD5)) return Result.fail("密码不正确！");

            String token = JwtUtil.createJWT(6000000, user);
            Map returnToken = new HashMap();
            returnToken.put("token",token);
            return Result.ok("登录成功！",returnToken);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("登录校验异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result logout(String token) {
        try{
            // 在 Spring Boot 中使用 JWT 实现退出登录（Logout）功能时，由于 JWT 是无状态的（服务端不存储 Token），传统的 Session 注销方式不适用
            //服务端维护 Token 黑名单
            //原理：将需失效的 Token 存入缓存（如 Redis），校验时检查黑名单
            blacklistService.addToBlacklist(token, 6000000);

            return Result.ok("退出成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("退出异常，原因："+e.getMessage());
        }
    }

}
