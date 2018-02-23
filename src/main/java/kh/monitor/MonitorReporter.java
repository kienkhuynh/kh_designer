package kh.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitorReporter {
	//@Autowired private GraphiteWriter writer;
	
	public void report(MetricKPI kpi, float value) {
		//writer.write(kpi, value);
	}
	
	public enum MetricKPI {
		SERVER_START,
		SERVER_STOP,
		DATABASE_STATUS
	}
}
