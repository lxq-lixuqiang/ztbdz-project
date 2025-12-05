package com.ztbdz.user.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztbdz.file.pojo.FileInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("投标方信息")
public class BidderInfo extends Model<BidderInfo> implements Serializable {
    @Getter
    private static final long serialVersionUID = 3784542354294997198L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("人员id")
    private Long memberId;
    @ApiModelProperty("人员信息")
    @TableField(exist = false)
    private Member member;
    @ApiModelProperty("投标方编号")
    private String bidderCode;
    @ApiModelProperty("是否审核通过（0=审核中 1=审核通过 2=不通过）")
    private Integer isCheck;
    @ApiModelProperty("审核不通过原因")
    private String notCheckShow;
    @ApiModelProperty("人员身份（0=法人 1=授权代表 -1=停用法人 -2=停用授权代表）")
    private String memberType;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("身份证正反面")
    private String qualificationCertificate01;
    @ApiModelProperty("法人授权函")
    private String qualificationCertificate02;
    @ApiModelProperty("授权代表在公司最近至少一个月社保")
    private String qualificationCertificate03;
    @ApiModelProperty("资质证明文件04")
    private String qualificationCertificate04;
    @ApiModelProperty("资质证明文件05")
    private String qualificationCertificate05;
    @ApiModelProperty(value = "审核时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date checkDate;
    @ApiModelProperty(value = "提交审核时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date submitDate;


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
