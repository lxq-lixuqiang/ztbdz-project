package com.ztbdz.user.web.config;

import com.ztbdz.user.service.LandlordService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.interceptor.AuthenticationInterceptor;
import com.ztbdz.user.web.util.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    public UserService userService;
    @Autowired
    public TokenBlacklistService blacklistService;
    @Autowired
    public LandlordService landlordService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(userService,blacklistService,landlordService))
                .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
    }

}
