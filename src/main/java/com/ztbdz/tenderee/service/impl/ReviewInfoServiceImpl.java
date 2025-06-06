package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.ReviewInfoMapper;
import com.ztbdz.tenderee.pojo.ReviewInfo;
import com.ztbdz.tenderee.pojo.Speciality;
import com.ztbdz.tenderee.pojo.WinBid;
import com.ztbdz.tenderee.service.ReviewInfoService;
import com.ztbdz.tenderee.service.SpecialityService;
import com.ztbdz.tenderee.service.WinBidService;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Common;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReviewInfoServiceImpl implements ReviewInfoService {

    @Autowired
    private ReviewInfoMapper reviewInfoMapper;
    @Autowired
    private SpecialityService specialityService;
    @Autowired
    private WinBidService winBidService;


    @Override
    public Result get(Long id) {
        return null;
    }

    @Override
    public Result create(ReviewInfo reviewInfo) {
        try{
            Integer num = this.insert(reviewInfo);
            if(num<=0) return Result.fail("新增失败！");
            return Result.ok("新增成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("新增评审信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(ReviewInfo reviewInfo) throws Exception {
        return reviewInfoMapper.insert(reviewInfo);
    }

    @Override
    public Result update(ReviewInfo reviewInfo) {
        try{
            Integer num = this.updateById(reviewInfo);
            if(num<=0) return Result.fail("修改失败！");
            return Result.ok("修改成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("修改评审信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer updateById(ReviewInfo reviewInfo) throws Exception {
        return reviewInfoMapper.updateById(reviewInfo);
    }

    @Override
    public ReviewInfo selectById(Long id) throws Exception {
        return reviewInfoMapper.selectById(id);
    }

    @Override
    public Result page(Integer page, Integer size, String projectName) {
        try{
            return Result.ok("查询成功！",this.selectPage(page,size,projectName,null));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询评审信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public PageInfo<ReviewInfo> selectPage(Integer page, Integer size, String projectName, List<Long> ids) throws Exception {
        PageHelper.startPage(page, size);
        return new PageInfo(reviewInfoMapper.selectByProjectName(projectName,ids));
    }


    @Override
    public Result deleteList(List<Long> ids) {
        try{
            Integer num = this.deletes(ids);
            if(num<=0) return Result.fail("删除失败！");
            return Result.ok("删除成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("批量删除评审信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer deletes(List<Long> ids) throws Exception {
        QueryWrapper<ReviewInfo> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return reviewInfoMapper.delete(queryWrapper);
    }

    @Override
    public Integer deleteByProjectId(Long projectId) throws Exception {
        QueryWrapper<ReviewInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project",projectId);
        return reviewInfoMapper.delete(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result randomExpert(Long id,String hideExpert, String hideAccount, List<Speciality> specialityList) {
        try{
            // 保存屏蔽的专家和单位
            ReviewInfo reviewInfo = this.selectById(id);
            reviewInfo.setHideExpert(hideExpert);
            reviewInfo.setHideAccount(hideAccount);
            this.update(reviewInfo);
            // 保存专业要求
            specialityService.deleteByReviewInfoId(id);
            // 抽取专家
            String[] hideExperts = null; // 屏蔽专家
            if(hideExpert!=null && hideExpert.length()>0){
                hideExperts = hideExpert.split("\\/");
            }
            String[] hideAccounts = null; // 屏蔽单位
            if(hideAccount!=null && hideAccount.length()>0){
                hideAccounts = hideAccount.split("\\/");
            }
            List<Member> winBidMemberList = new ArrayList();
            for(Speciality speciality : specialityList){
                speciality.setReviewInfoId(id);
                winBidMemberList.addAll(reviewInfoMapper.randomExpert(hideExperts,hideAccounts,speciality));
                specialityService.insert(speciality);
            }
            winBidService.delete(id); //删除已中标的中标id,防止重复
            WinBid winBid;
            for(Member member : winBidMemberList){
                winBid = new WinBid();
                winBid.setWinBidId(id);
                winBid.setMemberId(member.getId());
                winBidService.insert(winBid);
            }
            return Result.ok("抽取成功！",winBidMemberList);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("专家抽取异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result expertReviewInfoList(Integer page, Integer size, String projectName) {
        try{
            Long memberId = (Long)SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID);
            List<WinBid> winBidList = winBidService.selectList(memberId,null);
            List<Long> ids = new ArrayList();
            for(WinBid winBid : winBidList){
                ids.add(winBid.getWinBidId());
            }
            return Result.ok("查询成功！",this.selectPage(page,size,projectName,ids));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询专家评审列表异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result assignReviewExperts(ReviewInfo reviewInfo) {
        try{
            if(StringUtils.isEmpty(reviewInfo.getProject().getId()) || reviewInfo.getExpertIds().size()<=0) Result.fail("项目id和分配专家不能为空");
            List<Long> experIds = reviewInfo.getExpertIds();
            reviewInfo.setNumber(experIds.size());
            reviewInfo.setState(1);
            Long projectId = reviewInfo.getProject().getId();
            this.deleteByProjectId(projectId); // 删除防止重复分配
            winBidService.delete(projectId); // 删除防止重复分配
            this.insert(reviewInfo);
            for(Long experId : experIds){
                WinBid winBid = new WinBid();
                winBid.setMemberId(experId);
                winBid.setWinBidId(reviewInfo.getId());
                winBidService.insert(winBid);
            }
            return Result.ok("分配专家成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("分配评审专家异常，原因："+e.getMessage());
        }
    }
}
