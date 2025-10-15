package com.ztbdz.tenderee.pojo;

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
import java.util.List;

@Data
@ApiModel("项目信息")
public class Project extends Model<Project> implements Serializable {
    @Getter
    private static final long serialVersionUID = -5549757568868991272L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("项目编号")
    private String projectCode;
    @ApiModelProperty("行业分类")
    private String projectClassify;
    @ApiModelProperty("项目分类")
    private Long categoryId;
    @ApiModelProperty("采购方式")
    private String procurementMethod;
    @ApiModelProperty("联合体投标（0=支持 1=不支持）")
    private Integer consortiumBidding;
    @ApiModelProperty("开标方式（0=电子标 1=纸质标）")
    private Integer bidOpeningMethod;
    @ApiModelProperty("是否推送招标网（0=是 1=否）")
    private Integer isPushBiddingWebsite;
    @ApiModelProperty("中标价格类型（0=价格 1=其他）")
    private Integer bidWinningPriceType;
    @ApiModelProperty("是否划分标段（0=是 1=否）")
    private Integer isDivisionSection;
    @ApiModelProperty("审核状态（0=未审核 1=通过 2=不通过）")
    private Integer isAudit;
    @ApiModelProperty("不通过原因")
    private String notPassInfo;
    @ApiModelProperty("项目资格条件")
    private String projectQualificationConditions;
    @ApiModelProperty("项目符合性条件")
    private String projectComplianceConditions;
    @ApiModelProperty("项目打分要求")
    private String projectScoringRequirements;
    @ApiModelProperty("项目概况")
    private String projectOverview;
    @ApiModelProperty("上传采购文件")
    private String procurementDocuments;
    @ApiModelProperty("报名涵文件")
    private String descriptionFile;
    @ApiModelProperty("预算")
    private Double money;
    @ApiModelProperty("预算金额大写")
    @TableField(exist = false)
    private String moneyUppercase;
    @ApiModelProperty("标段")
    @TableField(exist = false)
    private List<Tender> tenders;
    @ApiModelProperty("评标标准")
    @TableField(exist = false)
    private List<EvaluationCriteria> evaluationCriterias;
    @ApiModelProperty("投标报名")
    @TableField(exist = false)
    private ProjectRegister projectRegisters;
    @ApiModelProperty("创建人")
    private Long memberId;
    @ApiModelProperty("招标信息")
    @TableField(exist = false)
    private Tenderee tenderee;
    @ApiModelProperty("报名费")
    private Long registrationFee;
    @ApiModelProperty("项目专家组长")
    @TableField(exist = false)
    private String projectExpertLeader;

    @ApiModelProperty("投标报名开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date senrollStartDate;
    @ApiModelProperty("投标报名截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date enrollEndDate;
    @TableField(exist = false)
    private String enrollEndDateSearch;
    @ApiModelProperty("答疑截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date answerEndDate;
    @ApiModelProperty("评审结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date reviewEndDate;

    @ApiModelProperty("项目状态（1=可以报名项目 2=正在进行项目 3=评审结束项目）")
    private Integer state;
    @ApiModelProperty("开标时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date bidOpeningTime;
    @ApiModelProperty("评审专家人数")
    private Integer numberReviewExpert;
    @ApiModelProperty("评审进度")
    private Integer reviewProgress;
    @ApiModelProperty("评审是否通过（0=通过 1=废标）")
    private Integer isPass;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
