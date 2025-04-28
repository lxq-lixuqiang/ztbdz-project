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
    @ApiModelProperty(value = "投标方信息id")
    private Long id;
    @ApiModelProperty(value = "人员id")
    private Long memberId;
    @ApiModelProperty(value = "人员信息")
    @TableField(exist = false)
    private Member member;
    @ApiModelProperty(value = "投标方编号")
    private String bidderCode;
    @ApiModelProperty(value = "资质证明文件01")
    private Long qualificationCertificate01;
    @ApiModelProperty(value = "资质证明文件02")
    private Long qualificationCertificate02;
    @ApiModelProperty(value = "资质证明文件03")
    private Long qualificationCertificate03;
    @ApiModelProperty(value = "资质证明文件04")
    private Long qualificationCertificate04;
    @ApiModelProperty(value = "资质证明文件05")
    private Long qualificationCertificate05;


    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date updateDate;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
