package com.ztbdz.tenderee.controller;

import com.ztbdz.tenderee.pojo.Blacklist;
import com.ztbdz.tenderee.service.BlacklistService;
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

import java.util.List;

@Api(tags = "黑名单")
@RequestMapping("/blacklist")
@RestController
public class BlacklistController {

    @Autowired
    private BlacklistService blacklistService;

    @ApiOperation(value = "根据id查询黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "黑名单id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return blacklistService.get(id);
    }

    @ApiOperation(value = "查询黑名单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("page")
    public Result page(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Blacklist blacklist) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return blacklistService.page(page,size, blacklist);
    }

    @ApiOperation(value = "创建或更新黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("createOrUpdate")
    public Result createOrUpdate(@RequestBody Blacklist blacklist) {
        return blacklistService.createOrUpdate(blacklist);
    }

    @ApiOperation(value = "批量删除黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "黑名单id", required=true, allowMultiple = true, dataType = "Long",paramType = "query")
    })
    @CheckToken
    @PostMapping("deleteList")
    public Result deleteList(@RequestParam(value="ids") List<Long> ids) {
        return blacklistService.deleteList(ids);
    }

}
