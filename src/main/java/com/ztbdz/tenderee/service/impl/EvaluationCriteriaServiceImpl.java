package com.ztbdz.tenderee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.tenderee.mapper.EvaluationCriteriaMapper;
import com.ztbdz.tenderee.pojo.EvaluationCriteria;
import com.ztbdz.tenderee.service.EvaluationCriteriaService;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
@Service
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {
    @Autowired
    private EvaluationCriteriaMapper evaluationCriteriaMapper;

    @Override
    public Result select(EvaluationCriteria evaluationCriteria) {
        try{
            return Result.ok("查询成功！",this.selectList(evaluationCriteria));
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询评标标准列表异常，原因："+e.getMessage());
        }
    }

    @Override
    public List<EvaluationCriteria> selectList(EvaluationCriteria evaluationCriteria) throws Exception {
        QueryWrapper<EvaluationCriteria> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("sort");

        if(!StringUtils.isEmpty(evaluationCriteria.getProjectId())) queryWrapper.like("project_id", evaluationCriteria.getProjectId());
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
}
