package com.ztbdz.file.controller;

import com.ztbdz.file.service.FileInfoService;
import com.ztbdz.user.web.token.CheckToken;
import com.ztbdz.user.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
    @PostMapping(value = "uploads",consumes = "multipart/form-data")
    public Result upload( MultipartFile multipartFile) {
        return fileInfoService.upload(multipartFile);
    }


    @ApiOperation(value = "查看文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", required=true, dataType = "Long")
    })
    @GetMapping("read/{id}")
    public Result read(@PathVariable Long id) {
//        return fileService.read(id);
        return null;
    }

    @ApiOperation(value = "下载文件")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
//            @ApiImplicitParam(name = "id", value = "文件id", required=true, dataType = "Long")
//    })
//    @CheckToken
    @GetMapping("download/{id}")
    public HttpEntity<byte[]> download(@PathVariable Long id) {

//        File file= new File(absPath, game);
//        byte[] body=FileUtils.readFileToByteArray(file);
//
//        HttpHeaders headers=new HttpHeaders();
//        headers.setContentDispositionFormData("attachment", game);
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        return new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
//        return fileService.download(id);
        return null;
    }
}
