package per.sc.tool.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用webservice接口（asmx） <br>
 * <br>
 * webservice接口必须支持HTTP请求
 * 
 * @Date 2018年3月30日
 * @Author 舒超
 */
public class SoapUtil {

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(SoapUtil.class);
	// 页面编码
	private static String htmlCharset = "UTF-8";

	public static void main(String[] args) throws Exception {
//		sendRequest("http://122.224.25.226:9999/sxdx/SXDX/WebService/WebServiceLogin.asmx?op=Login",  "迎评办","12345678");
		sendRequestGetResult("http://122.224.25.226:9999/sxdx/SXDX/WebService/WebServiceLogin.asmx?op=Login","迎评办","12345678");
	}

	/**
	 * webservice接口请求
	 * <br> <br>
	 * 
	 * @param opUrl asmx接口地址（例：http://122.224.25.226:9999/sxdx/SXDX/WebService/WebServiceLogin.asmx?op=Login）
	 * @param params 对应的参数
	 * @return 接口请求响应内容
	 * @date 2018年3月31日
	 * @author 舒超
	 */
	public static String sendRequest(String opUrl, String... params) {
		// 抓取请求消息
		String requestXml = getReqXml(opUrl);
		// 填充参数到消息
		requestXml = String.format(requestXml, params);
		System.out.println("\n请求xml:\n" + requestXml);
		// 发送接口请求
		String responseXml = postMethodAsmx(opUrl, queryOp(opUrl), requestXml);
		// 返回响应结果
		responseXml = responseXml.replace("&lt;", "<").replace("&gt;", ">");
		System.out.println("\n响应xml:\n" + responseXml);
		return responseXml;
	}

	/**
	 * webservice接口请求 
	 * <br>
	 * <br>
	 * 
	 * @param opUrl
	 * @param params
	 * @return 截取接口响应内容中的 result部分内容
	 * @date 2018年3月31日
	 * @author 舒超
	 */
	public static String sendRequestGetResult(String opUrl, String... params) {
		String responseXml = sendRequest(opUrl, params);
		// 操作方法
		String opString = queryOp(opUrl);
		String result = StringUtils.substringBetween(responseXml, "<" + opString + "Result>", "</" + opString + "Result>");
		System.out.println("\n" + opString + "Result:\n" + result);
		return result;
	}

	/** 根据接口地址查询接口的操作方法 */
	private static String queryOp(String opUrl) {
		String op = null;
		// 先截取op前段
		if (opUrl.contains("?op=")) {
			op = opUrl.substring(opUrl.indexOf("?op=") + 4);
		} else if (opUrl.contains("&op=")) {
			op = opUrl.substring(opUrl.indexOf("&op=") + 4);
		} else {
			return op;
		}
		// 移除op后段
		if (op.contains("&")) {
			op = op.substring(0, op.indexOf("&"));
		}
		return op;
	}

	/**
	 * 抓取请求的所需的消息格式xml 
	 * 
	 * @param opUrl
	 * @return
	 * @date 2018年3月31日
	 * @author 舒超
	 */
	public static String getReqXml(String opUrl) {
		String html = "";
		try {
			html = getMethodAsmx(opUrl);
		} catch (Exception e) {
			logger.error("getReqXml",e);
		}
		html = html.replace("&lt;", "<").replace("&gt;", ">");
		if (html.contains("<soap:Envelope")&& html.contains("</soap:Envelope>")) {
			html = "<soap:Envelope"+ StringUtils.substringBetween(html, "<soap:Envelope","</soap:Envelope>") + "</soap:Envelope>";
		}
		while (html.contains("<font class=value>") && html.contains("</font>")) {
			html = StringUtils.substringBefore(html, "<font class=value>")+ "%s" + StringUtils.substringAfter(html, "</font>");
		}
		return html;
	}


	/**
	 * get方法,获取接口参数  
	 * 
	 * @param urlString 请求地址
	 * @return String
	 */
	private static String getMethodAsmx(String urlString) {
		InputStream in = null;
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(urlString);
			httpConn = (HttpURLConnection) url.openConnection();
			// 设置参数
			httpConn.setUseCaches(false); // 不允许缓存
			httpConn.setRequestMethod("GET"); // 设置GET方式连接
			// 设置请求属性
			httpConn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
			// 连接
			httpConn.connect();
			// 获得响应状态
			return toString(httpConn, htmlCharset);
		} catch (Exception e) {
			logger.error("getMethod异常", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				httpConn.disconnect();
			} catch (Exception e2) {
				logger.error("关闭流与连接异常", e2);
			}

		}
		return "发送失败，程序异常";

	}

	/**
	 * post方法,发送接口请求 
	 * 
	 * @param urlString 请求地址
	 * @param op  接口操方法
	 * @param param 参数
	 * @return String
	 */
	private static String postMethodAsmx(String urlString, String op,
			String param) {
		InputStream in = null;
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(urlString);
			httpConn = (HttpURLConnection) url.openConnection();
			// 设置参数
			byte[] buf = param.getBytes();
			httpConn.setDoOutput(true); // 需要输出
			httpConn.setDoInput(true); // 需要输入
			httpConn.setUseCaches(false); // 不允许缓存
			httpConn.setRequestMethod("POST"); // 设置POST方式连接
			// 设置请求属性
			httpConn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
			httpConn.setRequestProperty("Content-Length",String.valueOf(buf.length));
			httpConn.setRequestProperty("Content-Type",
					"text/xml; charset="+htmlCharset);
			httpConn.setRequestProperty("soapActionString", op);
			// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
			httpConn.connect();
			// 建立输入流，向指向的URL传入参数
			OutputStream out = httpConn.getOutputStream();
	        out.write(buf);
	        out.flush();
	        out.close();
			// 获得响应状态
			return toString(httpConn, htmlCharset);
		} catch (Exception e) {
			logger.error("postMethod异常", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				httpConn.disconnect();
			} catch (Exception e2) {
				logger.error("关闭流与连接异常", e2);
			}
		}
		return "发送失败，程序异常";
	}

	/**
	 * 流转字节  
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 * @date 2018年3月22日
	 * @author 舒超
	 */
	private static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int i = 0;
		while ((i = in.read(buffer)) != -1) {
			byteOutputStream.write(buffer, 0, i);
		}
		byteOutputStream.flush();
		byte[] b = byteOutputStream.toByteArray();
		byteOutputStream.close();
		return b;
	}
	
	/**
	 * 读取响应文本  
	 * 
	 * @param httpConn
	 * @return
	 * @date 2018年3月22日
	 * @author 舒超
	 */
	private static String toString(HttpURLConnection httpConn, String charset) {
		String responseText = "";
		try {
			InputStream in = httpConn.getInputStream();
			byte[] b = toByteArray(in);
			if (charset == null || charset.trim().equals("")) {
				responseText = new String(b);
			} else {
				responseText = new String(b, charset);
			}
		} catch (Exception e) {
			logger.error("toString方法异常", e);
		}
		return responseText.replace("&nbsp;", " ").replace("&nbsp", " ");
	}
}
