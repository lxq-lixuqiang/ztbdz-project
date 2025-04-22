package com.ztbdz.file.service.impl;


import com.ztbdz.file.mapper.FileInfoMapper;
import com.ztbdz.file.pojo.FileInfo;
import com.ztbdz.file.service.FileInfoService;
import com.ztbdz.user.web.config.SystemConfig;
import com.ztbdz.user.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoMapper fileMapper;
    @Value("${upload.file.url}")
    private String uploadFileUrl;


    @Override
    public Result upload(MultipartFile multipartFile) {
        try{
            if(multipartFile==null)  return Result.fail("未接收到文件！");
            String fileId = this.upload(multipartFile,0);
            if(StringUtils.isEmpty(fileId)) return Result.fail("上传失败！");
            return Result.ok("上传成功！",fileId);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("上传文件异常，原因："+e.getMessage());
        }
    }

    @Override
    public String upload(MultipartFile multipartFile,Integer classify) throws Exception{
        if(multipartFile.getSize()>0) {
            FileInfo fileInfo = new FileInfo(multipartFile.getOriginalFilename(),multipartFile.getContentType(),String.valueOf(multipartFile.getSize()));
            this.insert(fileInfo);
            //随机生成文件名加原文件后缀
            String pictureName= fileInfo.getId().toString();
            //上传文件
            File uploadFile = new File(SystemConfig.getPath(uploadFileUrl,classify));
            if(!uploadFile.exists()) uploadFile.mkdirs();
            multipartFile.transferTo(new File(uploadFile,pictureName));
            return pictureName;
        }
        return "";
    }

    @Override
    public Integer insert(FileInfo file) throws Exception {
        return fileMapper.insert(file);
    }
}
