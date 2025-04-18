package com.ztbdz.user.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@ApiModel("角色信息")
public class Role  extends Model<Role> implements Serializable {
    @Getter
    private static final long serialVersionUID = 86617709176545036L;
    @ApiModelProperty("角色id")
    private String id;
    @ApiModelProperty("角色类型（admin=管理员 tenderee=招标方 bidder=投标方 expert=专家）")
    private String type;
    @ApiModelProperty("角色名称")
    private String typeName;
    @ApiModelProperty("权限")
    private String authorize;
    @ApiModelProperty("描述")
    private String describeInfo;
    @ApiModelProperty("是否预设（0=是 1=否）")
    private Integer isDefault;

    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;
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

    public void init(){
        Date nowDate = new Date();
        this.setId(String.valueOf(UUID.randomUUID().getMostSignificantBits()));
        this.setCreateDate(nowDate);
        this.setUpdateDate(nowDate);
    }

    public void update(){
        this.setUpdateDate(new Date());
    }
}
