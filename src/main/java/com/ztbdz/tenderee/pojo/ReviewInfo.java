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
@ApiModel("评审")
public class ReviewInfo extends Model<ReviewInfo> implements Serializable {

    @Getter
    private static final long serialVersionUID = 100867934119363498L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "项目")
    @TableField(value = "project_id",el = "project.id")
    private Project project;
    @ApiModelProperty(value = "抽取人数")
    private Integer number;
    @ApiModelProperty(value = "抽取状态（0=未开始 1=已结束）")
    private Integer state;
    @ApiModelProperty(value = "评审时长")
    private Integer duration;
    @ApiModelProperty(value = "评审日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reviewDate;
    @ApiModelProperty(value = "评审地址")
    private String address;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "屏蔽专家名称")
    private String hideExpert;
    @ApiModelProperty(value = "屏蔽工作单位")
    private String hideAccount;
    @ApiModelProperty(value = "专业要求")
    @TableField(exist = false)
    private List<Speciality> speciality;
    @ApiModelProperty(value = "分配评审专家")
    @TableField(exist = false)
    private List<Long> expertIds;

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
