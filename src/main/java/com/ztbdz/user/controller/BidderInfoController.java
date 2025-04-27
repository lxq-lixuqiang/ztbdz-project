package com.ztbdz.user.controller;


import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.service.BidderInfoService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "投标方信息")
@RequestMapping("/bidderInfo")
@RestController
public class BidderInfoController {

    @Autowired
    private BidderInfoService bidderInfoService;

    @ApiOperation(value = "查询投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getMemberInfo/{memberId}")
    public Result getMemberInfo(@PathVariable Long memberId) {
        return bidderInfoService.getMemberInfo(memberId);
    }

    @ApiOperation(value = "更新投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody BidderInfo bidderInfo) {
        return bidderInfoService.update(bidderInfo);
    }





}
