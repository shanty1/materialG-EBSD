package per.sc.tool.util.weixin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import per.sc.tool.util.base.PropertiesUtil;
import per.sc.tool.util.base.StringUtil;
import per.sc.tool.util.http.HttpUtil;
import per.sc.tool.util.parser.JsonUtil;
import per.sc.tool.util.web.RequestUtil;

/**
 * 微信接口处理类
 * @author shuchao
 * @date   2019年2月3日
 */
public class WxJsapiUtil {

	public static final String TOKEN_API;
	public static final String JSAPI_TICKET_API;
	public static final String APPID;
	public static final String APPSECRET; 
	
	public static Logger log = LoggerFactory.getLogger(WxJsapiUtil.class);
	
	static {
		Map<String, String> configMap = PropertiesUtil.loadPropertiesConfig("weixin/wx_interface.properties");
		TOKEN_API 			= 	configMap.get("token_api");
		JSAPI_TICKET_API 	= 	configMap.get("jsapi_ticket_api");
		APPID 				= 	configMap.get("AppID");
		APPSECRET 			= 	configMap.get("AppSecret");
	}
	
	/**
     *  获取微信公众号access_token
     * 
     * @author shuchao
     * @date 2019年2月21日 上午9:53:26
     * @param appid - 开发者ID
     * @param AppSecret - 开发者密码
     */
    public static String getAccessToken(String appid, String appSecret) {
        String url = StringUtil.format(TOKEN_API, appid, appSecret);
        JSONObject result = JsonUtil.parseJsonObject(HttpUtil.getMethod(url).getResponseHtml());
        return result.getString("access_token") ;
    }
    
    /**
     * 获取微信ticket 
     * 
     * @author shuchao
     * @date 2019年2月21日 上午9:53:26
     * @param access_token
     */
    public static String getTicket(String token) {
    	String url = StringUtil.format(JSAPI_TICKET_API, token);
        JSONObject result = JsonUtil.parseJsonObject(HttpUtil.getMethod(url).getResponseHtml());
        return result.getString("ticket");
    }

    /**
     * 生成JS-SDK权限验证的签名
     * @param jsapi_ticket
     * @param noncestr  随机字符串
     * @param timestamp 时间戳
     * @param url 当前请求url，不包含#部分
     */
    public static String getSign(String jsapi_ticket, String noncestr, Long timestamp, String url) throws NoSuchAlgorithmException{
        String shaStr = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "×tamp=" + timestamp + "&url="
                + url;
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(shaStr.getBytes());
        StringBuffer signature = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            signature.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return signature.toString();
    }
    
    /**
     * 根据项目配置文件里的微信开发者配置获取wxjs config
     */
    public static JSONObject getWxjsapiConfig() {
    	String token  = getAccessToken(APPID, APPSECRET);
    	String ticket = getTicket(token);
    	String noncestr = RandomStringUtils.randomAlphanumeric(16);
    	long timestamp = System.currentTimeMillis()/1000;
    	String url = RequestUtil.getRequestUrl();
    	String signature = "";
    	try {
			signature = getSign(ticket, noncestr, timestamp, url);
		} catch (NoSuchAlgorithmException e) {
			log.error("生成微信签名失败", e);
		}
    	JSONObject json = new JSONObject();
    	json.put("appId", APPID);
    	json.put("timestamp", timestamp);
    	json.put("nonceStr", noncestr);
    	json.put("signature", signature);
    	return json;
    }
}
