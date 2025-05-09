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
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "展示已发布公告")
    @PostMapping("list")
    public Result list(@RequestBody TendereeInform tendereeInform) {
        return tendereeInformService.list(tendereeInform);
    }

    @ApiOperation(value = "公告详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公告id", required=true, dataType = "Long")
    })
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return tendereeInformService.get(id);
    }

}
