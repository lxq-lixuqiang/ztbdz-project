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
@ApiModel("类别")
public class Category extends Model<Tenderee> implements Serializable{

    @Getter
    private static final long serialVersionUID = -2964770934932383907L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "类别名称")
    private String categoryName;
    @ApiModelProperty(value = "类别序号")
    private Integer categorySort;
    @ApiModelProperty(value = "类别分类")
    private String categoryClassify;
    @ApiModelProperty(value = "类别代码")
    private String categoryCode;
    @ApiModelProperty(value = "类别路径")
    private String categoryPath;
    @ApiModelProperty(value = "父Id")
    private Long parentId;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否使用（0=未使用 1=使用）")
    private Integer isUse;
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
