package com.ztbdz.user.web.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * 系统配置
 */
public class SystemConfig {

    /**
     * MD5加密 密钥
     */
    public static String MD5_KEY = "ztbdz";

    /**
     * 上传文件路基
     */
    @Value("upload.file.url")
    public static String UPLOAD_FILE_URL;

    /**
     * 短信标识
     */
    public static String SMS = "smsList";

    /**
     * 默认密码后缀
     */
    public static String DEFAULT_PASSWORD = "1234";

}
