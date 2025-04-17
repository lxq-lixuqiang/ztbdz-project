package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.AccountMapper;
import com.ztbdz.user.mapper.MemberMapper;
import com.ztbdz.user.mapper.UserMapper;
import com.ztbdz.user.pojo.Account;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.service.AccountService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.util.Common;
import com.ztbdz.user.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;


    @Override
    public Integer insert(Member member) throws Exception {
        member.init();
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
        member.update();
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", member.getId());
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
        if(member.getName()!=null && !"".equals(member.getName())) queryWrapper.like("name", member.getName());
        if(member.getSex()!=null ) queryWrapper.eq("sex", member.getSex());
        if(member.getPhone()!=null ) queryWrapper.like("phone", member.getPhone());
        return new PageInfo(memberMapper.selectList(queryWrapper));
    }

    @Override
    public Result get(Long id) {
        try{
            Member member = getById(id);
            member.setAccount(accountService.getById( member.getAccount().getId()));
            return Result.ok("查询成功！",member);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("查询异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result list(Integer page, Integer size, Member member) {
        try{
            PageInfo<Member> memberList = selectList(page, size, member);
            return Result.ok("查询人员成功！",memberList);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("查询人员异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result update(Member member) {
        try{
            Integer num = updateById(member);
            if(num>0) return Result.ok("更新人员成功！");
            return Result.fail("更新人员失败！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage());
            return Result.error("更新人员异常，原因："+e.getMessage());
        }
    }


}
