package com.ztbdz.tenderee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztbdz.tenderee.pojo.EvaluationCriteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationCriteriaMapper extends BaseMapper<EvaluationCriteria> {

    List<EvaluationCriteria> selectMember(EvaluationCriteria evaluationCriteria);
}
