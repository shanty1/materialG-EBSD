package per.sc.tool.handler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import per.sc.tool.constant.StatusCode;
import per.sc.tool.entity.vo.ResultVo;
import per.sc.tool.handler.exception.result.ResultVoMassage;

/**
 * 全局异常处理类
 * @author shuchao
 * @date   2019年2月3日
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * 异常状态码
	 */
	public static final int DEFAULT = StatusCode.SYSTEM_ERROR;
	
	/**
	 * 捕获所有抛出的Exception，进行下面处理
	 * @param request
	 * @param Exception
	 * @return
	 */
	@ExceptionHandler(value=Exception.class) 
	@ResponseBody
	private ResultVo exceptionHandler(HttpServletRequest request, Exception e) {
		ResultVo rv = new ResultVo();
		rv.setErrorNo(DEFAULT);
//		rv.setErrorMsg(e.getMessage());
		rv.setErrorMsg("系统全局异常");
		if(! (e instanceof ResultVoMassage))
			logger.error("全局拦截异常", e);
		return rv;
	}
	
	
	/**
	 *  响应信息输出
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value=ResultVoMassage.class) 
	@ResponseBody
	private ResultVo exceptionHandler2(ResultVoMassage e) {
		ResultVo rv = new ResultVo();
		rv.setErrorNo(e.getErrorNo());
		rv.setErrorMsg(e.getErrorMsg());
		rv.setData(e.getData());
		return rv;
	}
}
