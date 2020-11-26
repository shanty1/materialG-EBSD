package com.kglab;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglab.mg.repositor.GrainRepository;
import com.kglab.tool.control.BaseAction;
import com.kglab.tool.springbean.SpringContextHolder;
import com.kglab.tool.util.internet.IPUtil;

@SpringBootApplication
@ComponentScan("com.kglab")
@RequestMapping("/")
public class RunApplication extends BaseAction{
	@Autowired GrainRepository grainRepository;
	public static void main(String[] args){
		ApplicationContext applicationContext = SpringApplication.run(RunApplication.class, args);
		// 濡傛灉鏈敞鍏pring ApplicationContext锛屽垯浣跨敤鑷畾涔夋柟娉曚繚瀛�
		try {
			SpringContextHolder.getApplicationContext();
		} catch (Exception e) {
			SpringContextHolder.setMyApplicationContext(applicationContext);
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value={"/","/index"})
	@ResponseBody
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println(grainRepository.count());
//		JdbcTemplate defaultJdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
//		response.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html;charset=UTF-8");
//		PrintWriter pw = new PrintWriter(response.getOutputStream(), true);
//		pw.println("璁惧锛�" + request.getHeader("User-Agent")+"<br><br>");
//		pw.println("鏈満缃戠粶锛�" + IPUtil.getIpAddress(request)+"<br><br>");
//		try {
//			pw.println( "杩炴帴鍒版暟鎹簱锛�" + defaultJdbcTemplate.queryForObject("select database()", String.class));
//		} catch (Exception e) {
//			pw.println( "杩炴帴鍒版暟鎹簱锛�<font style='color:red'>澶辫触</font>" );
//		}
//		pw.println( "<br><br>------------搴旂敤鍚姩鎴愬姛------------<br><br>");
		return null;
	}
}
