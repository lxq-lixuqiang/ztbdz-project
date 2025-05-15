package com.ztbdz.tenderee.pojo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "项目id")
    @TableField(value = "project_id",el = "project.id")
    private Project project;
    @ApiModelProperty(value = "投标人id")
    @TableField(value = "member_id",el = "member.id")
    private Member member;
    @ApiModelProperty(value = "审核状态（0=未审核 1=通过 2=不通过）")
    private Integer state;
    @ApiModelProperty(value = "报名数量")
    private Integer num;
    @ApiModelProperty(value = "保证金金额")
    private double earnestMoney;
    @ApiModelProperty(value = "保证金状态（0=不收取 1=收取）")
    private Integer earnestMoneyState;
    @ApiModelProperty(value = "总分数")
    private Integer score;
    @ApiModelProperty(value = "标书")
    private String bidDocumentId;
    @ApiModelProperty(value = "中标金额")
    private Double bidMoney;
    @ApiModelProperty(value = "合同盖章文件")
    private String contractImprint;
    @ApiModelProperty(value = "评标报告文件")
    private String bidEvaluationReport;
    @ApiModelProperty(value = "报名费")
    private Long paymentMoney;
    @ApiModelProperty(value = "付款凭证")
    private String paymentVoucher;
    @ApiModelProperty(value = "中标情况（0=未公布 1=已中标 2=未中标）")
    private Integer winBidState;
    @ApiModelProperty(value = "保证金到账时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date earnestMoneyAccountDate;
    @ApiModelProperty(value = "保证金退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date earnestMoneyRefundDate;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;

    @ApiModelProperty("评标标准")
    @TableField(exist = false)
    private List<EvaluationCriteria> evaluationCriterias;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
