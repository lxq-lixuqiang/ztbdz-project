package com.ztbdz.user.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
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

    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("企业名称")
    private String accountName;
    @ApiModelProperty("企业所属类型")
    private Integer accountType;
    @ApiModelProperty("法人代表人姓名")
    private String accountUser;
    @ApiModelProperty("法人证件信息")
    private Long accountUserID;
    @ApiModelProperty("注册资金")
    private String accountMoney;
    @ApiModelProperty("企业开户行账号")
    private Long accountNumber;
    @ApiModelProperty("企业开户行名称")
    private Long accountNumberName;
    @ApiModelProperty("企业所在地")
    private Long accountAddress;
    @ApiModelProperty("联系人")
    private String member;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("企业成立时间")
    private Date accountCreateDate;
    @ApiModelProperty("统一社会信用代码")
    private String accountCode;
    @ApiModelProperty("交易主体类型")
    private String dealType;

    @ApiModelProperty("是否删除：0-未删除 1-已删除")
    private Integer isDelete;
    @ApiModelProperty("是否停用：0-启用 1-停用")
    private Integer isStop;
    @ApiModelProperty("创建日期")
    private Date createDate;
    @ApiModelProperty("更新日期")
    private Date updateDate;
    @ApiModelProperty("扩展字段01")
    private Integer ext1;
    @ApiModelProperty("扩展字段02")
    private Integer ext2;
    @ApiModelProperty("扩展字段03")
    private String ext3;
    @ApiModelProperty("扩展字段04")
    private String ext4;
    @ApiModelProperty("扩展字段05")
    private String ext5;

    @Override
    protected Serializable pkVal() {
        return id;
    }

    public void init(){
        Date nowDate = new Date();
        this.setId(UUID.randomUUID().getMostSignificantBits());
        this.setCreateDate(nowDate);
        this.setUpdateDate(nowDate);
        this.setIsDelete(0);
        this.setIsStop(0);
    }

    public void update(){
        this.setUpdateDate(new Date());
    }
}
