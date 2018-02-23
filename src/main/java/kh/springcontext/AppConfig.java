package kh.springcontext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import kh.web.core.RepositoryCoordinator;
 
@Configuration 
@ComponentScan({"kh.*" })
@Import({ SecurityConfig.class })
public class AppConfig {

	@Autowired
	RepositoryCoordinator serviceConfiguration;
	
	
	public RepositoryCoordinator serviceConfiguration() {
		return serviceConfiguration;
	}
}