package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.EvaluationCriteriaMapper;
import com.ztbdz.tenderee.pojo.EvaluationCriteria;
import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.service.EvaluationCriteriaService;
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

import java.util.List;


@Slf4j
@Service
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {
    @Autowired
    private EvaluationCriteriaMapper evaluationCriteriaMapper;
    @Autowired
    private ProjectService projectService;

    @Override
    public Result select(EvaluationCriteria evaluationCriteria) {
        try{
            return Result.ok("查询成功！",this.selectMember(evaluationCriteria));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询评标标准列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<EvaluationCriteria> selectMember(EvaluationCriteria evaluationCriteria) throws Exception {
        return evaluationCriteriaMapper.selectMember(evaluationCriteria);
    }

    @Override
    public List<EvaluationCriteria> selectList(EvaluationCriteria evaluationCriteria) throws Exception {
        QueryWrapper<EvaluationCriteria> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort");

        if(!StringUtils.isEmpty(evaluationCriteria.getProjectId())) queryWrapper.eq("project_id", evaluationCriteria.getProjectId().toString());
        if(!StringUtils.isEmpty(evaluationCriteria.getReviewType())) queryWrapper.eq("review_type", evaluationCriteria.getReviewType().toString());
        if(!StringUtils.isEmpty(evaluationCriteria.getProjectRegisterId())) queryWrapper.eq("project_register_id", evaluationCriteria.getProjectRegisterId().toString());
        if(!StringUtils.isEmpty(evaluationCriteria.getEvaluationCriteriaType())) queryWrapper.eq("evaluation_criteria_type", evaluationCriteria.getEvaluationCriteriaType());
        return evaluationCriteriaMapper.selectList(queryWrapper);
    }

    @Override
    public Result update(EvaluationCriteria evaluationCriteria) {
        try{
            if(this.updateById(evaluationCriteria)<=0)return Result.fail("更新失败！");
            return Result.ok("更新成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("更新评标标准异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer updateById(EvaluationCriteria evaluationCriteria) throws Exception {
        return evaluationCriteriaMapper.updateById(evaluationCriteria);
    }

    @Override
    public Result delete(Long id) {
        try{
            if(this.deleteById(id)<=0)return Result.fail("删除失败！");
            return Result.ok("删除成功！");
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("删除评标标准异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer deleteById(Long id) throws Exception {
        return evaluationCriteriaMapper.deleteById(id);
    }


    @Override
    public Integer deleteByProjectIdAndReviewType(Long projectId,Integer reviewType) throws Exception {
        QueryWrapper<EvaluationCriteria> queryWrapper = new QueryWrapper();
        queryWrapper.eq("project_id",projectId);
        queryWrapper.eq("review_type",reviewType);
        return evaluationCriteriaMapper.delete(queryWrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result create(List<EvaluationCriteria> evaluationCriteriaList,boolean isDelete) {
        try{
            if(isDelete){
                // 先删除 后添加 根据项目id和评审类型
                if(evaluationCriteriaList.size()>0){
                    Integer reviewType = evaluationCriteriaList.get(0).getReviewType();
                    Long projectId = evaluationCriteriaList.get(0).getProjectId();
                    if(StringUtils.isEmpty(reviewType) || StringUtils.isEmpty(projectId) ){
                        return Result.fail("项目id和评审类型不能为空！");
                    }
                    this.deleteByProjectIdAndReviewType(projectId,reviewType);
                    // 保存项目进度
                    Project project = new Project();
                    project.setId(projectId);
                    if(reviewType==1){ // 1=资格评审标准
                        project.setReviewProgress(25);
                    }else if(reviewType==2){ // 2=实质性符合标准
                        project.setReviewProgress(50);
                    }else if(reviewType==3){ // 3=评分标准
                        project.setReviewProgress(75);
                    }
                    projectService.updateById(project);
                }
            }

            Long memberId = SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID);
            for(EvaluationCriteria evaluationCriteria : evaluationCriteriaList){
                Member member = new Member();
                member.setId(memberId);// 默认当前登录人员
                evaluationCriteria.setMember(member);
                if(this.insert(evaluationCriteria)<=0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return Result.fail("添加失败！");
                }
            }
            return Result.ok("添加成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("添加评标标准异常，原因："+e.getMessage());
        }
    }

    @Override
    public Integer insert(EvaluationCriteria evaluationCriteria) {
        return evaluationCriteriaMapper.insert(evaluationCriteria);
    }
}
