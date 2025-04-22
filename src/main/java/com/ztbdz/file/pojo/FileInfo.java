package com.ztbdz.file.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
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
    private String size;
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

    public FileInfo(String name,String type,String size){
        this.name = name;
        this.type = type;
        this.size = size;
        this.classify = 0;

    }
}
