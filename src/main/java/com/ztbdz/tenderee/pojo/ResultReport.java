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
@ApiModel("评审结果报告")
public class ResultReport extends Model<EvaluationCriteria> implements Serializable {
    @Getter
    private static final long serialVersionUID = 7588680521620216025L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "类型（0=废标报告 1=评审报告）")
    private Integer state;
    @ApiModelProperty(value = "说明")
    private String describe;
    @ApiModelProperty(value = "废标报告")
    private String resultReport;
    @ApiModelProperty(value = "废标报告文件id")
    private String resultReportId;
    @ApiModelProperty(value = "废标项目")
    @TableField(value = "project_id",el = "project.id")
    private Project project;
    @ApiModelProperty("废标人员")
    @TableField(value = "member_id",el = "member.id")
    private Member member;
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
