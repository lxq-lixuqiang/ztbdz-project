package com.ztbdz.user.pojo;


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
@ApiModel("菜单权限")
public class MenuAuthorize extends Model<MenuAuthorize> implements Serializable {
    @Getter
    private static final long serialVersionUID = -7472846567734813500L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("菜单权限id")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识")
    private String sign;
    @ApiModelProperty("路径")
    private String path;
    @ApiModelProperty("地址")
    private String url;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date updateDate;
    @ApiModelProperty(value = "扩展字段01",hidden = true)
    private Integer ext1;
    @ApiModelProperty(value = "扩展字段02",hidden = true)
    private Integer ext2;
    @ApiModelProperty(value = "扩展字段03",hidden = true)
    private String ext3;
    @ApiModelProperty(value = "扩展字段04",hidden = true)
    private String ext4;
    @ApiModelProperty(value = "扩展字段05",hidden = true)
    private String ext5;

    @Override
    protected Serializable pkVal() {
        return id;
    }

}
