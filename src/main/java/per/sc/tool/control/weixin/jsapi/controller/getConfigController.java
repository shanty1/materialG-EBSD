package per.sc.tool.control.weixin.jsapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import per.sc.tool.control.BaseAction;
import per.sc.tool.util.cache.CacheUtils;
import per.sc.tool.util.weixin.WxJsapiUtil;

/**
 * 微信JS-SDK配置
 * @author shuchao
 * @date   2019年2月3日
 */
@RestController
@RequestMapping("/")
public class getConfigController extends BaseAction{
	
	@RequestMapping("/getconfig")
	public void getWxconfig() {
		JSONObject wxconfig = (JSONObject) CacheUtils.getCache("weixinconfig");
		if(wxconfig==null) {
			wxconfig = WxJsapiUtil.getWxjsapiConfig();
			CacheUtils.setCache("weixinconfig", wxconfig,-1);
		}
		print(SUCCESS, "OK", wxconfig);
	}
}
