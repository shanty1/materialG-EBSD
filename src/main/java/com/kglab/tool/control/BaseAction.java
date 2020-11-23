package com.kglab.tool.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.kglab.tool.constant.SecretKey;
import com.kglab.tool.constant.StatusCode;
import com.kglab.tool.entity.vo.ResultVo;
import com.kglab.tool.util.base.StringUtil;
import com.kglab.tool.util.crypt.EncodeUtils;
import com.kglab.tool.util.web.CookieUtil;
import com.kglab.tool.util.web.RequestUtil;

public abstract class BaseAction{
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;
	@Autowired
	protected HttpSession session;
	
	protected final static int SUCCESS = StatusCode.SUCCESS;
	protected final static int FAIL = StatusCode.FAIL;
	protected final static int REBUT = StatusCode.REBUT;
	protected final static int CONSTOM = StatusCode.CONSTOM;
	
	/** 获取Request */
	protected HttpServletRequest getRequest() {
		if(this.request==null)
			this.request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return this.request;
	}

	/** 获取Response */
	protected HttpServletResponse getResponse() {
		if(this.response==null)
			this.response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		return this.response;
	}

	/** 获取session */
	protected HttpSession getSession() {
		if(this.session==null)
			session = getRequest().getSession();
		return session;
	}
	
	/** 获取Application */
	protected ServletContext getApplication() {
		return getRequest().getServletContext();
	}
	
	
	protected String getRequestBaseUrl(){
		return RequestUtil.getRequestBaseUrl(request);
	}
	
	protected void setRequestAttribute(String name, Object value){
		getRequest().setAttribute(name, value);
	}
	
	protected void setSessionAttribute(String name, Object value){
		getSession().setAttribute(name, value);
	}
	
	protected void removeSessionAttribute(String... name ){
		for(String s:name)
			if(StringUtil.isNotEmpty(s))
				getSession().removeAttribute(s); 
	}
	
	protected void setApplicationAttribute(String name, Object value){
		getApplication().setAttribute(name, value);
	}
	
	protected Object getRequestAttribute(String name){
		return getRequest().getAttribute(name);
	}
	
	protected Object getSessionAttribute(String name){
		return getSession().getAttribute(name);
	}
	
	protected Object getApplicationAttribute(String name){
		return getApplication().getAttribute(name);
	}
	
	protected String getParameter(String name) {
		return getRequest().getParameter(name);
	}
	
	/**
	 * des加密写入cookie信息，如果没有设置iExpdate，默认为-1
	 *  <br><br>
	 *  默认采用des加密
	 * @param name
	 * @param value
	 */
	protected void setCookieSafely(String name, String value) {
		try {
			value = EncodeUtils.desEncode(value, SecretKey.DEFAULT_COOKIE_KEY);
		} catch (Exception e) {
			logger.error("cookie 加密异常", e);
		}
		CookieUtil.setCookie(request,response, name, value);
	}
	
	/**
	 * 写入cookie信息，如果没有设置iExpdate，默认为-1
	 * 
	 * @param name
	 * @param value
	 */
	protected void setCookie(String name, String value) {
		CookieUtil.setCookie(request,response, name, value);
	}
	
	/**
	 * 写入Cookie信息
	 * 
	 * @param name
	 * @param value
	 * @param expDate
	 */
	protected void setCookie(String name, String value, int expDate) {
		setCookie(name, value, expDate, null);
	}
	
	/**
	 * 写入cookie信息，如果没有设置iExpdate，默认为-1,增加设置domain
	 * 
	 * @param name
	 * @param value
	 * @param domain
	 */
	protected void setCookie(String name, String value, String domain) {
		setCookie(name, value,CookieUtil.DEFAULT_COOKIE_EXPIRE, domain);
	}
    
    /**
     * 写入Cookie信息
     * 
     * @param name
     * @param value
     * @param expDate 单位秒
     * @param domain 域名
     */
	protected void setCookie(String name, String value, int expDate, String domain) {
		CookieUtil.setCookie(getResponse(), name, value, domain, expDate);
    }
	
	/**
	 * 得到cookie信息，如果找不到返回null
	 * 
	 * @param cookieName
	 * @return
	 */
	protected String getCookieValue(String cookieName) {
		String value = CookieUtil.getCookieValue(getRequest(), cookieName);
		if(StringUtil.isNotEmpty(value)) {
			value = EncodeUtils.unescapeJS(value);
		}
		return value ;
	}
	
	/**
	 * des解密获取cookie信息
	 *  <br><br>
	 *  默认采用des解密
	 * @param name
	 */
	protected String getCookieValueSafely(String cookieName) {
		String value = getCookieValue(cookieName);
		if(value==null) return null;
		try {
			value = EncodeUtils.desDecode(value, SecretKey.DEFAULT_COOKIE_KEY);
		} catch (Exception e) {
			logger.error("cookie 解密异常", e);
		}
		return value;
	}
	
	protected void clearCookie(String... cookieName) {
		for(String name:cookieName)
			CookieUtil.removeCookie(getRequest(), getResponse(), name);
	}
	
	/** 获得请求头信息 **/
	protected String getRequestHeader(String name) {
		return getRequest().getHeader(name);
	}
	
	/** 设置响应头信息 (如果同名头已存在，则覆盖)**/
	protected void setResponseHeader(String name,String value) {
		getResponse().setHeader(name, value);
	}
	
	/** 添加响应头信息 (可添加同名头信息)**/
	protected void addResponseHeader(String name,String value) {
		getResponse().addHeader(name, value);
	}
	
	/** 直接响应结果(调用此方法后请直接return;结束当前方法) */
	protected void print(int errorNo) {
		print(errorNo, null, null);
	}
	/** 直接响应结果(调用此方法后请直接return;结束当前方法) */
	protected void print(int errorNo, String errorMsg) {
		print(errorNo, errorMsg, null);
	}
	/** 直接响应结果(调用此方法后请直接return;结束当前方法) */
	protected void print(int errorNo, String errorMsg, Object data) {
		print(new ResultVo(errorNo, errorMsg, data));
	}
	/** 直接响应结果(调用此方法后请直接return;结束当前方法) */
	protected void print(Object obj) {
		PrintWriter pw = null;
		try {
			HttpServletResponse response = getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			pw = new PrintWriter(response.getOutputStream(),true);
			if(ResultVo.class.isInstance(obj))
				pw.println(getOutput((ResultVo)obj));
			else
				pw.println(obj.toString());
		} catch (IOException e) {
			logger.error("response print IO exception ", e);
		} finally{
			pw.close();
		}
	}
	
	/** 获得json格式化响应内容 */
	protected String getOutput(int errorNo) {
		return getOutput(errorNo, null, null);
	}
	/** 获得json格式化响应内容 */
	protected String getOutput(int errorNo, String errorMsg) {
		return getOutput(errorNo, errorMsg, null);
	}
	/** 获得json格式化响应内容 */
	protected String getOutput(int errorNo, String errorMsg, Object data) {
		return getOutput(new ResultVo(errorNo, errorMsg, data));
	}
	/** 获得json格式化响应内容 */
	protected String getOutput(ResultVo rVo) {
		return JSONObject.toJSONString(rVo);
	}
	
}
