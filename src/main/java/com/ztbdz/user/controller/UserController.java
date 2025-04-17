package com.ztbdz.user.controller;

import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.LoginService;
import com.ztbdz.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "用户登录、注册")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required=true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required=true, dataType = "String")
    })
    @PostMapping("login")
    public Map login(String username,String password) {
        return loginService.login(username,password);
    }

    @ApiOperation(value = "登录退出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required=true, dataType = "String")
    })
    @PostMapping("logout")
    public Map logout(String token) {
        return loginService.logout(token);
    }


    @ApiOperation(value = "用户注册")
    @PostMapping("create")
    public Map create(@RequestBody User user) {
        return userService.create(user);
    }

    @ApiOperation(value = "更新密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "password", value = "原密码", required=true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required=true, dataType = "String")
    })
    @PostMapping("update")
    public Map update(Long userId,String password, String newPassword) {
        return userService.update(userId,password,newPassword);
    }


}
