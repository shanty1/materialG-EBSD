package com.kglab.tool.wrapper;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
	
	private String encoding = "UTF-8";
	private boolean encodingOn;
	private boolean trimOn;
	
	public MyHttpServletRequestWrapper(HttpServletRequest request, String encoding, boolean encodingOn,
			boolean trimOn) {
		super(request);
		
		/*
		 *  是否转换编码 
		 */
		this.encodingOn = encodingOn;
		String econ = request.getParameter("econ");
		if (econ != null) {
			if ("y".equalsIgnoreCase(econ)) {
				this.encodingOn = true;
			} else if ("n".equalsIgnoreCase(econ)) {
				this.encodingOn = false;
			}
		}
		
		if (encoding != null && !"".equals(encoding.trim())) {
			this.encoding = encoding;
		}
		
		/*
		 *  是否去除参数左右空格
		 */
		String ec = request.getParameter("ec");
		if (ec != null && !"".equals(ec.trim())) {
			this.encoding = ec.trim();
		}
		
		this.trimOn = trimOn;
		String trimon = request.getParameter("trimon");
		if (trimon != null) {
			if ("y".equalsIgnoreCase(trimon)) {
				this.trimOn = true;
			} else if ("n".equalsIgnoreCase(trimon)) {
				this.trimOn = false;
			}
		}
	}
	
	@Override
	public String getParameter(String key) {
		String ret = super.getParameter(key);
		if (ret != null) {
			ret = getEncodedString(ret, encoding);
		}
		return ret;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = super.getParameterMap();
		if (map == null) {
			return map;
		}
		
		ParameterMap<String, String[]> parameterMap = new ParameterMap<String, String[]>();
		
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String[] strs = map.get(key);
			String[] tempStr = null;
			if (strs != null) {
				tempStr = new String[strs.length];
				for (int i = 0; i < strs.length; i++) {
					tempStr[i] = getEncodedString(strs[i], encoding);
				}
			}
			parameterMap.put(key, tempStr);
		}
		return parameterMap;
	}
	
	@Override
	public String[] getParameterValues(String key) {
		String[] ret = super.getParameterValues(key);
		
		if (ret != null) {
			for (int i = 0; i < ret.length; i++) {
				ret[i] = getEncodedString(ret[i], encoding);
			}
		}
		
		return ret;
	}
	
	private String getEncodedString(String value, String enc) {
		if (encodingOn) {
			try {
				value = new String(value.getBytes("ISO-8859-1"), encoding);
			} catch (UnsupportedEncodingException e) {
			}
		}
		if (trimOn) {
			value = value.trim();
		}
		return value;
	}
	
}
