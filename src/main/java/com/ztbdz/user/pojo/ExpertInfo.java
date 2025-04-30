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
@ApiModel("专家信息")
public class ExpertInfo extends Model<ExpertInfo> implements Serializable {
    @Getter
    private static final long serialVersionUID = -1818102567495388210L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "人员id")
    private Long memberId;
    @ApiModelProperty(value = "人员信息")
    @TableField(exist = false)
    private Member member;
    @ApiModelProperty(value = "专家编号")
    private String expertCode;
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "专家证号")
    private String expertCard;
    @ApiModelProperty(value = "工作状态")
    private String workState;
    @ApiModelProperty(value = "最高学历")
    private String educational;
    @ApiModelProperty(value = "民族")
    private String nation;
    @ApiModelProperty(value = "所在区域")
    private String region;
    @ApiModelProperty(value = "政治面貌")
    private String politicalStatus;
    @ApiModelProperty(value = "专家状态")
    private String expertState;
    @ApiModelProperty(value = "专家类型")
    private String expertType;
    @ApiModelProperty(value = "是否审核通过（0=等待审核 1=审核通过 1=未通过）")
    private Integer isCheck;
    @ApiModelProperty(value = "审核不通过原因")
    private String notCheckShow;
    @ApiModelProperty(value = "紧急联系人电话")
    private String emergencyPhone;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "身份证正面文件id")
    private Long idCardFront;
    @ApiModelProperty(value = "身份证反面文件id")
    private Long idCardBack;
    @ApiModelProperty(value = "专家证书文件id")
    private Long certificate;
    @ApiModelProperty(value = "年检证明文件id")
    private Long annualCertificate;
    @ApiModelProperty(value = "专业领域文件id")
    private Long professionalField;
    @ApiModelProperty(value = "评标经验文件id")
    private Long bidEvaluationExperience;
    @ApiModelProperty(value = "参加工作日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;


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
