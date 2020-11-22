package per.sc.tool.util.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
/**
 * http|https 网络资源获取结果集
 * <br><br>
 * 用于保存爬虫常用的响应结果
 * @Date 2018年2月5日
 * @Author 舒超
 */
public class HttpResult {
	// 请求响应结果(文本形式保存)
	private String responseHtml = "无法获取网络资源数据";
	// 请求响应结果(字节形式保存)
	private byte[] responseByte = "无法获取网络资源数据".getBytes();
	// 响应cookie信息
	private CookieStore cookieStore = null;
	// 响应头集合
	private Map<String,String> headerMap = new HashMap<String, String>();
	// 重定向次数
	private short redirectCount = 0;
	// 最终重定向地址
	private String location = null;
	// 最终状态吗
	private int statusCode = -1;
	
	// 请求重定向记录
	private List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	
	/** 获取响应内容(文本形式，例如html) */
	public String getResponseHtml() {
		return responseHtml;
	}
	/** 设置响应内容(文本形式，例如html) */
	public void setResponseHtml(String responseHtml) {
		this.responseHtml = responseHtml;
	}
	/** 获取响应内容(字节形式, 例如图片、PDF等非文本文件, 以字节保存) */
	public byte[] getResponseByte() {
		return responseByte;
	}
	/** 设置响应内容(字节形式, 例如图片、PDF等非文本文件, 以字节保存) */
	public void setResponseByte(byte[] responseByte) {
		this.responseByte = responseByte;
	}
	/** 获取响应cookie */
	public CookieStore getCookieStore() {
		return cookieStore;
	}
	/** 设置响应cookie */
	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	/** 获取响应头集合 */
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}
	/** 设置响应头集合 */
	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}
	/** 获取请求重定向次数 */
	public short getRedirectCount() {
		return redirectCount;
	}
	/** 设置重定向次数 */
	public void redirectCountAdd1() {
		this.redirectCount = (short) (this.redirectCount + 1);
	}
	/** 获取重定向地址 */
	public String getLocation() {
		return location;
	}
	/** 设置重定向地址 */
	public void setLocation(String location) {
		this.location = location;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public void addResult(String url, int statusCode, String cookie){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("url", url);
		map.put("statusCode", statusCode);
		map.put("cookie", cookie);
		list.add(map);
	}
	public List<Map<String, Object>> getList() {
		return list;
	}

}
