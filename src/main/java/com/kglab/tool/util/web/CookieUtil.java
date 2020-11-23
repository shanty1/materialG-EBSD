package com.kglab.tool.util.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kglab.tool.constant.SecretKey;
import com.kglab.tool.util.base.StringUtil;
import com.kglab.tool.util.crypt.EncodeUtils;

public class CookieUtil {
     
	/** cookie默认有效期(session级别) */
	public static final int DEFAULT_COOKIE_EXPIRE = -1;
	/** 七天时长(单位：秒) */
    public static final int SEVEN_DAY = 7 * 24 * 3600;
    /** 半小时时长(单位：秒) */
    public static final int HALF_HOUR = 30 * 60;
    /** 是否开启HttpOnly(默认：true) */
    public static final boolean HTTPONLY = true;
     
    private static Logger logger = LoggerFactory.getLogger(CookieUtil.class); 
    
    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     * 
     * @param request
     * @param name
     * @return
     */ 
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies(); 
		if (cookies==null) {
			return null;
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}
	
     
    /**
     * 根据Cookie名称直接得到Cookie值
     * 
     * @param request
     * @param name
     * @return
     */ 
    public static String getCookieValue(HttpServletRequest request, String name) { 
        Cookie cookie = getCookie(request, name); 
        if(cookie != null){
            return cookie.getValue();
        }
        return null; 
    }
     
	
	/**
	 * des解密获取cookie信息
	 *  <br><br>
	 *  默认采用des解密
	 */
	public static String getCookieValueSafely(HttpServletRequest request, String cookieName) {
		String value = getCookieValue(request, cookieName);
		if(value==null) return null;
		try {
			value = EncodeUtils.desDecode(value, SecretKey.DEFAULT_COOKIE_KEY);
		} catch (Exception e) {
			logger.error("cookie 解密异常", e);
		}
		return value;
	}
	
	/**
	 * 写入cookie信息，如果没有设置iExpdate，默认为-1
	 * 
	 * @param name
	 * @param value
	 */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, null, DEFAULT_COOKIE_EXPIRE);
	}
	
	/**
	 * des加密写入cookie信息，如果没有设置iExpdate，默认为-1
	 *  <br><br>
	 *  默认采用des加密
	 * @param name
	 * @param value
	 */
	public static void setCookieSafely(HttpServletRequest request, HttpServletResponse response, String name, String value) {
		try {
			value = EncodeUtils.desEncode(value, SecretKey.DEFAULT_COOKIE_KEY);
		} catch (Exception e) {
			logger.error("cookie 加密异常", e);
		}
		setCookie(request,response, name, value);
	}
	
	/**
	 * 写入Cookie信息
	 * @author shuchao
	 * @data   2019年2月17日
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param expDate
	 */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int expDate) {
		setCookie(response, name, value, null, expDate);
	}
	
	/**
	 * 写入cookie信息，如果没有设置iExpdate，默认为-1,增加设置domain
	 * @author shuchao
	 * @data   2019年2月17日
	 * @param response
	 * @param name
	 * @param value
	 * @param domain
	 */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain) {
		setCookie(response, name, value, domain, DEFAULT_COOKIE_EXPIRE);
	}
	
    /**
     * 添加一条新的Cookie，可以指定过期时间(单位：秒)
     * @author shuchao
     * @data   2019年2月17日
     * @param response
     * @param name
     * @param value
     * @param domain
     * @param expDate 超期时间(秒)
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain, int expDate) { 
        if (StringUtil.isEmpty(name)) return; 
        if (null == value) 			  value = ""; 
        Cookie cookie = new Cookie(name, EncodeUtils.escapeJS(value));
        cookie.setMaxAge(expDate);
        cookie.setPath("/");
        if(domain!=null && !domain.trim().isEmpty())
        	cookie.setDomain(domain);
        cookie.setHttpOnly(HTTPONLY);
        response.addCookie(cookie);
    }
     
    
    /**
     * 将cookie封装到Map里面
     * @param request
     * @return
     */
    public static Map<String,Cookie> getCookieMap(HttpServletRequest request){ 
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
     
    /**
     * 移除cookie
     * @param request
     * @param response
     * @param name 这个是名称，不是值
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) { 
        if (null == name) { 
            return; 
        } 
        Cookie cookie = getCookie(request, name); 
        if(null != cookie){ 
            cookie.setPath("/"); 
            cookie.setValue(""); 
            cookie.setMaxAge(0); 
            response.addCookie(cookie);
        } 
    }
    
	
}