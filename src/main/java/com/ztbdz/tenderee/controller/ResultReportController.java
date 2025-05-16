package com.ztbdz.tenderee.controller;

import com.ztbdz.tenderee.pojo.ResultReport;
import com.ztbdz.tenderee.service.ResultReportService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "评审结果报告")
@RequestMapping("/resultReport")
@RestController
public class ResultReportController {
    @Autowired
    private ResultReportService resultReportService;

    @ApiOperation(value = "根据项目id查询废标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{projectId}")
    public Result get(@PathVariable Long projectId) {
        return resultReportService.selectByProjectId(projectId);
    }

    @ApiOperation(value = "添加或修改废标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("createOrUpdate")
    public Result createOrUpdate(@RequestBody ResultReport resultReport) {
        return resultReportService.createOrUpdate(resultReport);
    }


}
