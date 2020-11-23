package com.kglab.func;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kglab.tool.util.base.StringUtil;
import com.kglab.tool.util.base.TimeUtil;
import com.kglab.tool.util.http.HttpResult;
import com.kglab.tool.util.http.HttpUtil;
import com.kglab.tool.util.io.FileUtils;
import com.kglab.tool.util.parser.Dom4jUtil;
import com.kglab.tool.util.parser.JsoupUtil;
import com.mchange.lang.StringUtils;

public class Crawler {
	static Map<String , String> headerMap = new HashMap<String, String>();
	static String urlString = "https://user.qzone.qq.com/proxy/domain/m.qzone.qq.com/cgi-bin/new/get_msgb?uin=1409149045&hostUin=1409149045&num=20&start={1}&hostword=0&essence=1&r=0.12078719250808612&iNotice=0&inCharset=utf-8&outCharset=utf-8&format=jsonp&ref=qzone&g_tk=509557269&qzonetoken=e3938b743b1b3240389aec04f0ff197c4434bd0e1c24c5ccc9069ca827009c3e3e80ff6e9d14&g_tk=509557269";
	static
	{
		String cookie = "eas_sid=c1C5W5X5h9Q9P5F89184q6K0y2; pac_uid=0_5cbe9c9f40cf9; pgv_pvi=2633728000; tvfe_boss_uuid=bd7bd56b255ebb93; RK=x1T0lwb9YQ; LW_sid=V1t5s5P7j507a6I4P538Z749U5; LW_uid=M1A5a5H7C5r7X6a4H578M8U0g4; ptui_loginuin=1409149045; __Q_w_s_hat_seed=1; __Q_w_s__QZN_TodoMsgCnt=1; pgv_pvid=7001074054; ptisp=ctc; ptcz=c3e2794a7d8e99d1e0689225cf411cb9d3120233c2cd566621c5593fcbfe2af8; uin=o1409149045; skey=@whmY8DpI5; p_uin=o1409149045; pt4_token=2AUKLKh1eEzcw7bnfV*jsRe*yfikdDn4Hq4pogB9VzM_; p_skey=flHqWencqWZ30aBL4t1yOV-P6M7yv1n6CWu*CYwY4Sg_; qzspeedup=sdch; pgv_info=ssid=s8201034786; qz_screen=1680x1050; QZ_FE_WEBP_SUPPORT=1; cpu_performance_v8=6; HIDE_EMOTION=0; Loading=Yes; pgv_si=s7424984064";
		headerMap.put("Cookie", cookie);
		HttpUtil.keepSameHttpClient(true);
	}
	/*
	public static void main(String[] args) {
	 Document doc = DocumentHelper.createDocument();
     Element root = doc.addElement("MessageBoard");


		for(int i=0; i<=260; i+=20) {
			String url = StringUtil.format(urlString, i);
			HttpResult httpResult = HttpUtil.getMethod(url, headerMap);
			JSONObject jsonObject=JSONObject.parseObject(StringUtil.substringBetween(httpResult.getResponseHtml(), "(", ");"));
			JSONArray list = jsonObject.getJSONObject("data").getJSONArray("commentList");
			for(int j=0; j<list.size(); j++) {
				JSONObject msg = list.getJSONObject(j);
				int id = msg.getIntValue("id");
				String content = msg.getString("htmlContent");
				String writer = msg.getString("nickname");
				String date = msg.getString("pubtime");
				String qq = msg.getString("uin");
				
			     Element message = root.addElement("Message");
			     message.addAttribute("id", id+"");
			     message.addAttribute("nickname", writer);
			     message.addAttribute("qq", qq);
			     message.addAttribute("pubtime", date);

			     //处理图片
			     String imgurl = StringUtil.substringBetween(content, "src=\"", "\"");
			     if(StringUtil.isNotEmpty(imgurl)) {
			    	 String downurl = !imgurl.startsWith("http") ? "https://qzs.qq.com"+imgurl.trim() : imgurl.trim();
			    	 byte[] b= HttpUtil.getMethod(downurl,headerMap).getResponseByte();
			    	 FileUtils.newDirectory("C:\\Users\\Administrator\\Desktop\\messageboard\\images");
			    	 FileUtils.byte2File(b, new File("C:\\Users\\Administrator\\Desktop\\messageboard\\images\\"+id+"_0."+(downurl.endsWith(".gif")?"gif":"png"))); 
			    	 content=content.replace(imgurl, "/messageboard/images/"+id+"_0."+(downurl.endsWith(".gif")?"gif":"png"));
			     }
			     message.setText(content);
			    
				System.out.println("留言人："+writer);
				System.out.println("内容："+content);
				
				 Element ReplyList = message.addElement("ReplyList");
				JSONArray replyList = msg.getJSONArray("replyList");
				boolean lm = true;
				for(int k=0; k<replyList.size(); k++) {
					JSONObject kmsg = replyList.getJSONObject(k);
					String kcontent = kmsg.getString("content");
					String kwriter = kmsg.getString("nick");
					String kdate  = kmsg.getString("time");
					kdate = TimeUtil.getFullDateTime(Long.valueOf(kdate+"000"));
					if(kcontent.trim().equals("留")) {
						lm=false;
						continue;
					}
					 Element Reply = ReplyList.addElement("Reply");
					 Reply.addAttribute("nickname", kwriter);
					 Reply.addAttribute("qq", kmsg.getString("uin"));
					 Reply.addAttribute("pubtime", kdate);
					 Reply.setText(kcontent);
				}
				if(lm)     message.addAttribute("to", "liman");
			}
			
//			System.out.println(jsonObject);
		}
		
	     //用于格式化xml内容和设置头部标签
        OutputFormat format = OutputFormat.createPrettyPrint();
        //设置xml文档的编码为utf-8
        format.setEncoding("utf-8");
        Writer out;
        try {
            //创建一个输出流对象
            out = new FileWriter("C:\\Users\\Administrator\\Desktop\\messageboard\\MessageBoard.xml");
            //创建一个dom4j创建xml的对象
            XMLWriter writer = new XMLWriter(out, format);
            //调用write方法将doc文档写到指定路径
            writer.write(doc);
            writer.close();
            System.out.print("生成XML文件成功");
        } catch (IOException e) {
            System.out.print("生成XML文件失败");
            e.printStackTrace();
        }
	}
	*/
	public static void main(String[] args) throws IOException {
		org.jsoup.nodes.Document doc = Jsoup.parse(new File("C:\\Users\\Administrator\\Desktop\\messageboard\\MessageBoard.xml"), "utf-8");
		String t = doc.select("Message:eq(4)").html();
		System.out.println(t);
	}

}
