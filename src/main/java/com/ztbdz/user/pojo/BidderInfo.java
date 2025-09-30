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
    @ApiModelProperty("是否审核通过（0=等待审核 1=审核通过 1=未通过）")
    private Integer isCheck;
    @ApiModelProperty("审核不通过原因")
    private String notCheckShow;
    @ApiModelProperty("资质证明文件01")
    private String qualificationCertificate01;
    @ApiModelProperty("资质证明文件02")
    private String qualificationCertificate02;
    @ApiModelProperty("资质证明文件03")
    private String qualificationCertificate03;
    @ApiModelProperty("资质证明文件04")
    private String qualificationCertificate04;
    @ApiModelProperty("资质证明文件05")
    private String qualificationCertificate05;


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
