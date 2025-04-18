package com.ztbdz.user.controller;

import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.LoginService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.token.CheckToken;
import com.ztbdz.user.web.token.LoginToken;
import com.ztbdz.user.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "用户信息（登录、注册）")
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
    @LoginToken
    @PostMapping("login")
    public Result login(String username,String password) {
        return loginService.login(username,password);
    }

    @ApiOperation(value = "登录退出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("logout")
    public Result logout(@RequestHeader String token) {
        return loginService.logout(token);
    }


    @ApiOperation(value = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "短信验证码", required=false, dataType = "String")
    })
    @PostMapping("create")
    public Result create(@RequestBody User user,@RequestParam(required = false) String code) {
        return userService.create(user,code);
    }

    @ApiOperation(value = "更新密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", required=true, dataType = "Long"),
            @ApiImplicitParam(name = "password", value = "原密码", required=true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required=true, dataType = "String")
    })
    @CheckToken
    @PostMapping("updatePassword")
    public Result updatePassword(Long userId,String password, String newPassword) {
        return userService.updatePassword(userId,password,newPassword);
    }

    @ApiOperation(value = "忘记密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required=true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required=true, dataType = "String")
    })
    @PostMapping("forgetPassword")
    public Result forgetPassword(String phone, String newPassword) {
        return userService.forgetPassword(phone,newPassword);
    }


    @ApiOperation(value = "发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required=true, dataType = "String")
    })
    @PostMapping("sendSMS")
    public Result sendSMS(String phone) {
        return userService.sendSMS(phone);
    }

}
