package com.ztbdz.user.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@ApiModel("企业信息")
public class Account extends Model<Account> implements Serializable {

    @Getter
    private static final long serialVersionUID = 8478301967710403101L;

    @ApiModelProperty("企业id")
    private String id;
    @ApiModelProperty("企业名称")
    private String accountName;
    @ApiModelProperty("企业类型（0=供应商 1=代理机构 2=采购人）")
    private Integer accountType;
    @ApiModelProperty("法人代表人姓名")
    private String accountUser;
    @ApiModelProperty("法人证件信息")
    private Long accountUserId;
    @ApiModelProperty("注册资金")
    private String accountMoney;
    @ApiModelProperty("企业开户行账号")
    private Long accountNumber;
    @ApiModelProperty("企业开户行名称")
    private String accountNumberName;
    @ApiModelProperty("企业所在地")
    private String accountAddress;
    @ApiModelProperty("联系人")
    private String member;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱")
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

    @ApiModelProperty(value = "创建日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;
    @ApiModelProperty(value = "更新日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
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

    public void init(){
        Date nowDate = new Date();
        this.setId(String.valueOf(UUID.randomUUID().getMostSignificantBits()));
        this.setCreateDate(nowDate);
        this.setUpdateDate(nowDate);
    }

    public void update(){
        this.setUpdateDate(new Date());
    }
}
