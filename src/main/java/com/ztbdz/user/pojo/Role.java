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
import java.util.List;

@Data
@ApiModel("角色")
public class Role extends Model<Role> implements Serializable {
    @Getter
    private static final long serialVersionUID = 86617709176545036L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("默认角色类型（admin=管理员 tenderee=招标方 bidder=投标方 expert=专家）")
    private String type;
    @ApiModelProperty("角色名称")
    private String typeName;
    @ApiModelProperty("菜单权限")
    @TableField(exist = false)
    private List<MenuAuthorize> meunAuthorize;
    @ApiModelProperty("人员")
    @TableField(exist = false)
    private List<Member> member;
    @ApiModelProperty("描述")
    private String describeInfo;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("是否预设（0=是 1=否）")
    private Integer isDefault;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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

    public Role(){}

    public Role(String type,String typeName,String describeInfo,Integer isDefault){
        this.type = type;
        this.typeName = typeName;
        this.describeInfo = describeInfo;
        this.isDefault = isDefault;
    }

}
