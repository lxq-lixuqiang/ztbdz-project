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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "resultReport")
public class ResultReportServiceImpl implements ResultReportService {
    @Autowired
    private ResultReportMapper resultReportMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRegisterService projectRegisterService;


    @CacheEvict(cacheNames = "resultReport",allEntries = true)
    @Override
    public Integer insert(ResultReport abandonedBid) throws Exception {
        return resultReportMapper.insert(abandonedBid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result createOrUpdate(ResultReport resultReport) {
        try{
            if (StringUtils.isEmpty(resultReport.getProject()) || StringUtils.isEmpty(resultReport.getProject().getId())) return Result.fail("项目id不能为空！");
            if(StringUtils.isEmpty(resultReport.getId())) {
                Long memberId = SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID);
                resultReport.setMember(new Member(memberId)); // 保存创建人
                List<ResultReport> resultReports = this.select(resultReport);
                if (resultReports.size() > 0) {
                    resultReport.setId(resultReports.get(0).getId());
                }
            }
            if(StringUtils.isEmpty(resultReport.getId())){
                // 不通过 废标操作
                if("1".equals(resultReport.getProject().getIsPass().toString())){
                    List<ProjectRegister> projectRegisterList = projectRegisterService.selectByProjectId(resultReport.getProject().getId(),null);
                    List<Long> ids = new ArrayList();
                    for(ProjectRegister projectRegister : projectRegisterList){
                        ids.add(projectRegister.getId());
                    }
                    ProjectRegister projectRegister = new ProjectRegister();
                    projectRegister.setWinBidState(2);
                    projectRegisterService.updateWinBidState(ids,projectRegister); //更新中标状态

                    Project project = projectService.selectById(resultReport.getProject().getId());
                    project.setState(3);
                    project.setIsPass(resultReport.getProject().getIsPass());
                    project.setReviewEndDate(new Date());
                    projectService.updateById(project); // 更新项目状态
                }
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

    @CacheEvict(cacheNames = "resultReport",allEntries = true)
    @Override
    public Integer updateById(ResultReport resultReport) throws Exception {
        return resultReportMapper.updateById(resultReport);
    }

    @Cacheable
    @Override
    public ResultReport selectById(Long id) throws Exception {
        return resultReportMapper.selectById(id);
    }

    @Cacheable
    @Override
    public Result selectByProjectId(Long projectId) {
        try{
            ResultReport resultReport = new ResultReport();
            Project project = new Project();
            project.setId(projectId);
            resultReport.setProject(project);
            resultReport.setMember(new Member(SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID)));
            List<ResultReport> resultReports = this.select(resultReport);
            if(resultReports.size()>0){
                return Result.ok("查询成功！",resultReports.get(0));
            }else{
                return Result.error("未查询到报告信息！");
            }
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询异常，原因："+e.getMessage());
        }
    }

    @Cacheable
    @Override
    public List<ResultReport> select(ResultReport resultReport) throws Exception {
        QueryWrapper<ResultReport> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("update_date");
        if(!(StringUtils.isEmpty(resultReport.getProject()) || StringUtils.isEmpty(resultReport.getProject().getId()))) queryWrapper.eq("project_id",resultReport.getProject().getId().toString());
        if(!(StringUtils.isEmpty(resultReport.getMember()) || StringUtils.isEmpty(resultReport.getMember().getId()))) queryWrapper.eq("member_id",resultReport.getMember().getId().toString());
        return resultReportMapper.selectList(queryWrapper);
    }
}
