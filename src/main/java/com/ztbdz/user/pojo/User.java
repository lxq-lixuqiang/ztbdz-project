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
@ApiModel("用户信息")
public class User extends Model<User> implements Serializable {

    @Getter
    private static final long serialVersionUID = 6159714078966572284L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否删除（0=未删除 1=已删除）",hidden = true)
    private Integer isDelete;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否停用（0=启用 1=停用）",hidden = true)
    private Integer isStop;
    @ApiModelProperty("人员")
    @TableField(value = "member_id",el = "member.id")
    private Member member;
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
