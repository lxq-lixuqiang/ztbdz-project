package com.ztbdz.user.pojo;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@ApiModel("角色关联权限")
public class RoleRelatedAuthorize extends Model<RoleRelatedAuthorize> implements Serializable {
    @Getter
    private static final long serialVersionUID = 7312904238854888405L;

    @ApiModelProperty("角色id")
    private Long roleId;
    @ApiModelProperty("菜单权限id")
    private Long menuAuthorizeId;

    @Override
    protected Serializable pkVal() {
        return roleId+menuAuthorizeId;
    }
}
