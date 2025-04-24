package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.Tenderee;
import com.ztbdz.tenderee.service.TendereeService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "招标信息")
@RequestMapping("/tenderee")
@RestController
public class TendereeController {
    @Autowired
    private TendereeService tendereeService;


    @ApiOperation(value = "新增招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody Tenderee tenderee) {
        return tendereeService.create(tenderee);
    }

    @ApiOperation(value = "修改招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody Tenderee tenderee) {
        return tendereeService.update(tenderee);
    }

    @ApiOperation(value = "删除招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "招标id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return tendereeService.delete(id);
    }


}
