package com.ztbdz.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 将Token加入黑名单（设置剩余有效期）
    public void addToBlacklist(String token, long expireTime) {
        redisTemplate.opsForValue().set("blacklist:" + token, "logged_out",
                expireTime, TimeUnit.MILLISECONDS);
    }

    // 检查Token是否在黑名单
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }

}
