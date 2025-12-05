package com.ztbdz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.user.mapper.BidderInfoMapper;
import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.AccountService;
import com.ztbdz.user.service.BidderInfoService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.web.util.Common;
import com.ztbdz.web.util.MD5;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class BidderInfoServiceImpl implements BidderInfoService {

    @Autowired
    private BidderInfoMapper bidderInfoMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;


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
            return Result.error("根据人员id查询投标方信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result getMemberAndAccount(Long id) {
        try{
            return Result.ok("查询成功！",this.getOther(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询投标方信息异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(BidderInfo bidderInfo) {
        try{
            if(StringUtils.isEmpty(bidderInfo.getMemberType())){
                return Result.fail("人员类型不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getIdCard())){
                return Result.fail("身份证号不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getQualificationCertificate01())){
                return Result.fail("身份证正反面文件不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getMember())){
                return Result.fail("人员不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getMember().getAccount())){
                return Result.fail("单位不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getMember().getAccount().getAccountCode()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getAccountCodeFileId()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getMember()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getPhone()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getEmail())){
                return Result.fail("单位信息不能为空！");
            }

            if(bidderInfo.getMember().getAccount().getExt5()!=null &&
                    bidderInfo.getMember().getAccount().getAccountCode()!=null &&
                    bidderInfo.getMember().getAccount().getAccountCode().equals(bidderInfo.getMember().getAccount().getExt5())){
                if(accountService.count(bidderInfo.getMember().getAccount())>1) return Result.fail("统一社会信用代码已被注册，请更换其他统一社会信用代码！");
            }else{
                if(accountService.count(bidderInfo.getMember().getAccount())>0) return Result.fail("统一社会信用代码已被注册，请更换其他统一社会信用代码！");
            }

            if(bidderInfo.getIdCard()!=null &&
                    bidderInfo.getNotCheckShow()!=null &&
                    bidderInfo.getNotCheckShow().equals(bidderInfo.getIdCard())){
                if(this.countIdCard(bidderInfo.getIdCard())>1) return Result.fail("法人身份证号已被注册使用，请更换其他法人身份证号！");
            }else{
                if(this.countIdCard(bidderInfo.getIdCard())>0) return Result.fail("法人身份证号已被注册使用，请更换其他法人身份证号！");
            }
            bidderInfo.getMember().getAccount().setExt5("");
            bidderInfo.setNotCheckShow("");

            Integer num = 0;
            if(bidderInfo.getMember().getAccount()!=null){
                num = accountService.updateById(bidderInfo.getMember().getAccount());
                if(num<=0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
                    return Result.fail("更新企业信息失败！");
                }
            }
            if(bidderInfo.getMember()!=null){
                num = memberService.updateById(bidderInfo.getMember());
                if(num<=0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
                    return Result.fail("更新人员信息失败！");
                }
            }
            num = this.updateById(bidderInfo);
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
    public Result review(BidderInfo bidderInfo) {
        try{
            BidderInfo bidderInfo1 = this.get(bidderInfo.getId());
            if(bidderInfo1==null){
                return Result.fail("审核失败：没有查到对应信息！");
            }
            bidderInfo1.setIsCheck(bidderInfo.getIsCheck());
            bidderInfo1.setNotCheckShow(bidderInfo.getNotCheckShow());
            Integer num = this.updateById(bidderInfo1);
            if(num<=0){
                return Result.fail("审核失败！");
            }
            return Result.ok("审核成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚事务
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("审核投标方异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result register(BidderInfo bidderInfo) {
        try{
            if(StringUtils.isEmpty(bidderInfo.getNotCheckShow())){
                return Result.fail("密码不能为空！");
            }
            if(bidderInfo.getNotCheckShow().length()<6) {
                return Result.fail("密码不能少于6位！");
            }
            if(StringUtils.isEmpty(bidderInfo.getMemberType())){
                return Result.fail("人员类型不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getIdCard())){
                return Result.fail("身份证号不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getQualificationCertificate01())){
                return Result.fail("身份证正反面文件不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getMember())){
                return Result.fail("人员不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getMember().getAccount())){
                return Result.fail("单位不能为空！");
            }
            if(StringUtils.isEmpty(bidderInfo.getMember().getAccount().getAccountCode()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getAccountCodeFileId()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getMember()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getPhone()) ||
                    StringUtils.isEmpty(bidderInfo.getMember().getAccount().getEmail())){
                return Result.fail("单位信息不能为空！");
            }
            User user = new User();
            user.setUsername(bidderInfo.getMember().getAccount().getAccountName());
            user.setPassword(bidderInfo.getNotCheckShow());
            if(this.countIdCard(bidderInfo.getIdCard())>0){
                return Result.fail("法人身份证号已被注册使用，请更换其他法人身份证号！");
            }
            bidderInfo.setNotCheckShow("");
            user.setMember(bidderInfo.getMember());
            Result result = userService.create(user,Common.DEFAULT_VALUE);
            if(result.getStatus()!=200){
                return Result.fail(result.getMessage());
            }
            bidderInfo.setMemberId(user.getMember().getId());
            Integer num = this.create(bidderInfo);
            if(num<=0){
                return Result.fail("注册失败！");
            }
            return Result.ok("注册成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("注册投标方异常，原因："+e.getMessage());
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
    public Integer create(BidderInfo bidderInfo) throws Exception {
        return bidderInfoMapper.insert(bidderInfo);
    }

    @Override
    public Integer countIdCard(String idCard) throws Exception {
        QueryWrapper<BidderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id_card", idCard);
        queryWrapper.eq("member_type", 0);
        return bidderInfoMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer insert(BidderInfo bidderInfo) throws Exception {
        bidderInfo.setIsCheck(0);
        return bidderInfoMapper.insert(bidderInfo);
    }

    @Override
    public BidderInfo get(Long id) throws Exception {
        return bidderInfoMapper.selectById(id);
    }

    @Override
    public BidderInfo getOther(Long id) throws Exception {
        return bidderInfoMapper.getOther(id);
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
