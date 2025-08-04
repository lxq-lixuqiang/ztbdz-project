package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztbdz.file.pojo.FileInfo;
import com.ztbdz.file.service.FileInfoService;
import com.ztbdz.tenderee.mapper.ProjectRegisterMapper;
import com.ztbdz.tenderee.pojo.EvaluationCriteria;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.tenderee.pojo.WinBid;
import com.ztbdz.tenderee.service.*;
import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.service.BidderInfoService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.export.ProjectRegisterExport;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private FileInfoService fileInfoService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result create(ProjectRegister projectRegister) {
        try{
            // 判断是否在 报名时间 范围内
            Project project = projectService.selectById(projectRegister.getProject().getId());
            long nowDate = new Date().getTime();
            long startDate = project.getSenrollStartDate().getTime(); // 开始时间
            long endDate =project.getEnrollEndDate().getTime(); // 截止时间
            if(!(nowDate>startDate && endDate>nowDate)) return Result.fail("报名日期【"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(project.getSenrollStartDate())+" - "+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(project.getEnrollEndDate())+"】，请在报名时间内报名！");

            // 如果报名未通过的话，可重复报名,删除重复报名数据
            this.deletesByProjectIdAndMemberId(project.getId(),projectRegister.getMember().getId());

            Integer num = this.insert(projectRegister);
            if(num<=0) return Result.fail("报名失败");
            return Result.ok("报名成功");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
            Object id = SystemConfig.getCreateMember().getId();
            List<ProjectRegister> projectRegisterList = this.selectByCountProjectId(project,SystemConfig.getCreateMember().getId(),state);
            if(state==3){ // 进行去重操作
                List<ProjectRegister> newProjectRegisterList = new ArrayList();
                Map<Long,Object> isHavs = new HashMap();
                for(ProjectRegister projectRegister : projectRegisterList){
                    if(isHavs.get(projectRegister.getProject().getId())==null){
                        isHavs.put(projectRegister.getProject().getId(),"");
                        newProjectRegisterList.add(projectRegister);
                    }
                }
                projectRegisterList =  newProjectRegisterList;
            }

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
                    mapProjectRegister.put(projectId,projectRegister);
                }
                for(Project project1 : projectList) {
                    ProjectRegister projectRegister = mapProjectRegister.get(project1.getId().toString());
                    if(projectRegister!=null){
                        project1.setTenderee(projectRegister.getProject().getTenderee());
                        project1.setProjectRegisters(projectRegister);
                    }
                }
            }
            PageHelper.startPage(page, size);
            return Result.ok("查询成功！",new PageInfo(projectList));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目报名列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result pageInvoice(Integer page, Integer size, Project project) {
        try{
            return Result.ok("查询成功！",new PageInfo(this.selectInvoice(project)));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询报名项目的发票列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<ProjectRegister> selectInvoice(Project project) throws Exception {
        return projectRegisterMapper.selectInvoice(project);
    }

    @Override
    public List<ProjectRegister> selectByCountProjectId(Project project,Long memberId,Integer state) throws Exception {
        return projectRegisterMapper.selectByCountProjectId(project,memberId,state);
    }

    @Override
    public Result selectByProject(Integer page, Integer size, Project project, Integer state) {
        try{
            PageHelper.startPage(page, size);
            return Result.ok("查询成功！",new PageInfo(this.selectByCountProjectId(project,SystemConfig.getCreateMember().getId(),state)));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询项目已报名的报名列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public  ResponseEntity<byte[]> selectByProjectExport( Project project, Integer state) {
        try{
            List<ProjectRegister> projectRegisterList = this.selectByCountProjectId(project,SystemConfig.getCreateMember().getId(),state);
            return SystemConfig.excelExport("项目报名情况",ProjectRegisterExport.SelectByProjectExport.converter(projectRegisterList),ProjectRegisterExport.SelectByProjectExport.class);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public  ResponseEntity<byte[]> invoiceOrAuditExport( Project project, Integer exportType) {
        try{
            switch (exportType){
                case 0:
                    List<ProjectRegister> projectRegisterList = this.selectInvoice(project);
                    return SystemConfig.excelExport("开具发票申请列表",ProjectRegisterExport.SelectInvoiceExport.converter(projectRegisterList),ProjectRegisterExport.SelectInvoiceExport.class);
                case 1:
                    Result result = this.page(1,0,project,3);
                    List<Project> projectList = ((PageInfo)result.getData()).getList();
                    List<Long> projectId = new ArrayList();
                    Map<Long,Project> projectMap = new HashMap();
                    for(Project project1 : projectList){
                        projectId.add(project1.getId());
                        projectMap.put(project1.getId(),project1);
                    }
                    List<ProjectRegister> projectRegisterList1 = this.selectProjectByProjectIds(projectId);
                    List<Project> newProject = new ArrayList();
                    for(ProjectRegister projectRegister : projectRegisterList1){
                        if(projectRegister.getState()==0 || projectRegister.getState()==1 || projectRegister.getState()==2){
                            Project project1 = SerializationUtils.clone(projectMap.get(projectRegister.getProject().getId()));
                            project1.setState(project1.getProjectRegisters().getState());
                            project1.setProjectRegisters(projectRegister);
                            newProject.add(project1);
                        }
                    }
                    return SystemConfig.excelExport("项目列表",ProjectRegisterExport.PageExport.converter(newProject),ProjectRegisterExport.PageExport.class);
            }
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return ResponseEntity.notFound().build();
        }
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
        ProjectRegister projectRegister = projectRegisterMapper.selectProjectAndMemberById(id);
        if(!StringUtils.isEmpty(projectRegister.getBidMoney())) projectRegister.setBidMoneyUppercase(SystemConfig.digitUppercase(Double.valueOf(projectRegister.getBidMoney())));
        return projectRegister;
    }

    @Override
    public Result update(ProjectRegister projectRegister) {
        try{
            int num = this.updateById(projectRegister);
            if(num<=0) return Result.fail("更新失败");
            return Result.ok("更新成功");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新投标异常，原因："+e.getMessage());
        }
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
    public Result getProject(Long projectId,Integer state) {
        try{
            return Result.ok("查询成功",this.selectByProjectId(projectId,state));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("根据项目id查询投标异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<ProjectRegister> selectByProjectId(Long projectId, Integer state) throws Exception {
        List<ProjectRegister> projectRegisterList = projectRegisterMapper.getProject(projectId,state);
        for(ProjectRegister projectRegister : projectRegisterList){
            if(StringUtils.isEmpty(projectRegister.getBidDocumentId())) continue;
            String[] fileIdStrings = projectRegister.getBidDocumentId().split(",");
            List<Long> fileIds = new ArrayList<>();
            for(int i=0;i<fileIdStrings.length;i++){
                fileIds.add(Long.valueOf(fileIdStrings[i]));
            }
            List<FileInfo> fileInfoList = fileInfoService.listByIds(fileIds);
            for(FileInfo fileInfo : fileInfoList){
                fileInfo.convertSize();
            }
            projectRegister.setBidDocumentIds(fileInfoList);
        }
        return projectRegisterList;
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
            projectRegister.setBidMoney(String.valueOf(winBidProjectRegisterList.get(0).getProject().getMoney()));
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

    @Override
    public Integer deletesByProjectIdAndMemberId(Long projectId,Long memberId) throws Exception {
        QueryWrapper<ProjectRegister> queryWrapper = new QueryWrapper();
        queryWrapper.in("project_id",projectId.toString());
        queryWrapper.in("member_id",memberId.toString());
        return projectRegisterMapper.delete(queryWrapper);
    }

    @Override
    public List<ProjectRegister> selectProjectByProjectIds(List<Long> projectIds) throws Exception {
        return projectRegisterMapper.selectProjectByProjectIds(projectIds);
    }


}
