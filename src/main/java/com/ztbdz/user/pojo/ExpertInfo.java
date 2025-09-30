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
@ApiModel("专家信息")
public class ExpertInfo extends Model<ExpertInfo> implements Serializable {
    @Getter
    private static final long serialVersionUID = -1818102567495388210L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("人员id")
    private Long memberId;
    @ApiModelProperty("人员信息")
    @TableField(exist = false)
    private Member member;
    @ApiModelProperty("专家编号")
    private String expertCode;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("专家证号")
    private String expertCard;
    @ApiModelProperty("工作状态")
    private String workState;
    @ApiModelProperty("最高学历")
    private String educational;
    @ApiModelProperty("职称")
    private String position;
    @ApiModelProperty("民族")
    private String nation;
    @ApiModelProperty("所在区域")
    private String region;
    @ApiModelProperty("政治面貌")
    private String politicalStatus;
    @ApiModelProperty("专家状态")
    private String expertState;
    @ApiModelProperty("专家类型")
    private String expertType;
    @ApiModelProperty("是否审核通过（0=等待审核 1=审核通过 2=未通过）")
    private Integer isCheck;
    @ApiModelProperty("审核不通过原因")
    private String notCheckShow;
    @ApiModelProperty("紧急联系人电话")
    private String emergencyPhone;
    @ApiModelProperty("备注")
    private String description;
    @ApiModelProperty("是否组长（0=不是 1=是）")
    private Integer isAdmin;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("银行卡号")
    private String bankCard;
    @ApiModelProperty("身份证正面文件id")
    private Long idCardFront;
    @ApiModelProperty("身份证反面文件id")
    private Long idCardBack;
    @ApiModelProperty("专家证书文件id")
    private Long certificate;
    @ApiModelProperty("年检证明文件id")
    private Long annualCertificate;
    @ApiModelProperty("专业领域文件id")
    private Long professionalField;
    @ApiModelProperty("评标经验文件id")
    private Long bidEvaluationExperience;
    @ApiModelProperty("参加工作日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;


    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value ="创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value ="更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateDate;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
