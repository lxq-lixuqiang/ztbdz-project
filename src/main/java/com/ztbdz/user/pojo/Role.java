package com.ztbdz.user.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
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
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("角色类型（0=管理员 1=招标方 2=投标方 3=评标专家）")
    private Integer type;
    @ApiModelProperty("角色名称")
    private String typeName;
    @ApiModelProperty("菜单")
    private String menu;

    @ApiModelProperty("创建日期")
    private Date createDate;
    @ApiModelProperty("更新日期")
    private Date updateDate;
    @ApiModelProperty("扩展字段01")
    private Integer ext1;
    @ApiModelProperty("扩展字段02")
    private Integer ext2;
    @ApiModelProperty("扩展字段03")
    private String ext3;
    @ApiModelProperty("扩展字段04")
    private String ext4;
    @ApiModelProperty("扩展字段05")
    private String ext5;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    public void init(){
        Date nowDate = new Date();
        this.setId(UUID.randomUUID().getMostSignificantBits());
        this.setCreateDate(nowDate);
        this.setUpdateDate(nowDate);
    }

    public void update(){
        this.setUpdateDate(new Date());
    }
}
