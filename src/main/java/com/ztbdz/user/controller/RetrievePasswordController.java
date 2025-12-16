package com.ztbdz.user.controller;


import com.ztbdz.user.pojo.RetrievePassword;
import com.ztbdz.user.service.RetrievePasswordService;
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

@Api(tags = "审核找回密码")
@RequestMapping("/retrievePassword")
@RestController
public class RetrievePasswordController {

    @Autowired
    private RetrievePasswordService retrievePasswordService;

    @ApiOperation(value = "查询找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("selectBidderInfo/{id}")
    public Result selectBidderInfo(@PathVariable Long id) {
        return retrievePasswordService.selectBidderInfo(id);
    }


    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("page")
    public Result list(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) RetrievePassword retrievePassword) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return retrievePasswordService.page(page,size,retrievePassword);
    }

    @ApiOperation(value = "新增或修改信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @PostMapping("addORupdate")
    public Result addORupdate(@RequestBody RetrievePassword retrievePassword) {
        return retrievePasswordService.createORupdate(retrievePassword);
    }

    @ApiOperation(value = "审核信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("review")
    public Result review(@RequestBody RetrievePassword retrievePassword) {
        return retrievePasswordService.review(retrievePassword);
    }

    @ApiOperation(value = "查询审核状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "用户名", required=true, dataType = "String")
    })
    @GetMapping("selectReview/{username}")
    public Result selectReview(@PathVariable String username) {
        return retrievePasswordService.selectByUsername(username);
    }


}
