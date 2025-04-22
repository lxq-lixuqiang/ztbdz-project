package com.ztbdz.user.web.util;


import org.springframework.beans.factory.annotation.Value;

/**
 * 公共类
 *
 */
public interface Common {
    /** 成功状态 */
    Integer SUCCESS = 200;
    /** 失败状态 */
    Integer FAIL = 201;
    /** 错误状态 */
    Integer ERORR = 500;
    /** 正常状态：启用，未删除 */
    Integer ENABL = 0;
    /** 关闭状态：停用，已删除 */
    Integer DISABLE = 1;
}
