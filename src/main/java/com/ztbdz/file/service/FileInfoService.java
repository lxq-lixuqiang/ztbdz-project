package com.ztbdz.file.service;

import com.ztbdz.file.pojo.FileInfo;
import com.ztbdz.web.util.Result;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface FileInfoService {

    /**
     * 上传文件
     * @param multipartFile
     * @return
     */
    Result upload(MultipartFile multipartFile,Integer classify);

    /**
     * 上传文件
     * @param file
     * @param classify
     * @return
     */
    String uploadFile(MultipartFile file,Integer classify) throws Exception;

    /**
     * 保存文件
     * @param fileInfo
     * @return
     */
    Integer insert(FileInfo fileInfo) throws Exception;


    /**
     * 下载文件
     * @param id
     * @return
     */
    ResponseEntity<byte[]> download(Long id);

    /**
     * 查询文件信息
     * @param id
     * @return
     */
    FileInfo getById(Long id) throws Exception;

    /**
     * 预览文件
     * @param id
     * @return
     */
    ResponseEntity<Object> preview(Long id);

    /**
     * 查询文件
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * pdf盖章
     * @param pdfFile
     * @param stampImage
     * @return
     */
    Result stampPdf( MultipartFile pdfFile,MultipartFile stampImage,Float x_axis,Float y_axis);

}
