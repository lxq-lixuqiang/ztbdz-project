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
    @ApiModelProperty(value = "公告id")
    private Long id;
    @ApiModelProperty(value = "公告名称")
    private String projectName;
    @ApiModelProperty(value = "招标id")
    private Long tendereeId;
    @ApiModelProperty("公告文件")
    @TableField(value = "tenderee_inform_field_id",el = "tendereeInformField.id")
    private FileInfo tendereeInformField;
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "上传时间",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;
    @ApiModelProperty(value = "类型（0=公告文件 1=磋商文件 2=澄清与答疑文件）")
    private String type;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
