package com.ztbdz.file.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztbdz.web.config.SystemConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@ApiModel("文件")
public class FileInfo extends Model<FileInfo> implements Serializable {

    @Getter
    private static final long serialVersionUID = -7553919406996194012L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "文件id")
    private Long id;
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件类型")
    private String type;
    @ApiModelProperty(value = "文件大小")
    private Long size;
    @ApiModelProperty("显示文件大小")
    @TableField(exist = false)
    private String fileSize;
    @ApiModelProperty(value = "文件分类（0=file 1=document 3=img）")
    private Integer classify;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    public FileInfo(){}

    public FileInfo(String name,String type,Long size){
        this.name = name;
        this.type = type;
        this.size = size;
        this.classify = 0;

    }

    /**
     * 获取文件的地址
     * @return
     */
    public String getPath(){
        String uploadFileUrl = SystemConfig.UPLOAD_FILE_URL;
        if(StringUtils.isEmpty(uploadFileUrl)) uploadFileUrl = System.getProperty("user.dir")+File.separator+"uploads";
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(this.createDate);
        String[] nowDates = nowDate.split("-");
        return uploadFileUrl+File.separator+nowDates[0]+ File.separator+nowDates[1]+ File.separator+nowDates[2]+ File.separator+this.classify;
    }

    /**
     * 获取文件的完整地址
     * @return
     */
    public String getUrl(){
        return getPath()+ File.separator+this.id;
    }

    public void convertSize(){
        if(this.size<1024){
            this.fileSize = this.size+"B";
        }else if(this.size<1024 * 1024){
            this.fileSize = Math.round(this.size / 1024)+"KB";
        }else if(this.size<1024 * 1024 * 1024){
            this.fileSize = Math.round(this.size / (1024 * 1024))+"MB";
        }else if(this.size<1024 * 1024 * 1024 * 1024){
            this.fileSize = Math.round(this.size / (1024 * 1024 * 1024))+"GB";
        }
    }
}
