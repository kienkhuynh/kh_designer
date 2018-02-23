package kh.web.core;
  
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryCoordinator {
	//private static Logger logger = Logger.getLogger(RepositoryCoordinator.class);
    
	@Autowired
	public SystemConfiguration systemConfig;
	
    public RepositoryCoordinator() {
    	
	}
	

}
