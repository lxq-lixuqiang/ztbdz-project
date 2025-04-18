package com.ztbdz.user.web.util;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;



/**
 * 工具类
 *
 */
public abstract class WebTools {
	
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

	public static void setRequest(String name,Object value) {
		RequestAttributes requestAttributes=RequestContextHolder.currentRequestAttributes();
		if(requestAttributes != null) {
			requestAttributes.setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
		}
	}
	
	public static void sessionToRequest(String msg) {
		sessionToRequest(msg,true);
	}
	
	public static void sessionToRequest(String msg,boolean isOk) {
		Object value=getSession(msg);
		if(value != null) {
			if(isOk) {
				removeSession(msg);
			}
			setRequest(msg,value);
		}
	}

	public static String getPath(HttpServletRequest request) {
		return request.getScheme()+"://"+
				request.getServerName()+":"+
				request.getServerPort()+
				request.getContextPath()+"/";
	}

	@SuppressWarnings("unchecked")
	public static <T>T memory(Object pageSize, String name, Object num) {
		Object value=pageSize;
		if(value == null) {
			value=getSession(name);
			if(value == null) {
				value = num;
			}
		}
		return (T) value;
	}

	public static List<String> pagingButton(int pageNum, int pages) {
		List<String> pagingButton=new ArrayList<>();
		if(pageNum >0 && pages >0) {
			for(int i=pageNum-4;i<=pageNum+4 && i<=pages;i++){
				if(i<1) {
					continue;
				}
				if(i==pageNum-4) {
					pagingButton.add("...");
					continue;
				}
				if(i==pageNum+4) {
					pagingButton.add("...");
					continue;
				}
				pagingButton.add(String.valueOf(i));
			}
		}
		return pagingButton;
	}

}
