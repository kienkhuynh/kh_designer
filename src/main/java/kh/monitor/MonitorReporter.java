package kh.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kh.web.core.ServerConfiguration;

/*
 * Reports server metric KPIs
 */
@Component
public class MonitorReporter {
	
	@Autowired ServerConfiguration config;
	
	/**
	 * An instance of the writer to write metrics to monitoring server.
	 */
	private GraphiteWriter writer;
	 
	public void report(MetricKPI kpi, float value) {
		// Create an instance of Graphite Writer on demand. 
		if (writer == null) {
			synchronized(this) {
				if (writer == null) {
					writer = new GraphiteWriter(config);
					writer.init();
				}
			}
		}
		writer.write(kpi, value);
	}
	
	public enum MetricKPI {
		SERVER_START,
		SERVER_STOP,
		DATABASE_STATUS
	}
}
