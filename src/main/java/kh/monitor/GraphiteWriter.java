package kh.monitor;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kh.monitor.MonitorReporter.MetricKPI;
import kh.web.core.SystemConfiguration;


@Component
public class GraphiteWriter {
	Logger log =  Logger.getLogger(GraphiteWriter.class.getName());
	
	@Autowired
	private SystemConfiguration config;
	
	private static ConcurrentLinkedQueue<String> METRIC_QUEUE;
	
	public GraphiteWriter() {
	}
	
	@PostConstruct
	private void run() {
		METRIC_QUEUE = new ConcurrentLinkedQueue<String>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				SocketWriter sw = null;
				while (true) {
					String metrics = null;
					int metricCount = Math.min(20, METRIC_QUEUE.size());
					for (int i = 0; i < metricCount; i++) {
						if (metrics == null) {
							metrics = METRIC_QUEUE.poll();
						} else {
							metrics = metrics + "\n" + METRIC_QUEUE.poll();
						}
					}
					sw = sendMetrics(metrics, sw);
					
					try {
	                    Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
				}
			}
		}).start();
	}

	public void write(MetricKPI metricKPI, float value) {
		StringBuilder sb = new StringBuilder();
		sb.append("test.").append(metricKPI.name().toLowerCase()).append(" ").
			append(value).append(" ").append(System.currentTimeMillis() / 1000);
		METRIC_QUEUE.add(sb.toString());
	}
	
	private SocketWriter sendMetrics(String metrics, SocketWriter sw) {
		try {
			sw = validate(sw);
			if (sw != null) {
				Writer writer = sw.writer;
				writer.write(metrics + "\n");
				writer.flush();
				return sw;
			} else {
				return sw;
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
 
	private SocketWriter validate(SocketWriter sw) {
		if (sw != null) {
			Socket socket = sw.socket;
			if (socket.isConnected() && socket.isBound()
                && !socket.isClosed() && !socket.isInputShutdown()
                && !socket.isOutputShutdown()) {
				return sw;
			} else {
				destroy(sw);
			}
		}
		try {
			Socket socket = new Socket();
    		socket.connect(new InetSocketAddress(config.monitorServer, config.monitorPort), 10000);
    		Writer writer = new OutputStreamWriter(socket.getOutputStream());
    		return new SocketWriter(socket, writer);
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
	private void destroy(SocketWriter sw) {
		if (sw != null) {
			try {
				sw.socket.shutdownInput();
			} catch (IOException e) {
			}
			try {
				sw.socket.close();
			} catch (IOException e) {
			}
			try {
				sw.writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static class SocketWriter {
		Socket socket;
		Writer writer;
		public SocketWriter(Socket s, Writer w) {
			socket = s;
			writer = w;
		}
	}
} 