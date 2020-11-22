package per.sc.tool.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DemoAop {

	private static Logger logger = LoggerFactory.getLogger(DemoAop.class);
	
	@Pointcut("execution(public * per.sc.tool.control.common.MainsiteErrorController.handleError(..))")
	public void pointcut() { }
	
	@Before("pointcut()")
	public void doBefore() {
		// TODO Auto-generated method stub
		System.out.println("handleError切点");
	}
	
	@AfterReturning("pointcut()")
	public void doAfterReturning() {
		// TODO Auto-generated method stub
	}
}
