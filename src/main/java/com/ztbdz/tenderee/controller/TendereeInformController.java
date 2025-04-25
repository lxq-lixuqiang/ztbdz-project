package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.TendereeInform;
import com.ztbdz.tenderee.service.TendereeInformService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "招标公告")
@RequestMapping("/tendereeInfo")
@RestController
public class TendereeInformController {
    @Autowired
    private TendereeInformService tendereeInformService;

    @ApiOperation(value = "发布公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody TendereeInform tendereeInform) {
        return tendereeInformService.create(tendereeInform);
    }

    @ApiOperation(value = "修改公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody TendereeInform tendereeInform) {
        return tendereeInformService.update(tendereeInform);
    }

    @ApiOperation(value = "更新发布公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("isPublic")
    public Result isPublic(@RequestBody TendereeInform tendereeInform) {
        return tendereeInformService.update(tendereeInform);
    }

}
