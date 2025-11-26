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

@Data
@ApiModel("黑名单")
public class Blacklist extends Model<Blacklist> implements Serializable{

    @Getter
    private static final long serialVersionUID = -1602444221091150470L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("代理id")
    private Long memberId;
    @ApiModelProperty("业主名称")
    private String landlordName;
    @ApiModelProperty("投标方名称")
    private String bidderName;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否启用（0=启用 1=停用）")
    private int isEnable;
    @ApiModelProperty("类型（0=投标方黑名单 1=其他黑名单）")
    private int type;
    @ApiModelProperty("截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endDate;

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
