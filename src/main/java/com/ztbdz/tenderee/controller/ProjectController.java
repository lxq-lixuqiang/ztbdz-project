package com.ztbdz.tenderee.controller;

import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.service.ProjectService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "项目信息")
@RequestMapping("/project")
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "查询项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,1);
    }

    @ApiOperation(value = "查询需要抽取专家项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "state", value = "抽取状态", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("extractProjectList")
    public Result extractProjectList(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestParam(required = false) Integer state,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.extractProjectList(page,size,state,project);
    }

    @ApiOperation(value = "查询评审结束项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("reviewEndProject")
    public Result reviewEndProject(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,8);
    }

    @ApiOperation(value = "查询正在进行项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("runProject")
    public Result runProject(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,5);
    }

    @ApiOperation(value = "查询可评审项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("reviewProject")
    public Result reviewProject(@RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false) Integer size,
                                @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,6);
    }

    @ApiOperation(value = "查询专家可评审项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("expertReviewProject")
    public Result expertReviewProject(@RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false) Integer size,
                                @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,9);
    }

    @ApiOperation(value = "查询专家正在评审项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("expertRunReviewProject")
    public Result expertRunReviewProject(@RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false) Integer size,
                                      @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,10);
    }

    @ApiOperation(value = "查询专家项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("expertList")
    public Result expertList(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if (StringUtils.isEmpty(size)) size = SystemConfig.PAGE_SIZE;
        return projectService.page(page, size, project, 11);
    }

    @ApiOperation(value = "查询正在评审项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("runReviewProject")
    public Result runReviewProject(@RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false) Integer size,
                                @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,7);
    }

    @ApiOperation(value = "可报名项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("listAvailable")
    public Result listAvailable(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,2);
    }

    @ApiOperation(value = "已报名项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("listProject1")
    public Result listProject1(@RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false) Integer size,
                                @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,3);
    }

    @ApiOperation(value = "正在报名项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("listApplyingInit")
    public Result listApplyingInit(@RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false) Integer size,
                               @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectService.page(page,size,project,4);
    }

    @ApiOperation(value = "查询项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return projectService.get(id);
    }


    @ApiOperation(value = "项目ID获取中标人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("winDid/{projectId}")
    public Result winDid(@PathVariable Long projectId) {
        return projectService.winDid(projectId);
    }

}
