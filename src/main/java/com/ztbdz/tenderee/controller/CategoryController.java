package com.ztbdz.tenderee.controller;


import com.ztbdz.tenderee.pojo.Category;
import com.ztbdz.tenderee.service.CategoryService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "类别")
@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "查询类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "类别id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return categoryService.get(id);
    }

    @ApiOperation(value = "查询类别列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required=false, dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页大小", required=false, dataType = "Integer")
    })
    @CheckToken
    @PostMapping("page")
    public Result page(@RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false) Integer size,
                       @RequestBody(required = false) Category category) {
        if(StringUtils.isEmpty(size)) size=SystemConfig.PAGE_SIZE;
        return categoryService.page(page,size, category);
    }

    @ApiOperation(value = "查询所有分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("classify")
    public Result classify() {
        return categoryService.classify();
    }

    @ApiOperation(value = "查询类别树形列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "categoryClassify", value = "类别分类", required=true, dataType = "String"),
    })
    @CheckToken
    @GetMapping("getTreeNode/{categoryClassify}")
    public Result getTreeNode (@PathVariable String categoryClassify) {
        return categoryService.getTreeNode(categoryClassify);
    }

    @ApiOperation(value = "创建类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("create")
    public Result create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @ApiOperation(value = "修改类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("update")
    public Result update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    @ApiOperation(value = "批量删除类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "类别id", required=true, allowMultiple = true, dataType = "Long",paramType = "query")
    })
    @CheckToken
    @PostMapping("deleteList")
    public Result deleteList(@RequestParam(value="ids") List<Long> ids) {
        return categoryService.deleteList(ids);
    }

    @ApiOperation(value = "上传Excel类别文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping(value = "uploadExcel",consumes = "multipart/form-data")
    public Result uploadExcel( @RequestParam("file") MultipartFile file) {
        return categoryService.uploadExcel(file);
    }

}
