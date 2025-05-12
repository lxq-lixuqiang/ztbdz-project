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
import java.util.List;

@Data
@ApiModel("招标信息")
public class Tenderee extends Model<Tenderee> implements Serializable {

    @Getter
    private static final long serialVersionUID = 6199551224611535701L;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "招标方名称")
    private String tendereeName;
    @ApiModelProperty(value = "招标方代码类型")
    private String tendereeCodeType;
    @ApiModelProperty(value = "招标方代码")
    private String tendereeCode;
    @ApiModelProperty(value = "所在行政区域")
    private String administrationPlace;
    @ApiModelProperty(value = "单位性质")
    private String accountQuality;
    @ApiModelProperty(value = "信息申报责任人联系电话")
    private String responsibilityPhone;
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    @ApiModelProperty(value = "通讯地址")
    private String contactPlace;
    @ApiModelProperty(value = "招标范围")
    private String tendereeRange;
    @ApiModelProperty(value = "项目")
    @TableField(value = "project_id",el = "project.id")
    private Project project;
    @ApiModelProperty(value = "招标公告")
    @TableField(exist = false)
    private List<TendereeInform> tendereeInforms;
    @ApiModelProperty(value = "招标时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date tendereeDate;

    @ApiModelProperty("创建人")
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
