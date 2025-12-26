package com.ztbdz.user.pojo;

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
import java.util.List;

@Data
@ApiModel("企业")
public class Account extends Model<Account> implements Serializable {

    @Getter
    private static final long serialVersionUID = 8478301967710403101L;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("企业名称")
    private String accountName;
    @ApiModelProperty("企业类型（0=供应商 1=代理机构 2=采购人 3=专家）")
    private Integer accountType;
    @ApiModelProperty("法人名称")
    private String accountUser;
    @ApiModelProperty("法人身份证号")
    private String accountUserId;
    @ApiModelProperty("注册资金")
    private String accountMoney;
    @ApiModelProperty("企业开户行账号")
    private Long accountNumber;
    @ApiModelProperty("企业开户行名称")
    private String accountNumberName;
    @ApiModelProperty("企业所在地")
    private String accountAddress;
    @ApiModelProperty("联系人（法人或授权代表）")
    private String member;
    @ApiModelProperty("联系电话")
    private String phone;
    @ApiModelProperty("公司邮箱")
    private String email;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("企业成立时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accountCreateDate;
    @ApiModelProperty("统一社会信用代码")
    private String accountCode;
    @ApiModelProperty("交易主体类型")
    private String dealType;
    @ApiModelProperty("经营范围")
    private String natureBusiness;
    @ApiModelProperty("备注说明")
    private String remark;
    @ApiModelProperty("统一社会信用代码证书扫描件文件id")
    private String accountCodeFileId;
    @ApiModelProperty("统一社会信用代码证书扫描件文件集合")
    @TableField(exist = false)
    private List<FileInfo> accountCodeFileIds;
    @ApiModelProperty("投标方信息")
    @TableField(exist = false)
    private BidderInfo bidderInfo;

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
