package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.EvaluationCriteria;
import com.ztbdz.tenderee.service.EvaluationCriteriaService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "评标标准")
@RequestMapping("/evaluation")
@RestController
public class EvaluationCriteriaController {
    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;

    @ApiOperation(value = "查询评标标准列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("select")
    public Result select(@RequestBody EvaluationCriteria evaluationCriteria) {
        return evaluationCriteriaService.select(evaluationCriteria);
    }

    @ApiOperation(value = "新增评标标准")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody List<EvaluationCriteria> evaluationCriteriaList) {
        return evaluationCriteriaService.create(evaluationCriteriaList,true);
    }


    @ApiOperation(value = "更新评标标准")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody EvaluationCriteria evaluationCriteria) {
        return evaluationCriteriaService.update(evaluationCriteria);
    }

    @ApiOperation(value = "删除评标标准")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return evaluationCriteriaService.delete(id);
    }

    @ApiOperation(value = "项目计算中标投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("winBid")
    public Result winBid(@RequestBody EvaluationCriteria evaluationCriteria) {
        return evaluationCriteriaService.winBid(evaluationCriteria);
    }

}
