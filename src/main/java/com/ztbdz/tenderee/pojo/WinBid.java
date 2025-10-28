package com.ztbdz.tenderee.pojo;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@ApiModel("中标")
public class WinBid extends Model<WinBid> implements Serializable {

    @Getter
    private static final long serialVersionUID = 6866972172611477739L;

    @ApiModelProperty("人员id")
    private Long memberId;
    @ApiModelProperty("中标id")
    private Long winBidId;

    @Override
    protected Serializable pkVal() {
        return memberId+winBidId;
    }
}
