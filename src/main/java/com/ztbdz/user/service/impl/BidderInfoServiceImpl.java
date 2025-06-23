package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.BidderInfoMapper;
import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.service.BidderInfoService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class BidderInfoServiceImpl implements BidderInfoService {

    @Autowired
    private BidderInfoMapper bidderInfoMapper;
    @Autowired
    private MemberService memberService;


    @Override
    public Result getMemberInfo(Long memberId) {
        try{
            BidderInfo bidderInfo = selectByMemberId(memberId);
            if(bidderInfo==null){
                bidderInfo = new BidderInfo(); // 初始化投标方信息
                bidderInfo.setMemberId(memberId);
                this.insert(bidderInfo);
            }
            bidderInfo.setMember(memberService.getById(memberId));
            return Result.ok("查询成功！",bidderInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询投标方信息异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(BidderInfo bidderInfo) {
        try{
            if(bidderInfo.getMember()!=null) memberService.updateById(bidderInfo.getMember());
            Integer num = this.updateById(bidderInfo);
            if(num<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
                return Result.fail("更新失败！");
            }
            return Result.ok("更新成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新投标方异常，原因："+e.getMessage());
        }
    }

    @Override
    public BidderInfo selectByMemberId(Long memberId) throws Exception {
        QueryWrapper<BidderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", memberId.toString());
        return bidderInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(BidderInfo bidderInfo) throws Exception {
        return bidderInfoMapper.updateById(bidderInfo);
    }

    @Override
    public Integer insert(BidderInfo bidderInfo) throws Exception {
        bidderInfo.setIsCheck(0);
        return bidderInfoMapper.insert(bidderInfo);
    }

    @Override
    public Result list(Integer page, Integer size, BidderInfo bidderInfo) {
        try{
            return Result.ok("查询成功！",this.page(page, size, bidderInfo));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询未审核投标方异常，原因："+e.getMessage());
        }
    }

    @Override
    public PageInfo<BidderInfo> page(Integer page, Integer size, BidderInfo bidderInfo) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(bidderInfoMapper.selectMember(bidderInfo));
    }
}
