package com.kglab.tool.util.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {
	
	public static Map<String, String> props = new HashMap<String, String>();
	
	
	/**
	 * 加载资源文件
	 * <br><br>
	 * 
	 * @param resource 资源文件名
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	public static InputStream loadFileAsStream(String resource){
		try {
			return PropertiesUtil.class.getResourceAsStream("/"+resource);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Properties loadProperties(String resourceName){
		Properties prop = new Properties();
		try {
			InputStream in = loadFileAsStream(resourceName);
			prop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	/** 获取属性文件键值对集合(读取文件为UTF-8编码，会对iso进行UTF-8转码编码字符)*/
	public static Map<String, String> loadPropertiesConfig(String resourceName){
		Map<String,String> map = new HashMap<String, String>();
		try {
			Properties prop = loadProperties(resourceName);
			if(prop.size()>0){
				Set<Object> keys = prop.keySet();
				for (Object obj : keys) {
					if(obj!=null){
						map.put(StringUtil.iso2utf8(obj.toString()).trim(), StringUtil.iso2utf8(prop.getProperty(obj.toString())).trim());
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return map;
	}
	
	/** 获取属性文件键值对集合(读取文件为UTF-8编码，此方法会对iso进行UTF-8转码编码字符)*/
	public static String loadPropertiesConfig(String resourceName, String key){
		try{
			return StringUtil.iso2utf8(loadPropertiesConfig(resourceName).get(key).toString()).trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
