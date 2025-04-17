package com.ztbdz.user.web.util;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MD5加密盐值
 *
 */
public class MD5 {
	private final static Log log = LogFactory.getLog(MD5.class);

	/**
	 * 对字符串进行md5加密
	 * @param md5String
	 * @param md5Key
	 * @return md5加密后的密文
	 */
	public static String md5String(String md5String,String md5Key){
	    String md5Value = MD5Encoder(md5String + md5Key);
	    return md5Value;
	}
	
	public final static String MD5Encoder(String s) {
	    return MD5Encoder(s,"utf-8");
	}

	public final static String MD5Encoder(String s, String charset) {
	    try {
	        byte[] btInput = s.getBytes(charset);
	        MessageDigest mdInst = MessageDigest.getInstance("MD5");
	        mdInst.update(btInput);
	        byte[] md = mdInst.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < md.length; i++) {
	            int val = ((int) md[i]) & 0xff;
	            if (val < 16){
	                sb.append("0");
	            }
	            sb.append(Integer.toHexString(val));
	        }
	        return sb.toString();
	    } catch (Exception e) {
	    	log.error("MD5加密异常：" + e.getMessage());
	        return null;
	    }
	}
	
	/**
	 * MD5加密方式
	 * @param str
	 * @return
	 */
	public static String getMD5String(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
        	log.error("MD5加密异常：" + e.getMessage());
           return null;
        }
    }
}
