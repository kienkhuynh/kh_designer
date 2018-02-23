package kh.springcontext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import kh.web.core.InventoryRepository;

public class ApplicationContextUtils {

	public static AuthenticationService authService(HttpServletRequest request) {
		return bean(request, AuthenticationService.class);
	}
	public static InventoryRepository repo(HttpServletRequest request) {
		return bean(request, InventoryRepository.class);
	}
	public static InventoryRepository repo(ServletContext servlet) {
		return bean(servlet, InventoryRepository.class);
	}

	public static <T> T bean(ServletContext servlet, Class<T> t) {
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servlet);
		return ctx.getBean(t);
				
	}
	
	public static WebApplicationContext ctx(ServletContext servlet) {
		return WebApplicationContextUtils.getWebApplicationContext(servlet);
	}
	public static <T> T bean(HttpServletRequest request, Class<T> t) {
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		return ctx.getBean(t);
				
	}
	
	public static WebApplicationContext ctx(HttpServletRequest request) {
		return WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
	}

}
