package com.ztbdz.tenderee.controller;

import com.ztbdz.tenderee.pojo.ResultReport;
import com.ztbdz.tenderee.pojo.ReviewInfo;
import com.ztbdz.tenderee.service.ResultReportService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Api(tags = "评审结果报告")
@RequestMapping("/resultReport")
@RestController
public class ResultReportController {
    @Autowired
    private ResultReportService resultReportService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "根据项目id获取报告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{projectId}")
    public Result get(@PathVariable Long projectId) {
        return resultReportService.selectByProjectId(projectId);
    }

    @ApiOperation(value = "添加或修改报告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("createOrUpdate")
    public Result createOrUpdate(@RequestBody ResultReport resultReport) {
        return resultReportService.createOrUpdate(resultReport);
    }

    @ApiOperation(value = "临时保存专家评审页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("savePrintContents")
    public Result savePrintContents(@RequestBody ResultReport resultReport) {
        redisTemplate.opsForValue().set("printContents:" + resultReport.getProject().getId()+resultReport.getMember().getId(),resultReport.getResultHtml(),SystemConfig.TOKEN_VALIDITY*2, TimeUnit.MILLISECONDS);
        return Result.ok("临时保存专家评审页面成功！");
    }

    @ApiOperation(value = "获取专家评审页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getPrintContents/{memberId}/{projectId}")
    public Result getPrintContents(@PathVariable Long memberId,@PathVariable Long projectId) {
        Object data = redisTemplate.opsForValue().get("printContents:" +projectId+memberId);
        return Result.ok("查询成功！",data);
    }


}
