package com.ztbdz.tenderee.pojo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztbdz.user.pojo.ExpertInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("评审")
public class ReviewInfo extends Model<ReviewInfo> implements Serializable {

    @Getter
    private static final long serialVersionUID = 100867934119363498L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("项目")
    @TableField(value = "project_id",el = "project.id")
    private Project project;
    @ApiModelProperty("抽取数量")
    private Integer number;
    @ApiModelProperty("备选数量")
    private Integer spareNumber;
    @ApiModelProperty("抽取状态（0=未开始 1=已结束）")
    private Integer state;
    @ApiModelProperty("评审时长")
    private Integer duration;
    @ApiModelProperty("评审日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewDate;
    @ApiModelProperty("评审地址")
    private String address;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("屏蔽专家名称")
    private String hideExpert;
    @ApiModelProperty("屏蔽工作单位")
    private String hideAccount;
    @ApiModelProperty("专业要求")
    @TableField(exist = false)
    private List<Speciality> speciality;
    @ApiModelProperty("专家名单")
    private String selectExpert;
    @ApiModelProperty("备选专家名单")
    private String spareExpert;
    @ApiModelProperty("项目专家组长")
    private Long expertLeader;
    @ApiModelProperty("分配评审专家")
    @TableField(exist = false)
    private List<Long> expertIds;
    @ApiModelProperty("专家名单")
    @TableField(exist = false)
    private List<ExpertInfo> selectExpertList;

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
