package com.ztbdz.web.config;

import com.ztbdz.user.pojo.Member;
import com.ztbdz.web.util.Common;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 系统配置
 */
@Component
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
     * 文件上传地址
     */
    public static String UPLOAD_FILE_URL;
    @Value("${ztbdz.upload.file.url}")
    private String uploadFileUrl;

    /**
     * 临时文件地址
     */
    public static String UPLOAD_FILE_TEMP;
    /**
     * 盖章位置x和y
     */
    public static float X_AXIS;
    public static float Y_AXIS;
    @Value("${ztbdz.pdf.stamp.x}")
    private float x_axis;
    @Value("${ztbdz.pdf.stamp.y}")
    private float y_axis;

    @PostConstruct
    public void init() {
        UPLOAD_FILE_URL = uploadFileUrl;
        UPLOAD_FILE_TEMP = uploadFileUrl+"/temp";
        X_AXIS = x_axis;
        Y_AXIS= y_axis;

        // 创建临时文件目录
        File uploadFile = new File(SystemConfig.UPLOAD_FILE_TEMP);
        if(!uploadFile.exists()) uploadFile.mkdirs();
    }

    /**
     * 文件存储路径
     * @param classify 文件地址
     * @param classify 文件分类
     * @return
     */
    public static String getPath(Integer classify) throws Exception{
        if(StringUtils.isEmpty(UPLOAD_FILE_URL)) UPLOAD_FILE_URL = System.getProperty("user.dir")+File.separator+"uploads";
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String[] nowDates = nowDate.split("-");
        return UPLOAD_FILE_URL+File.separator+nowDates[0]+ File.separator+nowDates[1]+ File.separator+nowDates[2]+ File.separator+classify;
    }

    /**
     * 根据文件名后缀判断 MIME 类型
     */
    public static String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (!Arrays.asList("txt", "jpg", "jpeg", "png", "pdf","xml").contains(extension)) {
            throw new IllegalArgumentException("不支持的文件格式");
        }
        switch (extension) {
            case "txt": return MediaType.TEXT_PLAIN_VALUE;
            case "xml": return MediaType.TEXT_XML_VALUE;
            case "jpg":
            case "jpeg": return "image/jpeg";
            case "png": return "image/png";
            case "pdf": return "application/pdf";
            default: return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

    /**
     * 创建人默认赋值
     * @return
     */
    public static Member getCreateMember(){
        Member member = new Member();
        member.setId(Long.valueOf(SystemConfig.getSession(Common.LOGIN_MEMBER_ID).toString()));
        return member;
    }

    // 校验文件名并获取安全路径
    public static Path validateAndGetPath(String path,String filename) {
        Path baseDir = Paths.get(path).toAbsolutePath().normalize();
        Path filePath = baseDir.resolve(filename).normalize();

        if (!filePath.startsWith(baseDir)) {
            throw new SecurityException("非法文件路径访问");
        }
        return filePath;
    }

    public static void setSession(String name,Object value) {
        RequestAttributes requestAttributes=RequestContextHolder.currentRequestAttributes();
        if(requestAttributes != null) {
            requestAttributes.setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSession(String name) {
        RequestAttributes requestAttributes=RequestContextHolder.currentRequestAttributes();
        if(requestAttributes!=null) {
            return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_SESSION);
        }
        return null;
    }

    public static void removeSession(String name) {
        RequestAttributes requestAttributes=RequestContextHolder.currentRequestAttributes();
        if(requestAttributes != null) {
            requestAttributes.removeAttribute(name, RequestAttributes.SCOPE_SESSION);
        }

    }
}
