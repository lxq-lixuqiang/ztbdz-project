package com.ztbdz.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.user.mapper.MemberMapper;
import com.ztbdz.user.mapper.UserMapper;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.util.Common;
import com.ztbdz.user.web.util.CommonTools;
import com.ztbdz.user.web.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map create(User user) {
        try{
            user.setPassword(MD5.md5String(user.getPassword(),LoginServiceImpl.key));
            user.init();
            user.getMember().init();
            memberMapper.insert(user.getMember());

            userMapper.insert(user);
            return CommonTools.returnData(Common.SUCCESS,"注册成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return CommonTools.returnData(Common.ERORR,"注册异常，原因："+e.getMessage());
        }
    }


    @Override
    public Map update(Long userId,String password,String newPassword) {
        try{
            User user = get(userId);
            if(user==null) return CommonTools.returnData(Common.FAIL,"userId找不到对应数据！");
            String passwordMD5 = MD5.md5String(password,LoginServiceImpl.key);
            if(!user.getPassword().equals(passwordMD5)) return CommonTools.returnData(Common.FAIL,"原密码错误！");

            user.setPassword(MD5.md5String(newPassword,LoginServiceImpl.key));
            user.update();
            QueryWrapper<User> queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", user.getId());
            userMapper.update(user,queryWrapper);
            return CommonTools.returnData(Common.SUCCESS,"更新密码成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return CommonTools.returnData(Common.ERORR,"更新密码异常，原因："+e.getMessage());
        }
    }

    @Override
    public User get(Long id) throws Exception{
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        return userMapper.selectOne(queryWrapper);
    }
}
