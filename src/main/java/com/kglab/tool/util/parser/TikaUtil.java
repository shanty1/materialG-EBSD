package com.kglab.tool.util.parser;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
    
public class TikaUtil{
	 public static MimeTypes allTypes;
	 static{
		 allTypes = MimeTypes.getDefaultMimeTypes(); 
	 }
	
	 /**
	  * 通过tika的api，根据Content-Type查询文件后缀名
	  * <br><br>
	  * 
	  * @return
	  * @date 2018年5月11日
	  * @author 舒超
	  */
	private static String getFileExt(String contentType){
		String ext = "";
		try {
			MimeType mimeType = allTypes.forName(contentType);  
			ext = mimeType.getExtension(); // .jpg  
			if(ext!=null && ext.startsWith(".")){
				ext = ext.substring(1);
			}
		} catch (Exception e) {
		}
		return ext;
	}
	
	/**
	 * 判断网络资源文件后缀名
	 * <br><br>
	 * 主要是判断后缀名
	 * @return
	 * @date 2018年5月11日
	 */
	public static String getFileSuffix(byte[] b){
		try {
			return getFileExt(new Tika().detect(b));
		} catch (Exception e) {
			return "";
		}
	}
	public static String getFileSuffix(File b){
		try {
			return getFileExt(new Tika().detect(b));
		} catch (Exception e) {
			return "";
		}
	}
	public static String getFileSuffix(URL b){
		try {
			return getFileExt(new Tika().detect(b));
		} catch (Exception e) {
			return "";
		}
	}
	/**
	  * 通过流识别文件类型 ，尽量不要使用此方法，可能导致后续流传输缺失
	 * @author shuchao
	 * @data   2019年3月7日
	 * @param b
	 * @return
	 */
	@Deprecated
	public static String getFileSuffix(InputStream b){
		try {
			return getFileExt(new Tika().detect(b));
		} catch (Exception e) {
			return "";
		}
	}
	public static String getFileSuffix(String url){
		try {
			return getFileExt(new Tika().detect(new URL(url)));
		} catch (Exception e) {
			return getFileExt(new Tika().detect(url));
		}
	}
	public static void main(String[] args) throws  Exception {
		String name = "";
		URL u =new URL("https://www.baidu.com/link?url=1sM3kSl5jKy3AQOV7txisBBkhtX-j_8UaS6WgJobDO5l6y_WAdzbwHiijgDuqPhnNTG8GaQo--irsmtooqHE24VXhAetp2ToFqkmUI3Z81u&wd=&eqid=ce0337c80002d1e5000000025c7e34cb");
		System.out.println(u.getFile());
		System.out.println(getFileSuffix(u));
	}
}  