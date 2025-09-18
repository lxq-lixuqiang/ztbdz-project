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
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "项目编号")
    private String projectCode;
    @ApiModelProperty(value = "行业分类")
    private String projectClassify;
    @ApiModelProperty(value = "项目分类")
    private Long categoryId;
    @ApiModelProperty(value = "采购方式")
    private String procurementMethod;
    @ApiModelProperty(value = "联合体投标（0=支持 1=不支持）")
    private Integer consortiumBidding;
    @ApiModelProperty(value = "开标方式（0=电子标 1=纸质标）")
    private Integer bidOpeningMethod;
    @ApiModelProperty(value = "是否推送招标网（0=是 1=否）")
    private Integer isPushBiddingWebsite;
    @ApiModelProperty(value = "中标价格类型（0=价格 1=其他）")
    private Integer bidWinningPriceType;
    @ApiModelProperty(value = "是否划分标段（0=是 1=否）")
    private Integer isDivisionSection;
    @ApiModelProperty(value = "审核状态（0=未审核 1=通过 2=不通过）")
    private Integer isAudit;
    @ApiModelProperty(value = "不通过原因")
    private String notPassInfo;
    @ApiModelProperty(value = "项目资格条件")
    private String projectQualificationConditions;
    @ApiModelProperty(value = "项目符合性条件")
    private String projectComplianceConditions;
    @ApiModelProperty(value = "项目打分要求")
    private String projectScoringRequirements;
    @ApiModelProperty(value = "项目概况")
    private String projectOverview;
    @ApiModelProperty(value = "上传采购文件")
    private String procurementDocuments;
    @ApiModelProperty(value = "报名涵文件")
    private String descriptionFile;
    @ApiModelProperty(value = "预算")
    private Double money;
    @ApiModelProperty(value = "预算金额大写")
    @TableField(exist = false)
    private String moneyUppercase;
    @ApiModelProperty(value = "标段")
    @TableField(exist = false)
    private List<Tender> tenders;
    @ApiModelProperty(value = "评标标准")
    @TableField(exist = false)
    private List<EvaluationCriteria> evaluationCriterias;
    @ApiModelProperty(value = "投标报名")
    @TableField(exist = false)
    private ProjectRegister projectRegisters;
    @ApiModelProperty("创建人")
    private Long memberId;
    @ApiModelProperty(value = "招标信息")
    @TableField(exist = false)
    private Tenderee tenderee;
    @ApiModelProperty("报名费")
    private Long registrationFee;
    @ApiModelProperty(value = "项目专家组长")
    @TableField(exist = false)
    private String projectExpertLeader;

    @ApiModelProperty(value = "投标报名开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date senrollStartDate;
    @ApiModelProperty(value = "投标报名截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date enrollEndDate;
    @TableField(exist = false)
    private String enrollEndDateSearch;
    @ApiModelProperty(value = "答疑截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date answerEndDate;
    @ApiModelProperty(value = "评审结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date reviewEndDate;

    @ApiModelProperty(value = "项目状态（1=可以报名项目 2=正在进行项目 3=评审结束项目）")
    private Integer state;
    @ApiModelProperty(value = "开标时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date bidOpeningTime;
    @ApiModelProperty(value = "评审专家人数")
    private Integer numberReviewExpert;
    @ApiModelProperty(value = "评审进度")
    private Integer reviewProgress;
    @ApiModelProperty(value = "评审是否通过（0=通过 1=废标）")
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
