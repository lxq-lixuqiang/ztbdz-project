package com.ztbdz.user.controller;

import com.ztbdz.user.pojo.Account;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.service.RoleService;
import com.ztbdz.user.web.token.CheckToken;
import com.ztbdz.user.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色信息")
@RequestMapping("/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "角色id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return roleService.get(id);
    }

    @ApiOperation(value = "查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false, defaultValue = "20") Integer size,
                       @RequestBody(required = false) Role role) {
        return roleService.list(page,size,role);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @ApiOperation(value = "更新角色")
    @CheckToken
    @PostMapping("update")
    public Result list(@RequestBody Role role) {
        return roleService.update(role);
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "删除角色id", required=true, dataType = "List")
    })
    @CheckToken
    @PostMapping("delete")
    public Result deleteList(@PathVariable List<String> ids) {
        return roleService.deleteList(ids);
    }

}
