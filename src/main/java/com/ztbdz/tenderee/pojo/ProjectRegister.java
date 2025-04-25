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
import org.apache.ibatis.mapping.FetchType;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("项目报名")
public class ProjectRegister extends Model<ProjectRegister> implements Serializable {

    @Getter
    private static final long serialVersionUID = 7975316725026196398L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "项目报名id")
    private Long id;
    @ApiModelProperty(value = "项目id")
    @TableField(value = "project_id",el = "project.id")
    private Project project;
    @ApiModelProperty(value = "投标人id")
    @TableField(value = "member_id",el = "member.id")
    private Member member;
    @ApiModelProperty(value = "报名状态（0=正在报名 1=报名结束）")
    private Integer state;
    @ApiModelProperty(value = "报名数量")
    @TableField(exist = false)
    private Integer num;
    @ApiModelProperty(value = "保证金金额")
    private double earnestMoney;
    @ApiModelProperty(value = "保证金状态（0=不收取 1=收取）")
    private double earnestMoneyState;
    @ApiModelProperty(value = "保证金到账时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date earnestMoneyAccountDate;
    @ApiModelProperty(value = "保证金退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date earnestMoneyRefundDate;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
