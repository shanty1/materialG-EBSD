package com.kglab.tool.app;

import java.io.IOException;
import java.util.Properties;


public class WebConfigHandle {
	private final static Properties configProperties = new Properties();
	
	
	static {
		try {
			configProperties.load(WebConfigHandle.class.getResourceAsStream("/config.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/** 获取字符串配置 , 不存在默认返回"" */
	public static String getByName(String key){
		return getByName(key, String.class, "");
	}
	/** 获取布尔型配置, 不存在默认返回false*/
	public static Boolean getBooleanByName(String key){
		return getByName(key, boolean.class, false);
	}
	/** 获取整型配置  , 不存在默认返回null*/
	public static Integer getIntegerByName(String key){
		return getByName(key, Integer.class, null);
	}
	/** 获取双精度浮点型配置  , 不存在默认返回null*/
	public static Double getDoubleByName(String key){
		return getByName(key, Double.class, null);
	}
	/** 获取单精度浮点型配置  , 不存在默认返回null*/
	public static Float getFloatByName(String key){
		return getByName(key, Float.class, null);
	}
	/** 获取长整型配置  , 不存在默认返回null*/
	public static Long getLongByName(String key){
		return getByName(key, Long.class, null);
	}
	/** 获取短整型配置  , 不存在默认返回null*/
	public static Short getShortByName(String key){
		return getByName(key, Short.class, null);
	}
	
	/** 统一获取属性文件值入口 */
	@SuppressWarnings("unchecked")
	private static <T> T getByName(String key, Class<T> clazz, T defaultVal){
		Object value = configProperties.get(key);
		// 如果配置为空, 返回默认值
		if(value==null || String.valueOf(value).trim().equals("")){
			return defaultVal;
		} 
		// 强转类型
		try {
			return (T)value;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static void main(String[] args) {
		System.out.println(getIntegerByName("SearchTypes"));
	}

}
