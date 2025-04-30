package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.ExpertInfoMapper;
import com.ztbdz.user.pojo.*;
import com.ztbdz.user.service.ExpertInfoService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ExpertInfoServiceImpl implements ExpertInfoService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserService userService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result create(ExpertInfo expertInfo) {
        try{
            // 手机号不能为空
            if(StringUtils.isEmpty(expertInfo.getMember()) || StringUtils.isEmpty(expertInfo.getMember().getPhone())){
                return Result.fail("专家手机号不能为空！");
            }
            User user = new User();
            Role role = new Role();
            role.setType("expert");
            expertInfo.getMember().setRole(role);
            user.setMember(expertInfo.getMember());
            user.setUsername(expertInfo.getMember().getPhone());
            user.setPassword(expertInfo.getMember().getPhone()+SystemConfig.DEFAULT_PASSWORD);
            userService.create(user,null);
            expertInfo.setMemberId(user.getMember().getId());
            Integer num = this.insert(expertInfo);
            if(num<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("添加失败！");
            }
            return Result.ok("添加成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("添加专家异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result getMemberInfo(Long memberId) {
        try{
            ExpertInfo expertInfo = this.selectByMemberId(memberId);
            if(expertInfo==null){
                expertInfo = new ExpertInfo(); // 初始化投标方信息
                expertInfo.setMemberId(memberId);
                this.insert(expertInfo);
            }
            expertInfo.setMember(memberService.getById(memberId));
            return Result.ok("查询成功！",expertInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询投标方信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result update(ExpertInfo expertInfo) {
        try{
            if(expertInfo.getMember()!=null) memberService.updateById(expertInfo.getMember());
            Integer num = this.updateById(expertInfo);
            if(num<=0){
                return Result.fail("更新失败！");
            }
            return Result.ok("更新成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新专家方异常，原因："+e.getMessage());
        }
    }

    @Override
    public ExpertInfo selectByMemberId(Long memberId) throws Exception {
        QueryWrapper<ExpertInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", memberId.toString());
        return expertInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(ExpertInfo expertInfo) throws Exception {
        return expertInfoMapper.updateById(expertInfo);
    }

    @Override
    public Integer insert(ExpertInfo expertInfo) throws Exception {
        expertInfo.setIsCheck(0);
        return expertInfoMapper.insert(expertInfo);
    }

    @Override
    public Result list(Integer page, Integer size, ExpertInfo expertInfo) {
        try{
            return Result.ok("查询成功！",this.page(page, size, expertInfo));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询未审核专家异常，原因："+e.getMessage());
        }
    }

    @Override
    public PageInfo<ExpertInfo> page(Integer page, Integer size, ExpertInfo expertInfo) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(expertInfoMapper.selectMember(expertInfo));
    }
}
