package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.ResultReportMapper;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.tenderee.pojo.ResultReport;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.service.ProjectRegisterService;
import com.ztbdz.tenderee.service.ResultReportService;
import com.ztbdz.tenderee.service.ProjectService;
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
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ResultReportServiceImpl implements ResultReportService {
    @Autowired
    private ResultReportMapper resultReportMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRegisterService projectRegisterService;


    @Override
    public Integer insert(ResultReport abandonedBid) throws Exception {
        return resultReportMapper.insert(abandonedBid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result createOrUpdate(ResultReport resultReport) {
        try{
            if(StringUtils.isEmpty(resultReport.getId())){
                if(StringUtils.isEmpty(resultReport.getProject()) || StringUtils.isEmpty(resultReport.getProject().getId())) return Result.fail("项目id不能为空！");
                Project project = projectService.selectById(resultReport.getProject().getId());
                project.setState(3);
                project.setIsPass(resultReport.getProject().getIsPass());
                project.setReviewProgress(100);
                project.setReviewEndDate(new Date());
                projectService.updateById(project); // 更新项目状态
                if("1".equals(resultReport.getProject().getIsPass().toString())){
                    List<ProjectRegister> projectRegisterList = projectRegisterService.selectByProjectId(project.getId(),null);
                    List<Long> ids = new ArrayList();
                    for(ProjectRegister projectRegister : projectRegisterList){
                        ids.add(projectRegister.getId());
                    }
                    ProjectRegister projectRegister = new ProjectRegister();
                    projectRegister.setWinBidState(2);
                    projectRegisterService.updateWinBidState(ids,projectRegister); //更新中标状态
                }
                resultReport.setMember(new Member(SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID))); // 保存创建人
                Integer num = this.insert(resultReport);
                if(num<=0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("添加失败！");
                }
                return Result.ok("添加成功！");
            }else{
                Integer num = this.updateById(resultReport);
                if(num<=0) return Result.fail("更新失败！");
                return Result.ok("更新成功！");
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("新增或更新废标异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer updateById(ResultReport resultReport) throws Exception {
        return resultReportMapper.updateById(resultReport);
    }

    @Override
    public ResultReport selectById(Long id) throws Exception {
        return resultReportMapper.selectById(id);
    }

    @Override
    public Result selectByProjectId(Long projectId) {
        try{
            ResultReport abandonedBid = new ResultReport();
            Project project = new Project();
            project.setId(projectId);
            abandonedBid.setProject(project);
            return Result.ok("查询成功！",this.select(abandonedBid));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询废标异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<ResultReport> select(ResultReport resultReport) throws Exception {
        QueryWrapper<ResultReport> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");
        if(!(StringUtils.isEmpty(resultReport.getProject()) || StringUtils.isEmpty(resultReport.getProject().getId()))) queryWrapper.eq("project_id",resultReport.getProject().getId().toString());
        if(!(StringUtils.isEmpty(resultReport.getMember()) || StringUtils.isEmpty(resultReport.getMember().getId()))) queryWrapper.eq("member_id",resultReport.getMember().getId().toString());
        return resultReportMapper.selectList(queryWrapper);
    }
}
