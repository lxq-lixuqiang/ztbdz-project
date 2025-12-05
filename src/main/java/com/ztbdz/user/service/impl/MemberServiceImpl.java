package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.file.pojo.FileInfo;
import com.ztbdz.file.service.FileInfoService;
import com.ztbdz.user.mapper.MemberMapper;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.*;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Common;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private LoginService loginService;


    @Override
    public Integer insert(Member member) throws Exception {
        return memberMapper.insert(member);
    }

    @Override
    public Integer delete(Long id) throws Exception {
        Member member =  getById(id);
        member.setIsDelete(Common.DISABLE); // 删除将状态调整了
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        return memberMapper.update(member,queryWrapper);
    }

    @Override
    public Integer updateById(Member member) throws Exception {
        loginService.deleteLoginInfo(member.getId());
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", member.getId().toString());
        queryWrapper.eq("is_delete", Common.ENABL);
        return memberMapper.update(member,queryWrapper);
    }

    @Override
    public Member getById(Long id) throws Exception {
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        queryWrapper.eq("is_delete", Common.ENABL);
        return memberMapper.selectOne(queryWrapper);
    }

    @Override
    public Member selectRoleAndAccount(Long id) throws Exception {
        return memberMapper.selectRoleAndAccount(id);
    }

    @Override
    public PageInfo<Member> selectPage(Integer page, Integer size, Member member) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(this.selectList(member));
    }

    @Override
    public List<Member> selectList( Member member) throws Exception {
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");
        queryWrapper.eq("is_delete", Common.ENABL);
        if(!StringUtils.isEmpty(member.getName())) queryWrapper.like("name", member.getName());
        if(!StringUtils.isEmpty(member.getSex())) queryWrapper.eq("sex", member.getSex());
        if(!StringUtils.isEmpty(member.getPhone())) queryWrapper.like("phone", member.getPhone());
        if(!StringUtils.isEmpty(member.getRole()) && !StringUtils.isEmpty(member.getRole().getId())) queryWrapper.eq("role_id", member.getRole().getId());
        return memberMapper.selectList(queryWrapper);
    }

    @Override
    public Result get(Long id) {
        try{
            Member member = memberMapper.selectRoleAndAccount(id);
            List<Role> roleList = new ArrayList();
            roleList.add(member.getRole());
            roleService.getMenuAuthorizeInfo(roleList);
            if(!StringUtils.isEmpty(member.getAccount().getAccountCodeFileId())){
                String[] fileIdStrings = member.getAccount().getAccountCodeFileId().split(",");
                List<Long> fileIds = new ArrayList<>();
                for(int i=0;i<fileIdStrings.length;i++){
                    fileIds.add(Long.valueOf(fileIdStrings[i]));
                }
                List<FileInfo> fileInfoList = fileInfoService.listByIds(fileIds);
                for(FileInfo fileInfo : fileInfoList){
                    fileInfo.convertSize();
                }
                member.getAccount().setAccountCodeFileIds(fileInfoList);
            }

            if(member==null) return Result.fail("【"+id+"】未查询到数据！",member);
            return Result.ok("查询成功！",member);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result page(Integer page, Integer size, Member member) {
        try{
            PageInfo<Member> memberList = this.selectPage(page, size, member);
            return Result.ok("查询人员成功！",memberList);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询人员异常，原因："+e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(User user) {
        try{
            loginService.deleteLoginInfo(user.getMember().getId());
            // 更新角色信息
            Integer roleNum = roleService.updateById(user.getMember().getRole());
            // 更新企业信息
            Integer accountNum = accountService.updateById(user.getMember().getAccount());
            // 更新人员
            Integer memberNum = this.updateById(user.getMember());
            // 更新用户信息
            Integer userNum = userService.updateById(user);

            if(roleNum==0 && accountNum==0 && memberNum==0 && userNum==0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
                return Result.ok("更新人员失败！");
            }
            return Result.fail("更新人员成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新人员异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteList(List<Long> ids) {
        try{
            User user ;
            Member member;
            for(Long id : ids){
                user = new User();
                member = memberMapper.selectRoleAndAccount(id);
                if(member == null)  return Result.fail("【"+id+"】未查询到数据！");
                // 删除用户
                user.setMember(member);
                user = userService.select(user);
                userService.delete(user.getId());

                // 删除企业
//                accountService.delete(member.getAccount().getId());
            }

            // 删除人员
            if(deletes(ids)<=0) return Result.fail("删除失败！");
            return Result.ok("删除成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("删除人员异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        Member member = new Member();
        member.setIsDelete(Common.DISABLE); // 删除将状态调整了
        member.setIsStop(Common.DISABLE); // 删除将状态调整了
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", ids);
        queryWrapper.eq("is_delete", Common.ENABL);
        return memberMapper.update(member,queryWrapper);
    }

    @Override
    public List<Member> selectByIds(List<Long> ids) throws Exception {
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", ids);
        queryWrapper.eq("is_delete", Common.ENABL);
        return memberMapper.selectList(queryWrapper);
    }


}
