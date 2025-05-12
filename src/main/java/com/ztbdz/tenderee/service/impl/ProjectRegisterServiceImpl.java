package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.tenderee.mapper.ProjectRegisterMapper;
import com.ztbdz.tenderee.pojo.*;
import com.ztbdz.tenderee.service.*;
import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.service.BidderInfoService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class ProjectRegisterServiceImpl implements ProjectRegisterService {

    @Autowired
    private ProjectRegisterMapper projectRegisterMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BidderInfoService bidderInfoService;
    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;
    @Autowired
    private WinBidService winBidService;
    @Autowired
    private TendereeService tendereeService;


    @Override
    public Result create(ProjectRegister projectRegister) {
        try{
            // 判断是否在 报名时间 范围内
            Project project = projectService.selectById(projectRegister.getProject().getId());
            long nowDate = new Date().getTime();
            long startDate = project.getSenrollStartDate().getTime(); // 开始时间
            long endDate =project.getEnrollEndDate().getTime(); // 截止时间
            if(!(nowDate>startDate && endDate>nowDate)) return Result.fail("报名日期【"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(project.getSenrollStartDate())+" - "+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(project.getEnrollEndDate())+"】，请在报名时间内报名！");

            Integer num = this.insert(projectRegister);
            if(num<=0) return Result.fail("报名失败");
            return Result.ok("报名成功");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("项目报名异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(ProjectRegister projectRegister) throws Exception {
        projectRegister.setWinBidState(0); // 默认未公布
        return projectRegisterMapper.insert(projectRegister);
    }

    @Override
    public List<ProjectRegister> selectList(ProjectRegister projectRegister) throws Exception {
        QueryWrapper<ProjectRegister> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(projectRegister.getProject()) && !StringUtils.isEmpty(projectRegister.getProject().getId())){
            queryWrapper.eq("project_id",projectRegister.getProject().getId().toString());
        }
        if(!StringUtils.isEmpty(projectRegister.getMember())) queryWrapper.eq("member_id",projectRegister.getMember().toString());
        if(!StringUtils.isEmpty(projectRegister.getWinBidState())) queryWrapper.eq("win_bid_state",projectRegister.getWinBidState().toString());
        return projectRegisterMapper.selectList(queryWrapper);
    }

    @Override
    public Result page(Integer page, Integer size, Project project,Integer state) {
        try{
            PageHelper.startPage(page, size);
            Long memberId = null;
            if(state ==1) memberId = SystemConfig.getCreateMember().getId();
            List<ProjectRegister> projectRegisterList = this.selectByCountProjectId(project,memberId);
            List<Long> projectIds = new ArrayList();
            for(ProjectRegister projectRegister : projectRegisterList){
                projectIds.add(projectRegister.getProject().getId());
            }
            List<Project> projectList = new ArrayList();
            if(projectIds.size()>0){
                projectList = projectService.selectByIds(projectIds);
                Map<String,ProjectRegister> mapProjectRegister = new HashMap();
                for (ProjectRegister projectRegister : projectRegisterList) {
                    String projectId = projectRegister.getProject().getId().toString();
                    projectRegister.setProject(null);
                    mapProjectRegister.put(projectId,projectRegister);
                }
                for(Project project1 : projectList) {
                    ProjectRegister projectRegister = mapProjectRegister.get(project1.getId().toString());
                    if(projectRegister!=null){
                        project1.setProjectRegisters(projectRegister);
                    }
                }
            }
            return Result.ok("查询成功！",new PageInfo(projectList));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目报名列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<ProjectRegister> selectByCountProjectId(Project project,Long memberId) throws Exception {
        return projectRegisterMapper.selectByCountProjectId(project,memberId);
    }

    @Override
    public Result checkAptitude(Long memberId) {
        try{
            BidderInfo bidderInfo = bidderInfoService.selectByMemberId(memberId);
            if(bidderInfo.getQualificationCertificate01()==null ||
                    bidderInfo.getQualificationCertificate02()==null ||
                    bidderInfo.getQualificationCertificate03()==null ||
                    bidderInfo.getQualificationCertificate04()==null ||
                    bidderInfo.getQualificationCertificate05()==null){
                return Result.fail("校验失败！");
            }
            return Result.ok("校验成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("校验报名资质异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result contractImprint(Long id, String contractImprint) {
        try{
            ProjectRegister projectRegister = new ProjectRegister();
            projectRegister.setId(id);
            projectRegister.setContractImprint(contractImprint);
            Integer num = this.updateById(projectRegister);
            if(num<=0) return Result.fail("上传合同盖章失败");
            return Result.ok("上传合同盖章成功");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("上传合同盖章异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result bidDocument(Long id, String bidDocumentId) {
        try{
            ProjectRegister projectRegister = new ProjectRegister();
            projectRegister.setId(id);
            projectRegister.setBidDocumentId(bidDocumentId);
            Integer num = this.updateById(projectRegister);
            if(num<=0) return Result.fail("上传标书失败");
            return Result.ok("上传标书成功");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("上传标书异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result get(Long id) {
        try{
            return Result.ok("查询成功",this.selectById(id));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询投标异常，原因："+e.getMessage());
        }
    }

    @Override
    public ProjectRegister selectById(Long id) throws Exception {
        return projectRegisterMapper.selectById(id);
    }

    @Override
    public Integer updateById(ProjectRegister projectRegister) throws Exception {
        return projectRegisterMapper.updateById(projectRegister);
    }


    @Override
    public Integer updateWinBidState(List<Long> ids,ProjectRegister projectRegister) throws Exception {
        QueryWrapper<ProjectRegister> queryWrapper = new QueryWrapper();
        queryWrapper.in("id",ids);
        return projectRegisterMapper.update(projectRegister,queryWrapper);
    }

    @Override
    public Result getProject(Long projectId) {
        try{
            return Result.ok("查询成功",projectRegisterMapper.getProject(projectId));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("根据项目id查询投标异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result countScore(Long id,String fileId) {
        try{
            EvaluationCriteria evaluationCriteria = new EvaluationCriteria();
            evaluationCriteria.setProjectRegisterId(id);
            List<EvaluationCriteria> evaluationCriteriaList = evaluationCriteriaService.selectList(evaluationCriteria);
            BigDecimal scoreBD = new BigDecimal("0");
            for(EvaluationCriteria evaluationCriteria1 : evaluationCriteriaList){
                BigDecimal getScoreBD = new BigDecimal(evaluationCriteria1.getScore());
                scoreBD=scoreBD.add(getScoreBD);
            }
            ProjectRegister projectRegister = new ProjectRegister();
            projectRegister.setId(id);
            projectRegister.setScore(scoreBD.intValue());
            projectRegister.setBidEvaluationReport(fileId);
            this.updateById(projectRegister);
            return Result.ok("计算成功",scoreBD.intValue());
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("统计投标总分数异常，原因："+e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result winBid(List<Long> ids) {
        try{
            List<ProjectRegister> projectRegisterList = this.selectListById(ids.get(0),ids);
            List<Long> notBidIds = new ArrayList();
            for(ProjectRegister projectRegister : projectRegisterList){
                notBidIds.add(projectRegister.getId());
            }
            // 保存中标信息
            List<ProjectRegister> winBidProjectRegisterList = this.selectMemberProjectByIds(ids);
            winBidService.delete(winBidProjectRegisterList.get(0).getProject().getId());
            for(ProjectRegister projectRegister : winBidProjectRegisterList){
                WinBid winBid = new WinBid();
                winBid.setWinBidId(projectRegister.getProject().getId());
                winBid.setMemberId(projectRegister.getMember().getId());
                winBidService.insert(winBid);
            }

            ProjectRegister projectRegister = new ProjectRegister();
            // 更新已中标状态
            projectRegister.setWinBidState(1);
            this.updateWinBidState(ids,projectRegister);
            // 更新未中标状态
            if(notBidIds.size()>0){
                projectRegister.setWinBidState(2);
                this.updateWinBidState(notBidIds,projectRegister);
            }
            return Result.ok("中标成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新中标投标异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<ProjectRegister> selectListById(Long id, List<Long> notIds) throws Exception {
        return projectRegisterMapper.selectListById(id, notIds);
    }

    @Override
    public List<ProjectRegister> selectMemberProjectByIds(List<Long> ids) throws Exception {
        return projectRegisterMapper.selectMemberProjectByIds(ids);
    }


}
