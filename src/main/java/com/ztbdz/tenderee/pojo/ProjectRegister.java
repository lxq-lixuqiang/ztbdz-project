package com.ztbdz.tenderee.pojo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztbdz.file.pojo.FileInfo;
import com.ztbdz.user.pojo.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("投标报名")
public class ProjectRegister extends Model<ProjectRegister> implements Serializable {

    @Getter
    private static final long serialVersionUID = 7975316725026196398L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("项目id")
    @TableField(value = "project_id",el = "project.id")
    private Project project;
    @ApiModelProperty("投标人id")
    @TableField(value = "member_id",el = "member.id")
    private Member member;
    @ApiModelProperty("审核状态（0=审核中 1=通过 2=不通过 3=资质审核中 4=资质通过 5=资质不通过）")
    private Integer state;
    @ApiModelProperty("不通过原因")
    private String notPassInfo;
    @ApiModelProperty("报名数量")
    private Integer num;
    @ApiModelProperty("保证金金额")
    private double earnestMoney;
    @ApiModelProperty("保证金状态（0=不收取 1=收取）")
    private Integer earnestMoneyState;
    @ApiModelProperty("总分数")
    private Integer score;
    @ApiModelProperty("标书")
    private String bidDocumentId;
    @ApiModelProperty("标书集合")
    @TableField(exist = false)
    private List<FileInfo> bidDocumentIds;
    @ApiModelProperty("中标金额")
    private String bidMoney;
    @ApiModelProperty("中标金额大写")
    @TableField(exist = false)
    private String bidMoneyUppercase;
    @ApiModelProperty("合同盖章文件")
    private String contractImprint;
    @ApiModelProperty("评标报告文件")
    private String bidEvaluationReport;
    @ApiModelProperty("报名费")
    private Long paymentMoney;
    @ApiModelProperty("付款凭证")
    private String paymentVoucher;
    @ApiModelProperty("中标情况（0=未公布 1=已中标 2=未中标）")
    private Integer winBidState;
    @ApiModelProperty("发票状态（0=待处理 1=已开票）")
    private Integer isInvoice;
    @ApiModelProperty("发票类型")
    private String invoiceType;
    @ApiModelProperty("保证金到账时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date earnestMoneyAccountDate;
    @ApiModelProperty("保证金退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date earnestMoneyRefundDate;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateDate;

    @ApiModelProperty("评标标准")
    @TableField(exist = false)
    private List<EvaluationCriteria> evaluationCriterias;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
