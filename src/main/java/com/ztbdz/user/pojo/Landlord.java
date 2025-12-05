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
@ApiModel("业主")
public class Landlord extends Model<Landlord> implements Serializable {

    @Getter
    private static final long serialVersionUID = 2676219512805137038L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("业主单位的名称")
    private String accountName;
    @ApiModelProperty("联系人")
    private String contact;

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


    // 转用户
    public User toUser(){
        User user = new User();
        user.setId(this.getId());
        Member member = new Member();
        member.setId(this.getId());
        user.setMember(member);
        user.setUsername(this.getName());
        user.setPassword(this.getPassword());
        user.setCreateDate(this.getCreateDate());
        user.setUpdateDate(this.getUpdateDate());
        user.setExt1(this.getExt1());
        user.setExt2(this.getExt2());
        user.setExt3(this.getExt3());
        user.setExt4(this.getExt4());
        user.setExt5(this.getExt5());
        return user;
    }

}
