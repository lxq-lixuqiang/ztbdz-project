package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.Tenderee;
import com.ztbdz.tenderee.service.TendereeService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "招标信息")
@RequestMapping("/tenderee")
@RestController
public class TendereeController {
    @Autowired
    private TendereeService tendereeService;


    @ApiOperation(value = "新增招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody Tenderee tenderee) {
        return tendereeService.create(tenderee);
    }

    @ApiOperation(value = "修改招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody Tenderee tenderee) {
        return tendereeService.update(tenderee);
    }

    @ApiOperation(value = "查询招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("page")
    public Result page(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Tenderee tenderee) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        if(tenderee==null) tenderee = new Tenderee();
        if(!(tenderee.getProject()!=null && tenderee.getProject().getIsAudit()!=null && tenderee.getProject().getIsAudit()==12)) tenderee.setMember(SystemConfig.getCreateMember());
        return tendereeService.page(page,size,tenderee);
    }

    @ApiOperation(value = "导出招标")
    @GetMapping("pageExport")
    public ResponseEntity<byte[]> pageExport(Tenderee tenderee) {
        return tendereeService.pageExport(tenderee);
    }

    @ApiOperation(value = "查询全部招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("pageAll")
    public Result pageAll(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Tenderee tenderee) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return tendereeService.page(page,size,tenderee);
    }


    @ApiOperation(value = "删除招标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "招标id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        return tendereeService.delete(id);
    }

    @ApiOperation(value = "公示评标结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "招标id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("getWinBidResult/{id}")
    public Result getWinBidResult(@PathVariable Long id) {
        return tendereeService.getWinBidResult(id);
    }


}
