package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.ReviewInfo;
import com.ztbdz.tenderee.pojo.Speciality;
import com.ztbdz.tenderee.service.ReviewInfoService;
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

import javax.sound.midi.SysexMessage;
import java.util.List;

@Api(tags = "评审信息")
@RequestMapping("/review")
@RestController
public class ReviewInfoController {
    @Autowired
    private ReviewInfoService reviewInfoService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @ApiOperation(value = "查询评审")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "评审id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return reviewInfoService.get(id);
    }

    @ApiOperation(value = "查询评审")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目Id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getProjectId/{projectId}")
    public Result getProjectId(@PathVariable Long projectId) {
        return reviewInfoService.getProjectId(projectId);
    }


    @ApiOperation(value = "新增评审")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody ReviewInfo reviewInfo) {
        return reviewInfoService.create(reviewInfo);
    }

    @ApiOperation(value = "修改评审")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody ReviewInfo reviewInfo) {
        return reviewInfoService.update(reviewInfo);
    }


    @ApiOperation(value = "评审列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "projectName", value = "项目名称", required=false, dataType = "String")
    })
    @CheckToken
    @PostMapping("page")
    public Result page(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestParam(required = false) String projectName) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return reviewInfoService.page(page,size, projectName);
    }

    @ApiOperation(value = "批量删除评审")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "评审id", required=true, allowMultiple = true, dataType = "Long",paramType = "query")
    })
    @CheckToken
    @PostMapping("deleteList")
    public Result deleteList(@RequestParam(value="ids") List<Long> ids) {
        return reviewInfoService.deleteList(ids);
    }

    @ApiOperation(value = "评审ID抽取专家抽取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "评审id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "hideExpert", value = "屏蔽专家名称", required=false, dataType = "String"),
            @ApiImplicitParam(name = "hideAccount", value = "屏蔽工作单位", required=false, dataType = "String")
    })
    @CheckToken
    @PostMapping("randomExpertById")
    public Result randomExpertById(@RequestParam(required = true) Long id,@RequestParam String hideExpert, @RequestParam String hideAccount,@RequestBody List<Speciality> specialityList) {
        return reviewInfoService.randomExpertById(id,hideExpert,hideAccount,specialityList);
    }

    @ApiOperation(value = "项目ID抽取专家抽取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("randomExpertByProjectId")
    public Result randomExpertByProjectId(@RequestBody ReviewInfo reviewInfo) {
        return reviewInfoService.randomExpertByProjectId(reviewInfo);
    }


    @ApiOperation(value = "专家评审列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "projectName", value = "项目名称", required=false, dataType = "String")
    })
    @CheckToken
    @PostMapping("expertReviewInfoList")
    public Result expertReviewInfoList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false) Integer size,
                                       @RequestParam(required = false) String projectName) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return reviewInfoService.expertReviewInfoList( page,  size,  projectName);
    }

    @ApiOperation(value = "分配评审专家")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("assignReviewExperts")
    public Result assignReviewExperts(@RequestBody ReviewInfo reviewInfo) {
        return reviewInfoService.assignReviewExperts(reviewInfo);
    }

    @ApiOperation(value = "汇总评审专家投票")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "评审id", required=true, dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "String"),
            @ApiImplicitParam(name = "num", value = "总人数", required=true, dataType = "String")
    })
    @CheckToken
    @PostMapping("voteLeader")
    public Result assignReviewExperts(@RequestParam String id,@RequestParam String memberId,@RequestParam String num) {
        return reviewInfoService.voteLeader(id,memberId,num);
    }

    @ApiOperation(value = "获取专家组长")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "评审id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getLeader/{id}")
    public Result getLeader(@PathVariable Long id) {
        Object member = redisTemplate.opsForValue().get("Leader:" + id);
        if(StringUtils.isEmpty(member)){
            return Result.fail("未选取专家组长！");
        }
        return Result.ok("查询成功！",member);
    }


}
