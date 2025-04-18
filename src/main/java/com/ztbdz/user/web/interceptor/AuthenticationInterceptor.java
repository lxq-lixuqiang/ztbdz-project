package com.ztbdz.user.web.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ztbdz.user.pojo.User;
import com.ztbdz.user.service.UserService;
import com.ztbdz.user.web.token.CheckToken;
import com.ztbdz.user.web.token.LoginToken;
import com.ztbdz.user.web.util.JwtUtil;
import com.ztbdz.user.web.util.TokenBlacklistService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    public UserService userService;
    public TokenBlacklistService blacklistService;
    public AuthenticationInterceptor(UserService userService1,TokenBlacklistService blacklistService1){
        userService = userService1;
        blacklistService = blacklistService1;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有LoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }

                // 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getClaim("id").asString();
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("访问异常！");
                }
                if(userId==null){
                    throw new RuntimeException("token无效！");
                }
                User user = userService.getById(Long.valueOf(userId));
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                Boolean verify = JwtUtil.isVerify(token, user);
                if (!verify) {
                    throw new RuntimeException("非法访问！");
                }
                if (blacklistService.isBlacklisted(token)) {
                    throw new RuntimeException("Token已失效！");
                }

                return true;
            }
        }
        return true;
    }
}
