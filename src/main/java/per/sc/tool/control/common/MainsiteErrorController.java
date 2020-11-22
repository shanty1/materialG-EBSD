package per.sc.tool.control.common;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import per.sc.tool.control.BaseAction;
import per.sc.tool.handler.exception.result.ResultVoMassage;

@Controller
public class MainsiteErrorController extends BaseAction implements ErrorController {

	private static final String ERROR_PATH = "/**/error";

	@RequestMapping(value = ERROR_PATH)
	public String handleError() {
		throw new ResultVoMassage(FAIL, "禁止访问");
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}