package com.ztbdz.user.web.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@ApiModel("返回数据格式")
@Data
public class Result implements Serializable {

    @Getter
    private static final long serialVersionUID = 4250679060070934409L;


    @ApiModelProperty("状态码（200=成功 201=失败 500=错误）")
    private Integer status;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("数据")
    private Object data;

    public static Result ok(String msg,Object data){
        Result result = new Result();
        result.setStatus(Common.SUCCESS);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }
    public static Result ok(String msg){
        return ok(msg,null);
    }

    public static Result fail(String msg,Object data){
        Result result = new Result();
        result.setStatus(Common.FAIL);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static Result fail(String msg){
        return fail(msg,null);
    }

    public static Result error(String msg,Object data){
        Result result = new Result();
        result.setStatus(Common.ERORR);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static Result error(String msg){
        return error(msg,null);
    }
}
