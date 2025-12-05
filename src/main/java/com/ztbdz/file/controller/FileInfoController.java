package com.ztbdz.file.controller;

import com.ztbdz.file.service.FileInfoService;
import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Api(tags = "文件信息")
@RequestMapping("/file")
@RestController
public class FileInfoController {

    @Autowired
    private FileInfoService fileInfoService;

    @ApiOperation(value = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @PostMapping(value = "upload",consumes = "multipart/form-data")
    public Result upload( @RequestParam("files") List<MultipartFile> files) {
        return fileInfoService.upload(files,0);
    }

    @ApiOperation(value = "上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping(value = "uploadImg",consumes = "multipart/form-data")
    public Result uploadImg(@RequestParam("files") List<MultipartFile> files) {
        return fileInfoService.upload(files,2);
    }


    @ApiOperation(value = "预览文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id（仅支持 txt, jpg, jpeg, png, pdf, xml）", required=true, dataType = "Long")
    })
    @GetMapping("preview/{id}")
    public ResponseEntity<Object> preview(@PathVariable Long id) {
        return fileInfoService.preview(id);
    }


    @ApiOperation(value = "下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", required=true, dataType = "Long")
    })
    @GetMapping("download/{id}")
    public ResponseEntity<Object> download(@PathVariable Long id) {
        return fileInfoService.download(id);
    }

    @ApiOperation(value = "查询文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String")
    })
    @CheckToken
    @PostMapping("list")
    public Result list(@RequestBody List<Long> ids) {
        return fileInfoService.list(ids);
    }

    @ApiOperation(value = "查询文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "ids", value = "文件id", required=true, dataType = "String")
    })
    @CheckToken
    @GetMapping("list2/{ids}")
    public Result list2(@PathVariable String ids) {
        List<Long> newIds = new ArrayList();
        for (String id : ids.split(",")){
            newIds.add(Long.valueOf(id));
        }
        return fileInfoService.list(newIds);
    }

    @ApiOperation(value = "查询文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "文件id", required=true, dataType = "Long")
    })
    @CheckToken
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        return fileInfoService.get(id);
    }

    @ApiOperation(value = "PDF文件盖章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "x_axis", value = "盖章位置X轴", required=false, dataType = "float"),
            @ApiImplicitParam(name = "y_axis", value = "盖章位置Y轴", required=false, dataType = "float")
    })
    @CheckToken
    @PostMapping(value = "stampPdf",consumes = "multipart/form-data")
    public Result stampPdf(@RequestParam("pdfFile") MultipartFile pdfFile,
                                            @RequestParam("stampImage") MultipartFile stampImage,
                                            Float x_axis,
                                            Float y_axis) {
        return fileInfoService.stampPdf(pdfFile,stampImage, x_axis, y_axis);
    }

}
