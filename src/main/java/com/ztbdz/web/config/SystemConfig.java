package com.ztbdz.web.config;

import com.alibaba.excel.EasyExcel;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.user.pojo.Member;
import com.ztbdz.web.export.ProjectRegisterExport;
import com.ztbdz.web.util.Common;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * redis缓存标识 - 登录信息
     */
    public static String REDIS_LOGIN_INFO = "loginInfo";

    /**
     * redis缓存标识 - 角色与菜单关联标识
     */
    public static String REDIS_ROLE_AND_MENU = "roleAndMenu";
    /**
     * redis缓存标识 - 全部菜单标识
     */
    public static String REDIS_ALL_MENU = "allMenu";

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

    /**
     * 默认页码大小
     */
    public static Integer PAGE_SIZE;
    @Value("${ztbdz.default.pageSize}")
    private Integer pageSize;

    @PostConstruct
    public void init() {
        UPLOAD_FILE_URL = uploadFileUrl;
        UPLOAD_FILE_TEMP = uploadFileUrl+"/temp";
        X_AXIS = x_axis;
        Y_AXIS= y_axis;
        PAGE_SIZE = pageSize;

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
     * 当前登录人默认赋值
     * @return
     */
    public static Member getCreateMember(){
        Member member = new Member();
        member.setId(Long.valueOf(SystemConfig.getSession(Common.SESSION_LOGIN_MEMBER_ID).toString()));
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

    /**
     * 数字金额大写转换
     */
    public static String digitUppercase(double n) {
        String fraction[] = { "角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = { { "元", "万", "亿"}, { "", "拾", "佰", "仟"}};

        String head = n < 0 ? "负" : "";
        n = Math.abs(n);

        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1) {
            s = "整";
        }
        int integerPart = (int) Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }

    /**
     * 导入Excel文件数据
     * @param file 文件
     * @param fields 对应字段名称
     * @return
     * @throws Exception
     */
    public static List<Map<String,String>> importExcelData(MultipartFile file,String[] fields) throws Exception {
        List<Map<String,String>> paramList = new ArrayList();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        try{
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个Sheet
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) rowIterator.next();// 跳过标题行（假设第一行是标题）
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(row.getLastCellNum() == fields.length){
                    if(StringUtils.isEmpty(getCellStringValue(row.getCell(0))) && StringUtils.isEmpty(getCellStringValue(row.getCell(1)))){
                        // 防止空数据,如果第一列和第二列都为空，说明下面行都是空行
                        break;
                    }
                    Map<String,String> paramMap = new HashMap();
                    for(int i=0;i<row.getLastCellNum();i++){
                        paramMap.put(fields[i],getCellStringValue(row.getCell(i)));
                    }
                    paramList.add(paramMap);
                }else{
                    throw new Exception("字段数量不一致！");
                }
            }
        }catch (Exception e){
            throw e;
        }finally {
            if(workbook!=null) workbook.close();
        }
        return paramList;
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING); // 统一按字符串读取
        return cell.getStringCellValue().trim();
    }

    /**
     * 导出Excel文件数据
     * @param fileName 文件名称
     * @param dataList 数据集合
     * @param entity 实体类对象
     * @return
     * @throws Exception
     */
    public static ResponseEntity<byte[]> excelExport(String fileName,List dataList,Class<?> entity) throws Exception{
        File file = null;
        try{
            String fileInfo = UUID.randomUUID().getMostSignificantBits()+".xlsx";
            file = new File(SystemConfig.UPLOAD_FILE_TEMP,fileInfo);
            file.createNewFile();

            EasyExcel.write(file, entity)
                    .sheet(fileInfo)
                    .doWrite(dataList);
            byte[] body=FileUtils.readFileToByteArray(file);
            HttpHeaders headers=new HttpHeaders();
            // 防止下载文件名乱码
            String encodedFileName = URLEncoder.encode(fileName+".xlsx", "UTF-8").replaceAll("\\+", "%20");  // 替换空格编码
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().headers(headers).body(body);
        }finally {
            if(file!=null) file.delete();
        }

    }

}
