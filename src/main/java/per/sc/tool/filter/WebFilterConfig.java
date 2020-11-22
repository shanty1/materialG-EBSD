package per.sc.tool.filter;
import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import per.sc.tool.filter.filters.CharacterEncodingFilter;


@Configuration
public class WebFilterConfig {
	@Bean
	public FilterRegistrationBean<Filter> filterDemo3Registration() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(CharacterEncodingFilter());
		registration.addUrlPatterns("/*");
		registration.setName("CharacterEncodingFilter");
		registration.setOrder(1);
		// 设置初始参数
		registration.addInitParameter("encoding", "UTF-8");
		registration.addInitParameter("encodingOn","false");
		registration.addInitParameter("trimOn", "true");
		return registration;
	}


	@Bean
	public Filter CharacterEncodingFilter() {
		return new CharacterEncodingFilter();
	}

}
