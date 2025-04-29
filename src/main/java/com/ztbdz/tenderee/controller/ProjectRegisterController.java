package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.tenderee.service.ProjectRegisterService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "投标报名")
@RequestMapping("/projectRegister")
@RestController
public class ProjectRegisterController {

    @Autowired
    private ProjectRegisterService projectRegisterService;

    @ApiOperation(value = "投标报名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody ProjectRegister projectRegister) {
        return projectRegisterService.create(projectRegister);
    }

    @ApiOperation(value = "中标投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "投标id", required=true, allowMultiple = true, dataType = "Long",paramType = "query")
    })
    @CheckToken
    @PostMapping("winBid")
    public Result winBid(@RequestParam(value="ids") List<Long> ids) {
        return projectRegisterService.winBid(ids);
    }

    @ApiOperation(value = "上传盖章合同")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "投标id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "contractImprint", value = "盖章合同文件id", required=true, dataType = "Long")
    })
    @CheckToken
    @PostMapping("contractImprint")
    public Result contractImprint(@RequestParam(required = true) Long id,@RequestParam(required = true) Long contractImprint) {
        return projectRegisterService.contractImprint(id,contractImprint);
    }

    @ApiOperation(value = "计算投标总分和评标报告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "投标id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "fileId", value = "评标报告id", required=true, dataType = "Long")
    })
    @CheckToken
    @PostMapping("countScore")
    public Result countScore(@RequestParam(required = true) Long id,@RequestParam(required = true) Long fileId) {
        return projectRegisterService.countScore(id,fileId);
    }

    @ApiOperation(value = "查询投标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "投标id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@RequestParam(required = true) Long id) {
        return projectRegisterService.get(id);
    }

    @ApiOperation(value = "查询项目投标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getProject/{projectId}")
    public Result getProject(@RequestParam(required = true) Long projectId) {
        return projectRegisterService.getProject(projectId);
    }

    @ApiOperation(value = "校验报名资质")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "Long")
    })
    @CheckToken
    @PostMapping("checkAptitude/{memberId}")
    public Result checkAptitude(Long memberId) {
        return projectRegisterService.checkAptitude(memberId);
    }

    @ApiOperation(value = "查询投标报名列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false, defaultValue = "20") Integer size,
                       @RequestBody(required = false) Project project) {
        return projectRegisterService.page(page,size,project);
    }
}
