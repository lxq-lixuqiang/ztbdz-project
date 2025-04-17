package com.ztbdz.user.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共工具类
 */
public class CommonTools {


    /**
     * 返回数据
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static Map returnData(Integer code,String msg,Object data){
        Map<String,Object> returnMap = new HashMap();
        returnMap.put("code", code);
        returnMap.put("msg",msg);
        returnMap.put("data",data);
        return returnMap;
    }

    /**
     * 返回数据
     * @param code
     * @param msg
     * @return
     */
    public static Map returnData(Integer code,String msg){
        return returnData( code, msg,null);
    }

}
