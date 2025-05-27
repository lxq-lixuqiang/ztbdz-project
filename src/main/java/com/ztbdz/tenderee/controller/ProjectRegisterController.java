package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.tenderee.service.ProjectRegisterService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
        projectRegister.setState(3);
        projectRegister.setMember(SystemConfig.getCreateMember());
        return projectRegisterService.create(projectRegister);
    }

    @ApiOperation(value = "更新报名状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody ProjectRegister projectRegister) {
        return projectRegisterService.update(projectRegister);
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
    public Result contractImprint(@RequestParam(required = true) Long id,@RequestParam(required = true) String contractImprint) {
        return projectRegisterService.contractImprint(id,contractImprint);
    }

    @ApiOperation(value = "上传标书")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "投标id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "bidDocumentId", value = "上传标书id", required=true, dataType = "Long")
    })
    @CheckToken
    @PostMapping("bidDocument")
    public Result bidDocument(@RequestParam(required = true) Long id,@RequestParam(required = true) String bidDocumentId) {
        return projectRegisterService.bidDocument(id,bidDocumentId);
    }

    @ApiOperation(value = "计算投标总分和评标报告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "投标id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "fileId", value = "评标报告id", required=true, dataType = "Long")
    })
    @CheckToken
    @PostMapping("countScore")
    public Result countScore(@RequestParam(required = true) Long id,@RequestParam(required = true) String fileId) {
        return projectRegisterService.countScore(id,fileId);
    }

    @ApiOperation(value = "查询投标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "投标id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return projectRegisterService.get(id);
    }

    @ApiOperation(value = "查询项目投标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getProject/{projectId}")
    public Result getProject(@PathVariable Long projectId) {
        return projectRegisterService.getProject(projectId,null);
    }

    @ApiOperation(value = "查询项目报名成功的投标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getProjectSuccess/{projectId}")
    public Result getProjectSuccess(@PathVariable Long projectId) {
        return projectRegisterService.getProject(projectId,1);
    }

    @ApiOperation(value = "校验报名资质")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("checkAptitude/{memberId}")
    public Result checkAptitude(@PathVariable Long memberId) {
        return projectRegisterService.checkAptitude(memberId);
    }

    @ApiOperation(value = "查询正在报名项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("listApplying")
    public Result listApplying(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectRegisterService.page(page,size,project,0);
    }

    @ApiOperation(value = "查询项目已报名的项目列表")
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
        return projectRegisterService.page(page,size,project,1);
    }

    @ApiOperation(value = "查询项目已报名的报名列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("selectByProject")
    public Result selectByProject(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectRegisterService.selectByProject(page,size,project,4);
    }

    @ApiOperation(value = "导出项目已报名的报名")
    @GetMapping("selectByProjectExport")
    public ResponseEntity<byte[]> selectByProjectExport(@RequestBody(required = false) Project project) {
        return projectRegisterService.selectByProjectExport(project,4);
    }

    @ApiOperation(value = "查询投标报名审核状态列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("auditList")
    public Result auditList(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Project project) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return projectRegisterService.page(page,size,project,3);
    }
}
