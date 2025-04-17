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
@ApiModel("人员信息")
public class Member extends Model<Member> implements Serializable {

    @Getter
    private static final long serialVersionUID = 1334289778100623211L;
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("性别：0-男 1-女")
    private Integer sex;
    @ApiModelProperty("出生日期")
    private Date birthday;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("现住地址")
    private String address;
    @ApiModelProperty("是否删除：0-未删除 1-已删除")
    private Integer isDelete;
    @ApiModelProperty("是否停用：0-启用 1-停用")
    private Integer isStop;
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
        this.setIsDelete(0);
        this.setIsStop(0);
    }

    public void update(){
        this.setUpdateDate(new Date());
    }
}
