package com.ztbdz.user.service.impl;

import com.ztbdz.user.pojo.Landlord;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.*;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.interceptor.AuthenticationInterceptor;
import com.ztbdz.web.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleService roleService;

    @Override
    public Result login(String username, String password) {
        try{
            if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) return Result.fail("用户名和密码不能为空！");
            User user = userService.selectMember(null,username);
            String passwordMD5 = MD5.md5String(password);
            if(user == null) return Result.fail("用户名或密码错误！");
            if(!user.getPassword().equals(passwordMD5)) return Result.fail("用户名或密码错误！");
            if(user.getIsStop() !=0 || user.getMember().getIsStop()!=0) return Result.fail("账号已被停用，请联系管理员！");

            SystemConfig.setSession(Common.SESSION_LOGIN_MEMBER_ID,user.getMember().getId().toString());
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
            SystemConfig.removeSession(Common.SESSION_LOGIN_MEMBER_ID);
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

            SystemConfig.setSession(Common.SESSION_LOGIN_MEMBER_ID,landlord.getId().toString());
            String token = JwtUtil.createJWT(SystemConfig.TOKEN_VALIDITY, landlord.toUser());
            Map returnToken = new HashMap();
            returnToken.put("token",token);
            return Result.ok("登录成功！",returnToken);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("业主登录校验异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result verifyLogin(String token,String url) {
        try{
            User user = authenticationInterceptor.verifyLogin(token);
            Object memberId = SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID);
            Object redisObject = redisTemplate.opsForValue().get(SystemConfig.REDIS_LOGIN_INFO);
            if(redisObject==null || ((Map<String,Object>)redisObject).get(memberId.toString())==null){
                Map<String,Map<String,Object>> redisDataMap  = (Map<String,Map<String,Object>>)redisObject;
                if(redisDataMap==null) redisDataMap = new HashMap();
                if(redisDataMap.get(memberId.toString())==null){
                    Map<String,Object> dataMap = this.getLoginInfo(Long.valueOf(memberId.toString()));
                    redisDataMap.put(memberId.toString(),dataMap);
                    redisTemplate.opsForValue().set(SystemConfig.REDIS_LOGIN_INFO,redisDataMap);
                    redisObject = redisDataMap;
                }
            }
            Map<String,Object> dataMap = ((Map<String,Map<String,Object>>) redisObject).get(memberId.toString());
            // 校验 当前人员是否有访问权限
            if(roleService.verifyAuthority(url)){
                return Result.fail("该账号没有权限，请切换对应账号访问！");
            }
//            dataMap.put("token",JwtUtil.createJWT(SystemConfig.TOKEN_VALIDITY, user));
            return Result.ok("校验成功！",dataMap);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getLoginInfo(Long memberId) throws Exception{
        Object objectInfo = memberService.selectRoleAndAccount(Long.valueOf(memberId));
        String type="member";
        if(objectInfo==null){
            objectInfo = landlordService.getById(Long.valueOf(memberId));
            type = "landlord";
        }else{
            List<Role> roleList = new ArrayList();
            roleList.add(((Member)objectInfo).getRole());
            roleService.getMenuAuthorizeInfo(roleList,false);
        }
        Map<String,Object> dataMap = new HashMap();
        dataMap.put(type,objectInfo);
        return dataMap;
    }


}
