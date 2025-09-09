package com.ztbdz.user.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.UserMapper;
import com.ztbdz.user.pojo.Account;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.AccountService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Common;
import com.ztbdz.web.util.MD5;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


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
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized Result create(User user,String code) {
        try{
            //TODO 对比短信验证码
            if(!code.equals(Common.DEFAULT_VALUE)){
//            Object codeRedis = redisTemplate.opsForValue().get(user.getMember().getPhone()+SystemConfig.SMS);
//            if(StringUtils.isEmpty(codeRedis)) return Result.fail("验证码已失效，请重新发送！");
//            if(!codeRedis.toString().equals(code))  return Result.fail("验证码错误！");
            }

            if(StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) return Result.fail("用户名和密码不能为空！");
            if(StringUtils.isEmpty(user.getMember()) ||StringUtils.isEmpty(user.getMember().getRole()) || StringUtils.isEmpty(user.getMember().getRole().getType())) return Result.fail("请选择用户类型！");
            if(user.getPassword().length()<6) return Result.fail("密码不能少于6位！");
            user.setPassword(MD5.md5String(user.getPassword()));
            if(count(user)>0) return Result.fail("用户名已被注册，请更换其他用户名！");
            if(accountService.count(user.getMember().getAccount())>0) return Result.fail("企业编码已被注册，请更换其他企业编码！");
            // 根据角色类型赋值角色，注册是默认是赋值一个角色
            List<Role> roleList = roleService.selectList(user.getMember().getRole());
            if(roleList.size()<=0) return Result.fail("没有对应角色编码【"+user.getMember().getRole().getType()+"】！");
            user.getMember().getRole().setId(roleList.get(0).getId());

            accountService.insert(user.getMember().getAccount());
            memberService.insert(user.getMember());
            this.insert(user);
            return Result.ok("注册成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("注册异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(User user) {
        return userMapper.insert(user);
    }


    @Override
    public Result updatePassword(Long userId, String password, String newPassword) {
        try{
            if(newPassword.length()<6 || newPassword.length()<6) return Result.fail("原密码或新密码不能少于6位！");
            User user = getById(userId);
            if(user==null) return Result.fail("userId找不到对应数据！");
            String passwordMD5 = MD5.md5String(password);
            if(!user.getPassword().equals(passwordMD5)) return Result.fail("原密码错误！");

            user.setPassword(MD5.md5String(newPassword));
            this.updateById(user);
            return Result.ok("更新密码成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新密码异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result forgetPassword(String phone, String newPassword) {
        try{
            if(newPassword.length()<6) return Result.fail("密码不能少于6位！");
            Member member = new Member();
            member.setPhone(phone);
            List<Member> memberList = memberService.selectList(member);
            if(memberList.size()<=0) return Result.fail("手机号没有查询到对应人员数据！");
            User user = new User();
            user.setMember(memberList.get(0));
            PageInfo<User> userPage =this.selectList(1,1,user);
            if(memberList.size()<=0) return Result.fail("人员id没有查询到对应用户数据！");
            user = userPage.getList().get(0);
            newPassword = MD5.md5String(newPassword);
            user.setPassword(newPassword);
            this.updateById(user);
            return Result.ok("更新密码成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新密码异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result sendSMS(String phone) {
        try{
            //TODO 发送短信逻辑
//            if(redisTemplate.hasKey(phone+SystemConfig.SMS)) return Result.fail("在60秒内请勿重复发送！");
//        redisTemplate.opsForValue().set(phone+SystemConfig.SMS, "短信验证码",60, TimeUnit.SECONDS);
            return Result.ok("发送成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("发送验证码异常，原因："+e.getMessage());
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
        User user =  new User();
        user.setIsDelete(Common.DISABLE); // 删除将状态调整了
        user.setIsStop(Common.DISABLE); // 删除将状态调整了
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
        if(!StringUtils.isEmpty(user.getMember().getId())) queryWrapper.eq("member_id", user.getMember().getId());
        return new PageInfo(userMapper.selectList(queryWrapper));

    }

    @Override
    public User select(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        if(!StringUtils.isEmpty(user.getUsername())) queryWrapper.eq("username",user.getUsername());
        if(!StringUtils.isEmpty(user.getMember()) && !StringUtils.isEmpty(user.getMember().getId())) queryWrapper.eq("member_id",user.getMember().getId());
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer count(User user) throws Exception {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        if(!StringUtils.isEmpty(user.getUsername())) queryWrapper.eq("username",user.getUsername());
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public User selectMember(Long id,String username) throws Exception {
        return userMapper.selectMember(id,username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result uploadExcel(MultipartFile file) {
        try{
            if(file.getOriginalFilename().indexOf("xls")<0) return Result.fail("文件类型错误，请上传Excel文件的xls，xlsx");

            String[] fields = new String[]{"用户名","密码","姓名","企业名称","角色类型","手机号","邮箱"};
            List<Map<String,String>> dataList = SystemConfig.importExcelData(file,fields);
            for(Map<String,String> dataMap : dataList){
                String message = dataMap.get(fields[0]);
                User user = new User();
                user.setUsername(message);
                user.setPassword(dataMap.get(fields[1]));
                Member member = new Member();
                member.setName(dataMap.get(fields[2]));
                Account account = new Account();
                account.setAccountName(dataMap.get(fields[3]));
                member.setAccount(account);
                Role role = new Role();
                role.setType(dataMap.get(fields[4]));
                member.setRole(role);
                member.setPhone(dataMap.get(fields[5]));
                member.setEmail(dataMap.get(fields[6]));
                user.setMember(member);
                Result result = this.create(user,Common.DEFAULT_VALUE);
                if(result.getStatus()!=200){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    log.error("解析'"+fields[0]+"'为【"+message+"】创建失败，原因："+result.getMessage()+",具体参数："+JSON.toJSONString(dataMap));
                    return Result.fail("解析'"+fields[0]+"'为【"+message+"】创建失败，原因："+result.getMessage());
                }
            }
            return Result.ok("解析Excel用户文件成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("解析Excel用户文件异常，原因："+e.getMessage());
        }
    }
}
