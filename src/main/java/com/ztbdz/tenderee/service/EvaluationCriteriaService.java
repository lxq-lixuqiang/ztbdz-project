package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.EvaluationCriteria;
import com.ztbdz.web.util.Result;

import java.util.List;

public interface EvaluationCriteriaService {

    /**
     * 查询评标标准
     * @param evaluationCriteria
     * @return
     */
    Result select(EvaluationCriteria evaluationCriteria);

    /**
     * 查询评标标准
     * @param evaluationCriteria
     * @return
     */
    List<EvaluationCriteria> selectMember(EvaluationCriteria evaluationCriteria) throws Exception;

    /**
     * 查询评标标准
     * @param evaluationCriteria
     * @return
     */
    List<EvaluationCriteria> selectList(EvaluationCriteria evaluationCriteria) throws Exception;

    /**
     * 修改评标标准
     * @param evaluationCriteria
     * @return
     */
    Result update(EvaluationCriteria evaluationCriteria);

    /**
     * 更新评标标准
     * @param evaluationCriteria
     * @return
     */
    Integer updateById(EvaluationCriteria evaluationCriteria) throws Exception;

    /**
     * 删除评标标准
     * @param id
     * @return
     */
    Result delete(Long id);


    /**
     * 删除评标标准
     * @param id
     * @return
     */
    Integer deleteById(Long id) throws Exception;

    /**
     * 删除评标标准
     * @param projectId
     * @param reviewType
     * @return
     */
    Integer deleteByProjectIdAndReviewType(Long projectId,Integer reviewType) throws Exception;

    /**
     * 创建评标标准
     * @param evaluationCriteriaList
     * @return
     */
    Result create(List<EvaluationCriteria> evaluationCriteriaList,boolean isDelete);

    /**
     * 添加评标标准
     * @param evaluationCriteria
     * @return
     */
    Integer insert(EvaluationCriteria evaluationCriteria);
}
