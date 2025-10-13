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

@Data
@ApiModel("评标标准")
public class EvaluationCriteria  extends Model<EvaluationCriteria> implements Serializable {
    @Getter
    private static final long serialVersionUID = 2471516643524243464L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("序号")
    private Integer sort;
    @ApiModelProperty("评审项名称")
    private String evaluationCriteriaName;
    @ApiModelProperty("评审标准")
    private String evaluationCriteriaInfo;
    @ApiModelProperty("评分方式")
    private String evaluationWay;
    @ApiModelProperty("分数设置")
    private String scoreSetting;
    @ApiModelProperty("分数")
    private Integer score;
    @ApiModelProperty("是否主观项（0=主观项 1=客观项）")
    private Integer isSubjective;
    @ApiModelProperty("评标标准类型")
    private String evaluationCriteriaType;
    @ApiModelProperty("项目id")
    private Long projectId;
    @ApiModelProperty("投标报名id")
    private Long projectRegisterId;
    @ApiModelProperty("评审人员id")
    @TableField(value = "member_id",el = "member.id")
    private Member member;
    @ApiModelProperty("评审类型（0=其他 1=资格评审标准 2=实质性符合标准 3=评分标准）")
    private Integer reviewType;
    @ApiModelProperty("是否通过（0=通过 1=不通过）")
    private Integer isPass;
    @ApiModelProperty("评审说明")
    private String reviewDescription;
    @ApiModelProperty("扣分理由")
    private String deductionReason;
    @ApiModelProperty("投标报名信息")
    @TableField(exist = false)
    private ProjectRegister projectRegister;

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
