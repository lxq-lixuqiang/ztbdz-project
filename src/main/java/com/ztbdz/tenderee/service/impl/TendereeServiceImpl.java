package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.TendereeMapper;
import com.ztbdz.tenderee.pojo.*;
import com.ztbdz.tenderee.service.*;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TendereeServiceImpl implements TendereeService {
    @Autowired
    private TendereeMapper tendereeMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TenderService tenderService;
    @Autowired
    private ProjectRegisterService projectRegisterService;
    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result create(Tenderee tenderee) {
        try{
            // 判断 投标报名，截止时间，开标时间 必填
            if(StringUtils.isEmpty(tenderee.getProject().getSenrollStartDate()) || StringUtils.isEmpty(tenderee.getProject().getEnrollEndDate()))  return Result.fail("投标开始时间和投标截止时间不能为空！");
            if(StringUtils.isEmpty(tenderee.getProject().getBidOpeningTime()))  return Result.fail("开标时间不能为空！");
            // 项目信息
            Project project = tenderee.getProject();
            projectService.insert(project);
            // 标段
            List<Tender> tenders =  project.getTenders();
            for(Tender tender : tenders){
                tender.setProjectId(project.getId());
                tenderService.insert(tender);
            }
            this.insert(tenderee);
            return Result.ok("新增成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("新增招标信息异常，原因："+e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(Tenderee tenderee) {
        try{
            //更新项目信息
            Project project = tenderee.getProject();
            if(!(StringUtils.isEmpty(tenderee) || StringUtils.isEmpty(tenderee.getProject()) || StringUtils.isEmpty(tenderee.getProject().getId()))) {
                projectService.updateById(project);
                //更新标段,可能有新增或删除情况，所以采用先删除再添加
                List<Tender> tenderList = project.getTenders();
                if(!StringUtils.isEmpty(tenderList)) {
                    tenderService.deleteByProjectId(project.getId());
                    for (Tender tender : tenderList) {
                        tenderService.insert(tender);
                    }
                }
            }
            //更新招标信息
            if(!(StringUtils.isEmpty(tenderee) || StringUtils.isEmpty(tenderee.getId()))){
                Integer num = this.updateById(tenderee);
                if(num<=0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("更新失败！");
                }
            }
            return Result.ok("更新成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新项目异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer deleteById(Long id) throws Exception {
        return tendereeMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result delete(Long id) {
        try{
            Tenderee tenderee = this.selectProject(id);
            Long projectId = tenderee.getProject().getId();
            //删除标段
            tenderService.deleteByProjectId(projectId);
            //删除项目信息
            projectService.delete(projectId);
            //删除招标信息
            if(this.deleteById(id)<=0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.fail("删除失败！");
            }
            return Result.ok("删除成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("删除招标异常，原因："+e.getMessage());
        }
    }

    @Override
    public Tenderee selectProject(Long id) throws Exception {
        return tendereeMapper.selectProject(id);
    }

    @Override
    public Result getWinBidResult(Long id) {
        try{
            // 招标信息和项目信息
            Tenderee tenderee = this.selectProject(id);
            ProjectRegister projectRegister = new ProjectRegister();
            projectRegister.setProject(tenderee.getProject());
            projectRegister.setWinBidState(1);
            // 获取到中标投标方和得分结果
            List<ProjectRegister> projectRegisterList = projectRegisterService.selectList(projectRegister);
            if(projectRegisterList==null || projectRegisterList.size()==0) return Result.fail("此招标项目还未公布中标结果!!!");
            EvaluationCriteria evaluationCriteria;
            for(ProjectRegister projectRegister1 : projectRegisterList){
                evaluationCriteria = new EvaluationCriteria();
                evaluationCriteria.setProjectRegisterId(projectRegister1.getId());
                // 获取项目的评分标准
                projectRegister1.setEvaluationCriterias(evaluationCriteriaService.selectList(evaluationCriteria));
            }
            Map<String,Object> resultMap = new HashMap();
            resultMap.put("tenderee",tenderee); // 招标信息和项目信息
            resultMap.put("projectRegister",projectRegisterList); // 获取到中标投标方和得分结果 评分标准
            return Result.ok("查询成功！",resultMap);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询中标结果异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result list(Tenderee tenderee) {
        try{
            return Result.ok("查询成功！",this.selectList(tenderee));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询招标列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<Tenderee> selectList(Tenderee tenderee) throws Exception {
        return tendereeMapper.selectListProject(tenderee);
    }

    @Override
    public Integer insert(Tenderee tenderee) throws Exception {
        // 创建人默认赋值
        tenderee.setMember(SystemConfig.getCreateMember());
        return tendereeMapper.insert(tenderee);
    }

    @Override
    public Tenderee selectByProjectId(Long projectId) throws Exception {
        QueryWrapper<Tenderee> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project_id", projectId.toString());
        return tendereeMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateById(Tenderee tenderee) throws Exception {
        return tendereeMapper.updateById(tenderee);
    }
}
