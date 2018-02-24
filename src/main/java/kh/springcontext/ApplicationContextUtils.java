package kh.springcontext;

import javax.servlet.ServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import kh.web.core.CustomerOrderManager;
import kh.web.core.InventoryManager;

/**
 * Utilities class for Spring bean retrieval.
 * 
 * @author kien
 */
public class ApplicationContextUtils {

	/**
	 * Retrieves authentication service bean
	 * 
	 * @param servletContext The servlet context
	 * @return authentication service
	 */
	public static AuthenticationService authService(ServletContext servletContext) {
		return bean(servletContext, AuthenticationService.class);
	}
	
	/**
	 * Retrieves Inventory Manager bean
	 * 
	 * @param servletContext The servlet context
	 * @return The inventory manager
	 */
	public static InventoryManager inventoryMgr(ServletContext servletContext) {
		return bean(servletContext, InventoryManager.class);
	}
	
	/**
	 * Retrieves Customer Order Manager for customer shopping carts management
	 * 
	 * @param servletContext The servlet context
	 * @return The customer order manager
	 */
	public static CustomerOrderManager customerOrderMgr(ServletContext servletContext) {
		return bean(servletContext, CustomerOrderManager.class);
	}

	/**
	 * Retrieves any spring beans
	 *
	 * @param servletContext
	 * @param clazz the bean type
	 * @return an instance of the specified bean type.
	 */
	public static <T> T bean(ServletContext servletContext, Class<T> clazz) {
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		return ctx.getBean(clazz);
	}
}
