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
@ApiModel("人员")
public class Member extends Model<Member> implements Serializable {

    @Getter
    private static final long serialVersionUID = 1334289778100623211L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("人员id")
    private Long id;
    @ApiModelProperty("头像")
    private Long headImg;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("性别（0=男 1=女）")
    private Integer sex;
    @ApiModelProperty("出生日期")
    private Date birthday;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("现住地址")
    private String address;
    @ApiModelProperty("角色")
    @TableField(value = "role_id",el = "role.id")
    private Role role;
    @ApiModelProperty("单位")
    @TableField(value = "account_id",el = "account.id")
    private Account account;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否删除（0=未删除 1=已删除）",hidden = true)
    private Integer isDelete;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否停用（0=启用 1=停用）",hidden = true)
    private Integer isStop;
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
