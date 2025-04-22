package com.ztbdz.user.web.config;

import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统配置
 */
public class SystemConfig {

    /**
     * MD5加密 密钥
     */
    public static String MD5_KEY = "ztbdz";

    /**
     * 短信标识
     */
    public static String SMS = "smsList";
    /**
     * 默认密码后缀
     */
    public static String DEFAULT_PASSWORD = "1234";

    /**
     * token时效 默认30分钟
     */
    public static long TOKEN_VALIDITY = 1800000;

    /**
     * 文件存储路径
     * @param classify 文件地址
     * @param classify 文件分类
     * @return
     */
    public static String getPath(String uploadFileUrl,Integer classify) throws Exception{
        if(StringUtils.isEmpty(uploadFileUrl)) uploadFileUrl = System.getProperty("user.dir")+File.separator+"uploads";
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String[] nowDates = nowDate.split("-");
        return uploadFileUrl+File.separator+nowDates[0]+ File.separator+nowDates[1]+ File.separator+nowDates[2]+ File.separator+classify;
    }
}
