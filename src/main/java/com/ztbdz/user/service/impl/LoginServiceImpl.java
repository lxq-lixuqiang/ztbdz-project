package com.ztbdz.user.service.impl;

import com.ztbdz.user.pojo.Landlord;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.LandlordService;
import com.ztbdz.user.service.LoginService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.config.SystemConfig;
import com.ztbdz.user.web.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private LandlordService landlordService;
    @Autowired
    private RedisTemplate redisTemplate;

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

            String token = JwtUtil.createJWT(SystemConfig.TOKEN_VALIDITY, user);
            Map returnToken = new HashMap();
            returnToken.put("token",token);
            return Result.ok("登录成功！",returnToken);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("人员登录校验异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result logout(String token) {
        try{
            // 在 Spring Boot 中使用 JWT 实现退出登录（Logout）功能时，由于 JWT 是无状态的（服务端不存储 Token），传统的 Session 注销方式不适用
            //服务端维护 Token 黑名单
            //原理：将需失效的 Token 存入缓存（如 Redis），校验时检查黑名单
            blacklistService.addToBlacklist(token, SystemConfig.TOKEN_VALIDITY);

            return Result.ok("退出成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("退出异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result loginLandlord(String username, String phone, String code) {
        try{
            //TODO 对比短信验证码
//            Object codeRedis = redisTemplate.opsForValue().get(user.getMember().getPhone()+SystemConfig.SMS);
//            if(!StringUtils.isEmpty(codeRedis)) return Result.fail("验证码已失效，请重新发送！");
//            if(!codeRedis.toString().equals(code))  return Result.fail("验证码错误！");

            Landlord landlord = new Landlord();
            landlord.setName(username);
            landlord = landlordService.select(landlord);
            String passwordMD5 = MD5.md5String(phone+SystemConfig.DEFAULT_PASSWORD);
            if(landlord == null) return Result.fail("没有查询到登录用户名！");
            if(!landlord.getName().equals(username)) return Result.fail("用户名不正确！");
            if(!landlord.getPassword().equals(passwordMD5)) return Result.fail("密码不正确！");

            String token = JwtUtil.createJWT(SystemConfig.TOKEN_VALIDITY, landlord.toUser());
            Map returnToken = new HashMap();
            returnToken.put("token",token);
            return Result.ok("登录成功！",returnToken);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("业主登录校验异常，原因："+e.getMessage());
        }
    }

}
