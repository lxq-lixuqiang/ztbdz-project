package com.ztbdz.tenderee.pojo;


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
@ApiModel("专业要求")
public class Speciality extends Model<Speciality> implements Serializable {
    @Getter
    private static final long serialVersionUID = 3120131873835324831L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("评审信息id")
    private Long reviewInfoId;
    @ApiModelProperty("序号")
    private Integer sort;
    @ApiModelProperty("专业")
    private String specialityInfo;
    @ApiModelProperty("计划抽取人数")
    private Integer planNum;
    @ApiModelProperty("准备人数")
    private Integer num;
    @ApiModelProperty("评审区域")
    private String reviewArea;
    @ApiModelProperty("专家类型")
    private String expertType;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateDate;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
