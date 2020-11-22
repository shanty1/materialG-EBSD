package per.sc;


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

import per.sc.mg.repositor.GrainRepository;
import per.sc.tool.control.BaseAction;
import per.sc.tool.springbean.SpringContextHolder;
import per.sc.tool.util.internet.IPUtil;

@SpringBootApplication
@ComponentScan("per.sc")
@RequestMapping("/")
public class RunApplication extends BaseAction{
	@Autowired GrainRepository grainRepository;
	public static void main(String[] args){
		ApplicationContext applicationContext = SpringApplication.run(RunApplication.class, args);
		// 如果未注入spring ApplicationContext，则使用自定义方法保存
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
//		pw.println("设备：" + request.getHeader("User-Agent")+"<br><br>");
//		pw.println("本机网络：" + IPUtil.getIpAddress(request)+"<br><br>");
//		try {
//			pw.println( "连接到数据库：" + defaultJdbcTemplate.queryForObject("select database()", String.class));
//		} catch (Exception e) {
//			pw.println( "连接到数据库：<font style='color:red'>失败</font>" );
//		}
//		pw.println( "<br><br>------------应用启动成功------------<br><br>");
		return null;
	}
}
