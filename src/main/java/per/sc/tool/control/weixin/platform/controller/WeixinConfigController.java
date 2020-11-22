package per.sc.tool.control.weixin.platform.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.sc.tool.control.BaseAction;
import per.sc.tool.util.weixin.SignUtil;

/**
 * 公众号服务器配置,此接口由微信访问检测
 * @author shuchao
 * @date   2019年2月3日
 */
@RestController
@RequestMapping("/")
public class WeixinConfigController extends BaseAction {
	
	@RequestMapping("/platform")
	public String wxplatform(String signature, String timestamp, String nonce,String echostr){
		if (SignUtil.checkSignature(signature, timestamp, nonce))
			return echostr;
		return null;
	}
}