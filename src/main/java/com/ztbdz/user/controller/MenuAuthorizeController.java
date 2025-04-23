package com.ztbdz.user.controller;

import com.ztbdz.user.pojo.MenuAuthorize;
import com.ztbdz.user.service.MenuAuthorizeService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "菜单权限")
@RequestMapping("/meunAuthorize")
@RestController
public class MenuAuthorizeController {

    @Autowired
    private MenuAuthorizeService menuAuthorizeService;

    @ApiOperation(value = "查询权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "权限id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return menuAuthorizeService.get(id);
    }


    @ApiOperation(value = "查询权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false, defaultValue = "20") Integer size,
                       @RequestBody(required = false) MenuAuthorize meunAuthorize) {
        return menuAuthorizeService.list(page,size,meunAuthorize);
    }

    @ApiOperation(value = "修改权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody MenuAuthorize meunAuthorize) {
        return menuAuthorizeService.update(meunAuthorize);
    }

    @ApiOperation(value = "创建权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody MenuAuthorize meunAuthorize) {
        return menuAuthorizeService.create(meunAuthorize);
    }


}
