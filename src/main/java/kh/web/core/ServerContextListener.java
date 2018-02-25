package kh.web.core;
  
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServerContextListener implements  javax.servlet.ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent context) {
		System.out.println("ServletContextListener destroyed");
		
	}

        //Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent context) {
		System.out.println("ServletContextListener initialized");
	}
}
