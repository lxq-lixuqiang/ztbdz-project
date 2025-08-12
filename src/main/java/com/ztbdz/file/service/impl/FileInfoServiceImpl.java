package com.ztbdz.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztbdz.file.mapper.FileInfoMapper;
import com.ztbdz.file.pojo.FileInfo;
import com.ztbdz.file.service.FileInfoService;
import com.ztbdz.web.config.SystemConfig;
import com.ztbdz.web.util.PdfStampUtil;
import com.ztbdz.web.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "fileInfo")
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoMapper fileMapper;


    @Override
    public Result upload(List<MultipartFile> fileList, Integer classify) {
        try{
            if(fileList==null || fileList.size()==0)  return Result.fail("未接收到文件！");
            StringBuilder fileSB = new StringBuilder();
            for(MultipartFile file : fileList){
                if(fileSB.length()>0) fileSB.append(",");
                fileSB.append(this.uploadFile(file,classify));
            }
            if(fileSB.length()==0) return Result.fail("上传失败！");
            return Result.ok("上传成功！",fileSB);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("上传文件异常，原因："+e.getMessage());
        }
    }

    @Override
    public String uploadFile(MultipartFile multipartFile,Integer classify) throws Exception{
        if(multipartFile.getSize()>0) {
            FileInfo fileInfo = new FileInfo(multipartFile.getOriginalFilename(),multipartFile.getContentType(),multipartFile.getSize());
            fileInfo.setClassify(classify);
            this.insert(fileInfo);
            //随机生成文件名加原文件后缀
            String pictureName= fileInfo.getId().toString();
            //上传文件
            File uploadFile = new File(SystemConfig.getPath(classify));
            if(!uploadFile.exists()) uploadFile.mkdirs();
            multipartFile.transferTo(new File(uploadFile,pictureName));
            return pictureName;
        }
        return "";
    }

    @CacheEvict(cacheNames = "fileInfo",allEntries = true)
    @Override
    public Integer insert(FileInfo file) throws Exception {
        return fileMapper.insert(file);
    }

    @Override
    public ResponseEntity<Object> download(Long id) {
        try{
            FileInfo fileInfo = this.getById(id);
            File file= new File(fileInfo.path(), fileInfo.getId().toString());
            byte[] body=FileUtils.readFileToByteArray(file);
            HttpHeaders headers=new HttpHeaders();
            // 防止下载文件名乱码
            String encodedFileName = URLEncoder.encode(fileInfo.getName(), "UTF-8").replaceAll("\\+", "%20");  // 替换空格编码
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().headers(headers).body(body);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return ResponseEntity.notFound().build();
        }
    }

    @Cacheable
    @Override
    public FileInfo getById(Long id) throws Exception {
        return fileMapper.selectById(id);
    }

    @Override
    public ResponseEntity<Object> preview(Long id) {
        try{
            FileInfo fileInfo = this.getById(id);
            String contentType = SystemConfig.determineContentType(fileInfo.getName());
            if(contentType.indexOf("不支持")>-1){
                return download(id); // 不支持预览的文件就进行下载
            }
            Resource resource = new FileSystemResource(SystemConfig.validateAndGetPath(fileInfo.path(),fileInfo.getId().toString()));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+URLEncoder.encode(fileInfo.getName(),"UTF-8"));
            return ResponseEntity.ok().headers(headers).body(resource);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public Result list(List<Long> ids) {
        try{
            List<FileInfo> fileInfoList = this.listByIds(ids);
            for(FileInfo fileInfo :fileInfoList){
                fileInfo.convertSize();
            }
            return Result.ok("查询成功！",fileInfoList);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询文件列表异常，原因："+e.getMessage());
        }
    }

    @Cacheable
    @Override
    public List<FileInfo> listByIds(List<Long> ids) throws Exception {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", ids);
        return fileMapper.selectList(queryWrapper);
    }

    @Override
    public Result get(Long id) {
        try{
            FileInfo fileInfo = this.getById(id);
            fileInfo.convertSize();
            return Result.ok("查询成功！",fileInfo);
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("查询文件异常，原因："+e.getMessage());
        }
    }

    @Override
    public Result stampPdf(MultipartFile pdfFile, MultipartFile stampImage,Float x_axis,Float y_axis) {
        try{
            if(pdfFile.getContentType().indexOf("pdf")<0) return Result.fail("不是pdf文件！");
            if(stampImage.getContentType().indexOf("image")<0) return Result.fail("不是印章图片！");

            // 1. 保存上传的PDF和图片到临时文件
            File uploadFile = new File(SystemConfig.UPLOAD_FILE_TEMP);
            File inputPdf = new File(uploadFile,"temp-"+pdfFile.getOriginalFilename());
            pdfFile.transferTo(inputPdf);

            File stampImg = new File(uploadFile,"temp-"+stampImage.getOriginalFilename());
            stampImage.transferTo(stampImg);
            FileInfo fileInfo = new FileInfo(pdfFile.getOriginalFilename(),pdfFile.getContentType(),pdfFile.getSize());
            this.insert(fileInfo);

            // 2. 生成输出文件路径
            File outputPdfFile = new File(fileInfo.path());
            if(!outputPdfFile.exists()) outputPdfFile.mkdirs();
            // 3. 调用盖章工具
            PdfStampUtil.addStampToPdf(inputPdf.getAbsolutePath(),stampImg.getAbsolutePath(),fileInfo.url(), x_axis, y_axis);

            // 删除临时文件
            if(inputPdf!=null) inputPdf.delete();
            if(stampImg!=null) stampImg.delete();

            // 4. 返回处理后的PDF
            return Result.ok("盖章成功！",fileInfo.getId());
        }catch (Exception e){
            log.error(this.getClass().getName()+" 中 "+new RuntimeException().getStackTrace()[0].getMethodName()+" 出现异常，原因："+e.getMessage(),e);
            return Result.error("生产PDF盖章文件异常，原因："+e.getMessage());
        }
    }
}
