package com.ztbdz.user.controller;


import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.service.BidderInfoService;
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

import java.util.Date;

@Api(tags = "投标方信息")
@RequestMapping("/bidderInfo")
@RestController
public class BidderInfoController {

    @Autowired
    private BidderInfoService bidderInfoService;

    @ApiOperation(value = "查询投标方根据人员id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "人员id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getMemberInfo/{memberId}")
    public Result getMemberInfo(@PathVariable Long memberId) {
        return bidderInfoService.getMemberInfo(memberId);
    }

    @ApiOperation(value = "查询投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return bidderInfoService.getMemberAndAccount(id);
    }

    @ApiOperation(value = "更新投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody BidderInfo bidderInfo) {
        bidderInfo.setIsCheck(0);
        bidderInfo.setSubmitDate(new Date());
        return bidderInfoService.update(bidderInfo);
    }

    @ApiOperation(value = "审核投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("review")
    public Result review(@RequestBody BidderInfo bidderInfo) {
        bidderInfo.setCheckDate(new Date());
        return bidderInfoService.review(bidderInfo);
    }

    @ApiOperation(value = "注册投标方")
    @PostMapping("register")
    public Result register(@RequestBody BidderInfo bidderInfo) {
        bidderInfo.setIsCheck(0);
        bidderInfo.setSubmitDate(new Date());
        return bidderInfoService.register(bidderInfo);
    }

    @ApiOperation(value = "查询审核投标方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("page")
    public Result page(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) BidderInfo bidderInfo) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return bidderInfoService.list(page,size,bidderInfo);
    }



}
