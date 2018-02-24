package kh.springcontext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Registration of root spring bean configuration classes 
 * 
 * @author kien
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebApplicationContextInitializer implements WebApplicationInitializer {

	static Logger log = Logger.getLogger(WebApplicationContextInitializer.class);
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(AppConfig.class, SecurityConfig.class);
		try {
			rootContext.refresh();			
		} catch (Exception e){
			log.error(e);
		}
		servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.setInitParameter("defaultHtmlEscape", "true");
        
        // Quickly test if the configuration successfully taken.
        rootContext.getBean(SecurityConfig.class);
        rootContext.getBean(AppConfig.class);
        
        //InventoryRepository bean = ApplicationContextUtils.bean(servletContext, InventoryRepository.class);
        
        /*AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
        mvcContext.register(MvcConfigurations.class);
        mvcContext.refresh();*/

        /*ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "dispatcher", new DispatcherServlet(mvcContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/test/*");*/
	} 

}
