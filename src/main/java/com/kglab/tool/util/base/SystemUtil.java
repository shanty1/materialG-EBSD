package com.kglab.tool.util.base;

import java.io.File;

public class SystemUtil {

	/**
	 * 打印jdk版本和位数
	 * <br><br>
	 * 
	 * @date 2018年1月19日
	 * @author 舒超
	 */
	public static void printJDKEdition(){
		 System.out.println("JDK版本："+System.getProperty("java.version")+"-"+System.getProperty("sun.arch.data.model")+"位"); //查看jdk版本
	}
	
	/**
	 *  通过创建文件获取当前项目根目录（也可以获取Tomcat根目录）
	 * <br><br>
	 * 
	 * @return
	 * @date 2018年1月29日
	 * @author 舒超
	 */
	public static String getFileDir(){
		String tomcatDir = new File("").getAbsolutePath();
		if(tomcatDir.endsWith(File.separator + "bin")){
			tomcatDir = tomcatDir.substring(0,tomcatDir.lastIndexOf(File.separator + "bin"));
		}
		return tomcatDir;
	}
	
	/**
	 *   获取项目目录
	 * @author shuchao
	 * @data   2019年3月3日
	 * @return 例：K:\WorkSpace\taobao_proxy
	 */
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}
	
	public static void main(String[] args) {
		System.out.println(getUserDir());
	}
}
