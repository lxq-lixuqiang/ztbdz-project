package com.ztbdz.user.controller;


import com.ztbdz.user.pojo.ExpertInfo;
import com.ztbdz.user.service.ExpertInfoService;
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
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "专家信息")
@RequestMapping("/expertInfo")
@RestController
public class ExpertInfoController {
    @Autowired
    private ExpertInfoService expertInfoService;

    @ApiOperation(value = "添加专家")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody ExpertInfo expertInfo) {
        return expertInfoService.create(expertInfo);
    }


    @ApiOperation(value = "查询专家")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getMemberInfo/{memberId}")
    public Result getMemberInfo(@PathVariable Long memberId) {
        return expertInfoService.getMemberInfo(memberId);
    }

    @ApiOperation(value = "更新专家")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody ExpertInfo expertInfo) {
        return expertInfoService.update(expertInfo);
    }

    @ApiOperation(value = "查询未审核专家")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                         @RequestParam(required = false) Integer size,
                         @RequestBody(required = false) ExpertInfo expertInfo) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        expertInfo.setIsCheck(0);
        return expertInfoService.list(page,size,expertInfo);
    }

    @ApiOperation(value = "上传Excel专家文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping(value = "uploadExcel",consumes = "multipart/form-data")
    public Result uploadExcel( @RequestParam("file") MultipartFile file) {
        return expertInfoService.uploadExcel(file);
    }


}
