package com.ztbdz.config;

import com.ztbdz.web.config.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteRedisDataTest {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 删除Reids缓存
     */
    @Test
    public void databaseEncoder() {
//        redisTemplate.delete("initRole"); //初始化 角色
//        redisTemplate.delete("initAdmin"); //初始化 管理员
//        redisTemplate.delete("initMenu"); // 初始化菜单权限 并 关联角色
//        redisTemplate.delete(SystemConfig.REDIS_LOGIN_INFO); // 登录用户信息

        redisTemplate.delete(SystemConfig.REDIS_ROLE_AND_MENU); // 角色和菜单关联信息
        redisTemplate.delete(SystemConfig.REDIS_ALL_MENU); // 全部菜单信息
        System.out.println("删除redis缓存成功！");
    }
}
