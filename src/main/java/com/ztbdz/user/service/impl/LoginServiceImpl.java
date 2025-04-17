package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.user.mapper.UserMapper;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.LoginService;
import com.ztbdz.user.web.util.Common;
import com.ztbdz.user.web.util.CommonTools;
import com.ztbdz.user.web.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Value("login.md5.key")
    public static String key;

    @Override
    public Map login(String username, String password) {
        try{
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("username",username);
            User user = userMapper.selectOne(queryWrapper);
            String passwordMD5 = MD5.md5String(password,key);
            if(user == null){
                return CommonTools.returnData(Common.FAIL,"没有查询到登录用户名！");
            }
            if(!user.getUsername().equals(username)){
                return CommonTools.returnData(Common.FAIL,"用户名不正确！");
            }
            if(!user.getPassword().equals(passwordMD5)){
                return CommonTools.returnData(Common.FAIL,"密码不正确！");
            }
            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String token = MD5.md5String(username+","+passwordMD5+","+dateString,key);

            Map returnToken = new HashMap();
            returnToken.put("token",token);
            redisTemplate.opsForValue().set(token,user);
            return CommonTools.returnData(Common.SUCCESS,"登录成功！",returnToken);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return CommonTools.returnData(Common.ERORR,"登录校验异常，原因："+e.getMessage());
        }
    }

    @Override
    public Map logout(String token) {
        try{
            redisTemplate.delete(token);
            return CommonTools.returnData(Common.SUCCESS,"退出成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return CommonTools.returnData(Common.ERORR,"退出异常，原因："+e.getMessage());
        }
    }

}
