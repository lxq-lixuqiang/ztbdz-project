package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.MemberMapper;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.AccountService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.util.Common;
import com.ztbdz.user.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.List;

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
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", member.getId().toString());
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        return memberMapper.update(member,queryWrapper);
    }

    @Override
    public Member getById(Long id) throws Exception {
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id.toString());
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        return memberMapper.selectOne(queryWrapper);
    }

    @Override
    public PageInfo<Member> selectList(Integer page, Integer size, Member member) throws Exception {
        PageHelper.startPage(page, size);
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_date");
        queryWrapper.eq("is_stop", Common.ENABL);
        queryWrapper.eq("is_delete", Common.ENABL);
        if(!StringUtils.isEmpty(member.getName())) queryWrapper.like("name", member.getName());
        if(!StringUtils.isEmpty(member.getSex())) queryWrapper.eq("sex", member.getSex());
        if(!StringUtils.isEmpty(member.getPhone())) queryWrapper.like("phone", member.getPhone());
        if(!StringUtils.isEmpty(member.getRole()) && !StringUtils.isEmpty(member.getRole().getId())) queryWrapper.like("roles_id", member.getRole().getId());
        return new PageInfo(memberMapper.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        try{
            Member member = memberMapper.selectRoleAndAccount(id);
            return Result.ok("查询成功！",member);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result list(Integer page, Integer size, Member member) {
        try{
            PageInfo<Member> memberList = selectList(page, size, member);
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
            // 更新角色信息
            Integer roleNum = roleService.updateById(user.getMember().getRole());
            // 更新企业信息
            Integer accountNum = accountService.updateById(user.getMember().getAccount());
            // 更新人员
            Integer memberNum = this.updateById(user.getMember());
            // 更新用户信息
            Integer userNum = userService.updateById(user);

            if(roleNum==0 || accountNum==0 || memberNum==0 || userNum==0){
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
            User user = new User();
            Member member;
            for(Long id : ids){
                member = this.getById(id);
                // 删除用户
                user.setMember(member);
                user = userService.select(user);
                userService.delete(user.getId());

                // 删除企业
                accountService.delete(member.getAccount().getId());
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
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return memberMapper.delete(queryWrapper);
    }


}
