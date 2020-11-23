package com.kglab.tool.util.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kglab.tool.util.base.StringUtil;

public class RequestUtil {
	
	
	/**
	 * 获取当前被请求的地址
	 * @return
	 * @date 2018年3月1日
	 * @author 舒超
	 */
	public static String getSessionID(){
		try {
			HttpServletRequest request = getRequest();
			String sessionID = request.getSession().getId();
			return sessionID;
		} catch (Exception e) {
			System.out.println("获取sessionID异常");
			return null;
		}
	}
	
	
	/**
	 * 获取当前被请求的地址
	 * @return
	 * @date 2018年3月1日
	 * @author 舒超
	 */
	public static String getRequestUrl(){
		HttpServletRequest request = getRequest();
		String url = request.getRequestURL().toString() + "?" + request.getQueryString();
		return url;
	}
	
	/**
	 * 查看referer字段是否合法
	 * @return
	 * @date 2018年3月1日
	 * @author 舒超
	 */
	public static boolean checkRefererLegal() {
		HttpServletRequest request = getRequest();
		String requestUrl = request.getRequestURL().toString();
		String refererUrl = request.getHeader("Referer");
		if(StringUtil.isBaseUrlEquals(requestUrl, refererUrl)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 获取请求参数集合
	 * <br><br>
	 * 
	 * @return
	 * @date 2018年3月1日
	 * @author 舒超
	 */
	public static Map<String,String> getRequestParams(){
		Map<String,String> paramMap = new HashMap<String,String>();
		HttpServletRequest request = getRequest();
		Map<String,String[]> params = request.getParameterMap();
		Iterator<Entry<String, String[]>> its = params.entrySet().iterator();
		while(its.hasNext()){
			Entry<String,String[]> entry = its.next();
			String key = entry.getKey();
			String[] value = entry.getValue();
			paramMap.put(key, value==null?null:value[0]);
		}
		return paramMap;
	}
	
	
	/**
	 * 根据当前请求获取网站访问的根url
	 * @author shuchao
	 * @data   2019年3月8日
	 * @remark 不包含最后的"/"
	 * @return
	 */
	public static String getRequestBaseUrl() {
		return getRequestBaseUrl(getRequest()) ;
	}


	private static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}


	public static String getRequestBaseUrl(HttpServletRequest request) {
		return request.getScheme() //当前链接使用的协议
			    +"://" + request.getServerName()//服务器地址 
			    + ":" + request.getServerPort() //端口号 
			    + request.getContextPath(); //应用名称，如果应用名称为
	}
	
}
