package com.ztbdz.web.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    /**
     * 处理所有未匹配的路由
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // 获取错误状态码
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // 根据状态码返回不同的错误页面
        if (statusCode != null) {
            if (statusCode == 404) {
                return "redirect:/404.html";
            }
        }
        return "error";
    }

    /**
     * 提供错误路径
     */
    public String getErrorPath() {
        return "/error";
    }
}
