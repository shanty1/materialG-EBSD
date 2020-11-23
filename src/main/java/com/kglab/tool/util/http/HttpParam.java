package com.kglab.tool.util.http;

import java.util.Map;


public class HttpParam {

	private String url ;
	
	private String type;
	
	private String paramCharset;
	
	private Map<String,String> paramMap;
	
	private String jsonStringParam;
 
	private Map<String,String> headerMap;
	
	private String htmlCharset;

	public HttpParam(){
		
	}
	
	public HttpParam(String url, String type, String paramCharset,
			Map<String,String> paramMap, String jsonStringParam, Map<String,String> headerMap, String htmlCharset) {
		super();
		this.url = url;
		this.type = type;
		this.paramCharset = paramCharset;
		this.paramMap = paramMap;
		this.jsonStringParam = jsonStringParam;
		this.headerMap = headerMap;
		this.htmlCharset = htmlCharset;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParamCharset() {
		return paramCharset;
	}

	public void setParamCharset(String paramCharset) {
		this.paramCharset = paramCharset;
	}

	public Map<String,String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String,String> paramMap) {
		this.paramMap = paramMap;
	}

	public Map<String,String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String,String> headerMap) {
		this.headerMap = headerMap;
	}

	public String getHtmlCharset() {
		return htmlCharset;
	}

	public void setHtmlCharset(String htmlCharset) {
		this.htmlCharset = htmlCharset;
	}

	public String getJsonStringParam() {
		return jsonStringParam;
	}

	public void setJsonStringParam(String jsonStringParam) {
		this.jsonStringParam = jsonStringParam;
	}
	
}
