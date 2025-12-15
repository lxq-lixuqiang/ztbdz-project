package com.ztbdz.tenderee.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ztbdz.file.pojo.FileInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel("招标公告")
public class TendereeInform extends Model<TendereeInform> implements Serializable {
    @Getter
    private static final long serialVersionUID = 2262965491235147490L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("公告内容")
    private String content;
    @ApiModelProperty("公告类型")
    private String tendereeInformType;
    @ApiModelProperty("补充内容")
    private String supplementaryContent;
    @ApiModelProperty("关联项目编号")
    @TableField(exist = false)
    private String correlationProjectCode;
    @ApiModelProperty("招标id")
    private Long tendereeId;
    @ApiModelProperty("关联id")
    private Long associationId;
    @ApiModelProperty("项目id")
    private Long projectId;
    @ApiModelProperty("文件id")
    private String tendereeInformField;
    @ApiModelProperty("回复内容")
    private String replyContent;
    @ApiModelProperty("回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date replyDate;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
    @ApiModelProperty("类型（0=公告 1=澄清信息 2=其他）")
    private Integer type;
    @ApiModelProperty("是否公布（0=未公布 1=公布）")
    private Integer isPublic;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
