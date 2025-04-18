package com.ztbdz.user.controller;


import com.ztbdz.user.pojo.Member;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.web.token.CheckToken;
import com.ztbdz.user.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "人员信息")
@RequestMapping("/member")
@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "查询人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "人员id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return memberService.get(id);
    }


    @ApiOperation(value = "查询人员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                        @RequestParam(required = false, defaultValue = "20") Integer size,
                        @RequestBody(required = false) Member member) {
        return memberService.list(page,size,member);
    }

    @ApiOperation(value = "更新人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result list(@RequestBody Member member) {
        return memberService.update(member);
    }


}
