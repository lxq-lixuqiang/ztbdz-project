package com.ztbdz.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.AccountMapper;
import com.ztbdz.user.mapper.MemberMapper;
import com.ztbdz.user.mapper.RoleMapper;
import com.ztbdz.user.mapper.UserMapper;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.AccountService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.util.Common;
import com.ztbdz.user.web.util.MD5;
import com.ztbdz.user.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountService accountService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result create(User user) {
        try{
            user.setPassword(MD5.md5String(user.getPassword(),LoginServiceImpl.key));
            roleService.insert(user.getMember().getRole());
            accountService.insert(user.getMember().getAccount());
            memberService.insert(user.getMember());
            this.insert(user);
            return Result.ok("注册成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("注册异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(User user) {
        user.init();
        return userMapper.insert(user);
    }


    @Override
    public Result updatePassword(Long userId, String password, String newPassword) {
        try{
            User user = getById(userId);
            if(user==null) return Result.fail("userId找不到对应数据！");
            String passwordMD5 = MD5.md5String(password,LoginServiceImpl.key);
            if(!user.getPassword().equals(passwordMD5)) return Result.fail("原密码错误！");

            user.setPassword(MD5.md5String(newPassword,LoginServiceImpl.key));
            user.update();
            updateById(user);
            return Result.ok("更新密码成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("更新密码异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result forgetPassword(String phone, String newPassword) {
        try{
            Member member = new Member();
            member.setPhone(phone);
            PageInfo<Member> memberPage = memberService.selectList(1,1,member);
            if(memberPage.getTotal()<=0) return Result.fail("手机号没有查询到对应人员数据！");
            User user = new User();
            user.setMember(memberPage.getList().get(0));
            PageInfo<User> userPage =this.selectList(1,1,user);
            if(memberPage.getTotal()<=0) return Result.fail("人员id没有查询到对应用户数据！");
            user = userPage.getList().get(0);
            newPassword = MD5.md5String(newPassword,LoginServiceImpl.key);
            user.setPassword(newPassword);
            this.insert(user);
            return Result.ok("更新密码成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("更新密码异常，原因："+e.getMessage());
        }
    }

    @Override
    public User getById(Long id) throws Exception{
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", user.getId());
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        return userMapper.update(user,queryWrapper);
    }

    @Override
    public Integer delete(Long id) throws Exception {
        User user =  getById(id);
        user.setIsDelete(Common.DISABLE); // 删除将状态调整了
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        return userMapper.update(user,queryWrapper);
    }

    @Override
    public PageInfo<User> selectList(Integer page, Integer size, User user) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        if(user.getMember().getId()!=null){
            queryWrapper.eq("member_id", user.getMember().getId());
        }
        return new PageInfo(userMapper.selectList(queryWrapper));

    }
}
