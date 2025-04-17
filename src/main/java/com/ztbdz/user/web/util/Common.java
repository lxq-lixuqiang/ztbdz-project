package com.ztbdz.user.web.util;


import org.springframework.beans.factory.annotation.Value;

/**
 * 公共类
 *
 */
public interface Common {

    /** 消息标识 */
    String MSG="msg";
    /** 登录标识 */
    String LOGIN_USER = "loginUser";
    /** 成功状态 */
    Integer SUCCESS = 200;
    /** 失败状态 */
    Integer FAIL = -1;
    /** 错误状态 */
    Integer ERORR = -2;
}
