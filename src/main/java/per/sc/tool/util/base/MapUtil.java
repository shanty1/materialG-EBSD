package per.sc.tool.util.base;

import java.util.Map;
@SuppressWarnings("rawtypes")
public class MapUtil {
	
	/**从map中取String类型值，如不存在返回空字符串*/
	public static String getString(Map map,String key) {
		if(map==null) return "";
		return map.get(key) == null ? "" : map.get(key).toString();
	}
	/**从map中取boolean类型值，如失败返回设置的默认值*/
	public static boolean getBoolean(Map map,String key,boolean defaultValue) {
		try {
			return Boolean.parseBoolean(getString(map, key));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	/**从map中取int类型值，如失败返回设置的默认值*/
	public static int getInt(Map map,String key,int defaultValue) {
		try {
			return Integer.parseInt(getString(map, key));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	/**从map中取long类型值，如失败返回设置的默认值*/
	public static long getLong(Map map,String key,long defaultValue) {
		try {
			return Long.parseLong(getString(map, key));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	/**从map中取float类型值，如失败返回设置的默认值*/
	public static float getFloat(Map map,String key,float defaultValue) {
		try {
			return Float.parseFloat(getString(map, key));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	/**从map中取double类型值，如失败返回设置的默认值*/
	public static double getDouble( Map map,String key,double defaultValue) {
		try {
			return Double.parseDouble(getString(map, key));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	/**从map中取Object类型值，如失败返回设置的默认值*/
	public static Object getObject(Map map,String key) {
		try {
			return map.get(key);
		} catch (Exception e) {
			return null;
		}
	}
	
}
