package com.ztbdz.tenderee.controller;

import com.alibaba.fastjson.JSON;
import com.ztbdz.tenderee.pojo.ResultReport;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
    @PostMapping("savePrintContents")
    public Result savePrintContents(@RequestBody ResultReport resultReport) {
        // 临时保存专家评审页面 时效为2周
        redisTemplate.opsForValue().set("printContents:" + resultReport.getProject().getId()+resultReport.getMember().getId(),resultReport.getResultHtml(),SystemConfig.TOKEN_VALIDITY*112, TimeUnit.MILLISECONDS);
        return Result.ok("临时保存专家评审页面成功！");
    }

    @ApiOperation(value = "获取专家评审页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @GetMapping("getPrintContents/{memberId}/{projectId}")
    public Result getPrintContents(@PathVariable Long memberId,@PathVariable Long projectId) {
        Object data = redisTemplate.opsForValue().get("printContents:" +projectId+memberId);
        return Result.ok("查询成功！",data);
    }

    @ApiOperation(value = "更新澄清响应状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "String"),
            @ApiImplicitParam(name = "accountId", value = "供应商id", required=true, dataType = "String"),
            @ApiImplicitParam(name = "state", value = "状态", required=true, dataType = "String")
    })
    @GetMapping("updateState/{projectId}/{accountId}/{state}")
    public Result updateState(@PathVariable String projectId,@PathVariable String accountId,@PathVariable String state) {
        Object data = redisTemplate.opsForValue().get("clarifyState:" +projectId);
        Map<String,String> map = new HashMap();
        if(!StringUtils.isEmpty(data)){
            map = JSON.parseObject(data.toString(),Map.class);
        }
        map.put(projectId+"|"+accountId,state);
        redisTemplate.opsForValue().set("clarifyState:" + projectId,JSON.toJSONString(map),SystemConfig.TOKEN_VALIDITY*4, TimeUnit.MILLISECONDS);
        return Result.ok("更新澄清响应状态成功！");
    }

    @ApiOperation(value = "获取澄清响应状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "String"),
            @ApiImplicitParam(name = "accountId", value = "供应商id", required=true, dataType = "String")
    })
    @GetMapping("getState/{projectId}")
    public Result getState(@PathVariable String projectId) {
        Object data = redisTemplate.opsForValue().get("clarifyState:" +projectId);
        Map<String,String> map = new HashMap();
        if(!StringUtils.isEmpty(data)){
            map = JSON.parseObject(data.toString(),Map.class);
        }
        return Result.ok("获取澄清响应状态成功！",map);
    }


}
