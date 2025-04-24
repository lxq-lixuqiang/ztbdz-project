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
    @ApiModelProperty(value = "项目id")
    private Long id;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "项目编号")
    private String projectCode;
    @ApiModelProperty(value = "行业分类")
    private String projectClassify;
    @ApiModelProperty(value = "项目分类")
    @TableField(value = "category_id",el = "category.id")
    private Category category;
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
    @ApiModelProperty(value = "标段")
    @TableField(exist = false)
    private List<Tender> tenders;
    @ApiModelProperty(value = "评标标准")
    @TableField(exist = false)
    private List<EvaluationCriteria> evaluationCriterias;

    @ApiModelProperty(value = "项目状态（0=进行中的项目 1=已完成的项目 2=待重新招标的项目）")
    private Integer state;
    @ApiModelProperty(value = "开标时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date bidOpeningTime;
    @ApiModelProperty(value = "评审专家人数")
    private Integer numberReviewExpert;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
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
