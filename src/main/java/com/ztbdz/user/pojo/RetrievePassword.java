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
@ApiModel("找回密码")
public class RetrievePassword extends Model<RetrievePassword> implements Serializable {

    @Getter
    private static final long serialVersionUID = -1842799461569709653L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("新密码")
    private String newPassword;
    @ApiModelProperty("旧密码")
    private String oldPassword;
    @ApiModelProperty("营业执照")
    private String businessLicenseId;
    @ApiModelProperty("社保证明")
    private String socialSecurityCertificateId;
    @ApiModelProperty("是否审核通过（0=审核中 1=审核通过 2=不通过）")
    private Integer isPass;
    @ApiModelProperty("不通过原因")
    private String notPassContent;
    @ApiModelProperty("用户信息")
    @TableField(exist = false)
    private User user;
    @ApiModelProperty("供应商信息")
    @TableField(exist = false)
    private BidderInfo bidderInfo;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
