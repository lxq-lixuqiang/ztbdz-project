package com.ztbdz.web.controller;


import com.ztbdz.web.token.CheckToken;
import com.ztbdz.web.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "缓存信息")
@RequestMapping("/redis")
@RestController
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "删除缓存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required=true,paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "key", value = "key", required=true, dataType = "String")
    })
    @CheckToken
    @GetMapping("get/{key}")
    public Result get(@PathVariable String key) {
        redisTemplate.delete(key);
        return Result.ok("删除缓存成功！");
    }

}
