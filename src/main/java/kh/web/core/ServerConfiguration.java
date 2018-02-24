package kh.web.core;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ServerConfiguration {
	
	Logger log = Logger.getLogger(ServerConfiguration.class);
	
	public final String databaseUrl, dbUser, dbPassword, schema, driver;
	
	public final String monitorServer;
	public final int monitorPort;
	
	public ServerConfiguration() {
		databaseUrl = System.getProperty("databaseUrl");
		dbUser = System.getProperty("dbUser");
		dbPassword = System.getProperty("dbPassword");
		schema =  System.getProperty("schema");
		driver = System.getProperty("driver");
		
		monitorServer = System.getProperty("monitorServer").trim();
		monitorPort = Integer.parseInt(System.getProperty("monitorPort"));
		
		log.info("End of System configuration");
	}
}
