package com.ztbdz.web.config;

import com.ztbdz.user.service.LandlordService;
import com.ztbdz.user.service.MemberService;
import com.ztbdz.user.service.UserService;
import com.ztbdz.web.interceptor.AuthenticationInterceptor;
import com.ztbdz.web.util.TokenBlacklistService;
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
    @Autowired
    public MemberService memberService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(userService,blacklistService,landlordService,memberService))
                .addPathPatterns("/**") // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
                .excludePathPatterns(   // 放行请求
                        "/",
                        "/user/login",
                        "/user/register",
                        "/static/**",
                        "/config/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/plugin/**",
                        "/*.txt",
                        "/*.html",
                        "/*.htm",
                        "/*.js",
                        "/*.css",
                        "/*.ico",
                        "/*.xml"
                );
    }

}
