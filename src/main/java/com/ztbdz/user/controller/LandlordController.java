package com.ztbdz.user.controller;

import com.ztbdz.user.pojo.Landlord;
import com.ztbdz.user.pojo.Role;
import com.ztbdz.user.service.LandlordService;
import com.ztbdz.user.service.LoginService;
import com.ztbdz.user.web.token.CheckToken;
import com.ztbdz.user.web.token.LoginToken;
import com.ztbdz.user.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "业主信息")
@RequestMapping("/landlord")
@RestController
public class LandlordController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private LandlordService landlordService;

    @ApiOperation(value = "业主登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required=true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号", required=true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "短信验证码", required=false, dataType = "String")
    })
    @LoginToken
    @PostMapping("loginLandlord")
    public Result login(String username, String phone,String code) {
        return loginService.loginLandlord(username,phone,code);
    }

    @ApiOperation(value = "查询业主列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false, defaultValue = "20") Integer size,
                       @RequestBody(required = false) Landlord landlord) {
        return landlordService.list(page,size,landlord);
    }

    @ApiOperation(value = "业主注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "短信验证码", required=false, dataType = "String")
    })
    @PostMapping("create")
    public Result create(@RequestBody Landlord landlord,@RequestParam(required = false) String code) {
        return landlordService.create(landlord,code);
    }

    @ApiOperation(value = "修改业主")
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody Landlord landlord) {
        return landlordService.updateById(landlord);
    }

    @ApiOperation(value = "查询业主")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "业主id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return landlordService.get(id);
    }


    @ApiOperation(value = "批量删除业主")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "删除业主id", required=true, allowMultiple = true, dataType = "Long",paramType = "query")
    })
    @CheckToken
    @PostMapping("deleteList")
    public Result deleteList(@RequestParam(value="ids") List<Long> ids) {
        return landlordService.deleteList(ids);
    }

}
