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
import com.ztbdz.user.pojo.ExpertInfo;
import com.ztbdz.user.service.ExpertInfoService;
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
import java.util.Arrays;
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
    @Autowired
    private ExpertInfoService expertInfoService;


    @Override
    public Result get(Long id) {
        try{
            return Result.ok("查询成功！",this.selectById(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询评审信息异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result getProjectId(Long projectId) {
        try{
            ReviewInfo reviewInfo = this.selectByProjectId(projectId);
            if(reviewInfo!=null && !StringUtils.isEmpty(reviewInfo.getSelectExpert())){
                String selectExpertString = reviewInfo.getSelectExpert();
                List<Long> expertIds = new ArrayList<Long>();
                for(String selectExpert : selectExpertString.split(",")){
                    expertIds.add(Long.valueOf(selectExpert));
                }
                reviewInfo.setSelectExpertList(expertInfoService.selectExpertTo(expertIds));
            }
            return Result.ok("查询成功！",reviewInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("项目id查询评审信息异常，原因："+e.getMessage());
        }
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
    public ReviewInfo selectByProjectId(Long projectId) throws Exception {
        QueryWrapper<ReviewInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project_id",projectId);
        return reviewInfoMapper.selectOne(queryWrapper);
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
        queryWrapper.eq("project_id",projectId);
        return reviewInfoMapper.delete(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result randomExpertById(Long id,String hideExpert, String hideAccount, List<Speciality> specialityList) {
        try{
            List<ExpertInfo> winBidMemberList = new ArrayList<ExpertInfo>();
            // 保存屏蔽的专家和单位
            ReviewInfo reviewInfo = this.selectById(id);
            reviewInfo.setState(1);
            reviewInfo.setHideExpert(hideExpert);
            reviewInfo.setHideAccount(hideAccount);
            this.update(reviewInfo);

            // 保存专业要求
            specialityService.deleteByReviewInfoId(id);
            // 抽取专家
            List<String> hideExperts = new ArrayList<String>(); // 屏蔽专家
            if(reviewInfo.getHideExpert()!=null && reviewInfo.getHideExpert().length()>0){
                hideExperts =  Arrays.asList(reviewInfo.getHideExpert().split("\\/"));
            }
            List<String> hideAccounts = new ArrayList<String>(); // 屏蔽单位
            if(reviewInfo.getHideAccount()!=null && reviewInfo.getHideAccount().length()>0){
                hideAccounts = Arrays.asList(reviewInfo.getHideAccount().split("\\/"));
            }
            for(Speciality speciality : specialityList){
                speciality.setReviewInfoId(id);
                specialityService.insert(speciality);
                List<ExpertInfo> expertInfoList = reviewInfoMapper.randomExpert(hideExperts,hideAccounts,speciality.getNum(),speciality);
                if(expertInfoList.size()==0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.error("【"+speciality.getExpertType()+"】类别，没有找到符合条件的专家，请调整筛选条件！");
                }
                winBidMemberList.addAll(expertInfoList);
                for(ExpertInfo expertInfo : winBidMemberList){
                    hideExperts.add(""+expertInfo.getMember().getId());
                }
            }
            winBidService.delete(id); //删除已中标的中标id,防止重复
            WinBid winBid;
            String selectExpert = "";
            String spareExpert = "";
            for(int i=0;i<winBidMemberList.size();i++){
                ExpertInfo expertInfo = winBidMemberList.get(i);
                if(reviewInfo.getNumber()>=(i+1)){
                    winBid = new WinBid();
                    winBid.setWinBidId(reviewInfo.getId());
                    winBid.setMemberId(expertInfo.getMember().getId());
                    winBidService.insert(winBid);
                    if(selectExpert.length()>0) selectExpert+=",";
                    selectExpert+=expertInfo.getMember().getId();
                }else{
                    if(spareExpert.length()>0) spareExpert+=",";
                    spareExpert+=expertInfo.getMember().getId();
                }
            }
            reviewInfo.setSelectExpert(selectExpert);
            reviewInfo.setSpareExpert(spareExpert);
            this.update(reviewInfo);
            return Result.ok("抽取成功！",winBidMemberList);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("评审ID抽取专家抽取异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result randomExpertByProjectId(ReviewInfo reviewInfo) {
        try{
            reviewInfo.setState(1);
            this.insert(reviewInfo);
            List<ExpertInfo> winBidMemberList = new ArrayList<ExpertInfo>();

            // 抽取专家
            List<String> hideExperts = new ArrayList<String>(); // 屏蔽专家
            if(reviewInfo.getHideExpert()!=null && reviewInfo.getHideExpert().length()>0){
                hideExperts =  Arrays.asList(reviewInfo.getHideExpert().split("\\/"));
            }
            List<String> hideAccounts = new ArrayList<String>(); // 屏蔽单位
            if(reviewInfo.getHideAccount()!=null && reviewInfo.getHideAccount().length()>0){
                hideAccounts = Arrays.asList(reviewInfo.getHideAccount().split("\\/"));
            }
            for(Speciality speciality : reviewInfo.getSpeciality()){
                speciality.setReviewInfoId(reviewInfo.getId());
                specialityService.insert(speciality);
                List<ExpertInfo> expertInfoList = reviewInfoMapper.randomExpert(hideExperts,hideAccounts,speciality.getNum(),speciality);
                if(expertInfoList.size()<speciality.getNum()){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.error("【"+speciality.getExpertType()+"】类别的专家数量不足，请调整筛选条件！");
                }
                if(expertInfoList.size()==0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.error("【"+speciality.getExpertType()+"】类别，没有找到符合条件的专家，请调整筛选条件！");
                }
                winBidMemberList.addAll(expertInfoList);
                for(ExpertInfo expertInfo : winBidMemberList){
                    hideExperts.add(""+expertInfo.getMember().getName());
                }
            }
            WinBid winBid;
            String selectExpert = "";
            String spareExpert = "";
            for(int i=0;i<winBidMemberList.size();i++){
                ExpertInfo expertInfo = winBidMemberList.get(i);
                if(reviewInfo.getNumber()>=(i+1)){
                    winBid = new WinBid();
                    winBid.setWinBidId(reviewInfo.getId());
                    winBid.setMemberId(expertInfo.getMember().getId());
                    winBidService.insert(winBid);
                    if(selectExpert.length()>0) selectExpert+=",";
                    selectExpert+=expertInfo.getMember().getId();
                }else{
                    if(spareExpert.length()>0) spareExpert+=",";
                    spareExpert+=expertInfo.getMember().getId();
                }
            }
            reviewInfo.setSelectExpert(selectExpert);
            reviewInfo.setSpareExpert(spareExpert);
            this.update(reviewInfo);
            return Result.ok("抽取成功！",winBidMemberList);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("项目ID抽取专家抽取异常，原因："+e.getMessage());
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

            ReviewInfo reviewInfo1 = this.selectByProjectId(projectId);
            if(!StringUtils.isEmpty(reviewInfo1)){//备选专家重新赋值
                reviewInfo.setId(reviewInfo1.getId());
                reviewInfo.setSpareExpert(reviewInfo1.getSpareExpert());
                reviewInfo.setSpareNumber(reviewInfo1.getSpareNumber());
                this.update(reviewInfo);
            }else{
                this.insert(reviewInfo);
            }
            winBidService.delete(reviewInfo.getId()); // 删除防止重复分配
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
