package com.kglab.tool.filter.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kglab.tool.wrapper.MyHttpServletRequestWrapper;

public class CharacterEncodingFilter implements Filter {
	protected static Logger log = LoggerFactory.getLogger(CharacterEncodingFilter.class);
	private String encoding = "UTF-8";
	private boolean encodingOn = false;
	private boolean trimOn = true;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException,
			ServletException {
		if (req instanceof MyHttpServletRequestWrapper) {
			filterChain.doFilter(req, res);
			return;
		}

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy server

		String suri = request.getRequestURI().toLowerCase();
//		if (suri.endsWith(".do")) {
			String content_type = request.getContentType();
			if (content_type == null || content_type.toLowerCase().indexOf("multipart/form-data") == -1) {
				request = new MyHttpServletRequestWrapper(request, encoding, encodingOn, trimOn);
			}
//		}
		if (suri.endsWith(".manifest")) {
			response.setContentType("text/cache-manifest");
		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("编码过滤器初始化");
		String e = filterConfig.getInitParameter("encoding");
		if (e != null && !"".equals(e.trim())) {
			encoding = e;
		}
		String eo = filterConfig.getInitParameter("encodingOn");
		if (eo != null) {
			encodingOn = Boolean.parseBoolean(eo);
		}
		String to = filterConfig.getInitParameter("trimOn");
		if (to != null) {
			trimOn = Boolean.parseBoolean(to);
		}
	}

}
