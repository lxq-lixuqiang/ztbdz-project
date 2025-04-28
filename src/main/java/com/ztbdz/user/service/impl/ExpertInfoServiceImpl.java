package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.user.mapper.ExpertInfoMapper;
import com.ztbdz.user.pojo.ExpertInfo;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.service.ExpertInfoService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class ExpertInfoServiceImpl implements ExpertInfoService {

    @Autowired
    private ExpertInfoMapper expertInfoMapper;
    @Autowired
    private MemberService memberService;


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
        return expertInfoMapper.insert(expertInfo);
    }
}
