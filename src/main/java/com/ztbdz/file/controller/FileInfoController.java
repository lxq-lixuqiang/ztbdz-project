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

import org.springframework.core.io.Resource;


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
    @CheckToken
    @PostMapping(value = "upload",consumes = "multipart/form-data")
    public Result upload( MultipartFile file) {
        return fileInfoService.upload(file,0);
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
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        return fileInfoService.download(id);
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
