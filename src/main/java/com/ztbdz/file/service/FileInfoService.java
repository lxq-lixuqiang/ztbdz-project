package com.ztbdz.file.service;

import com.ztbdz.file.pojo.FileInfo;
import com.ztbdz.user.web.util.Result;
import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService {

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    Result upload(MultipartFile multipartFile);

    /**
     * 上传文件
     * @param file
     * @param classify
     * @return
     */
    String upload(MultipartFile file,Integer classify) throws Exception;

    /**
     * 保存文件
     * @param fileInfo
     * @return
     */
    Integer insert(FileInfo fileInfo) throws Exception;

}
