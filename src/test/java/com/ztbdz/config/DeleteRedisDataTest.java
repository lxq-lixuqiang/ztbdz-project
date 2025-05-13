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
        redisTemplate.delete(SystemConfig.ROLE_AND_MENU); // 角色和菜单关联信息
        redisTemplate.delete(SystemConfig.ALL_MENU); // 全部菜单信息
        System.out.println("删除redis缓存成功！");
    }
}
