package com.kiryeyev.serverstat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.kiryeyev.serverstat.monitor.StatMonitor;
import com.kiryeyev.serverstat.monitor.StatRecord;
import com.kiryeyev.serverstat.monitor.heap.CompositeStatistics;
import com.kiryeyev.serverstat.monitor.heap.HeapMonitor;
import com.kiryeyev.serverstat.monitor.heap.HeapStatistics;
import com.kiryeyev.serverstat.monitor.jvm.JvmInfoMonitor;
import com.kiryeyev.serverstat.monitor.jvm.JvmStatistic;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Web server to show JVM load statistics
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public class StatServer {
	public static void main(String[] args) throws IOException {
		InetSocketAddress addr = new InetSocketAddress(8080);
		HttpServer server = HttpServer.create(addr, 0);

		server.createContext("/", new StatHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server is listening on port 8080");
	}
}

/**
 * Responds to statistics request and outputs all gathered statistics in plain
 * text format.
 * 
 * @author Yevgen_Kiryeyev
 *
 */
class StatHandler implements HttpHandler {
	private StatMonitor aggregator = new HeapMonitor();
	private JvmInfoMonitor jvmMonitor = new JvmInfoMonitor();
	private Map<Class<?>, StatRenderer> renderers = new HashMap<>();

	/**
	 * Create object instance and starts all statistics monitor
	 */
	public StatHandler() {
		renderers.put(CompositeStatistics.class, (r) -> formatCompositeStatistics((CompositeStatistics) r));
		renderers.put(JvmStatistic.class, (r) -> formatSystemProps((JvmStatistic) r));
		renderers.put(HeapStatistics.class, (r) -> formatHeapStatistics((HeapStatistics) r));
		aggregator.start();
		jvmMonitor.start();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, 0);

			OutputStream responseBody = exchange.getResponseBody();
			StringBuilder response = new StringBuilder("WebServer Statistics");
			response.append("\n");
			response.append("\n");

			StatRecord statistics;
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS));
			response.append(formatStatistics(statistics));
			response.append("\n");
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
			response.append(formatStatistics(statistics));
			response.append("\n");
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
			response.append(formatStatistics(statistics));
			response.append("\n");
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(3, TimeUnit.MINUTES));
			response.append(formatStatistics(statistics));
			response.append("\n");

			statistics = jvmMonitor.getStatistic(0);
			response.append(formatStatistics(statistics));
			response.append("\n");

			responseBody.write(response.toString().getBytes());
			responseBody.close();
		}
	}

	private String formatStatistics(StatRecord record) {
		StatRenderer renderer = renderers.get(record.getClass());
		return renderer.render(record);
	}

	private String formatSystemProps(JvmStatistic systemStat) {
		StringBuilder result = new StringBuilder("JVM System Info");
		result.append("\n");
		result.append("\n");
		String props = systemStat.getSystemProperties().entrySet().stream()
				.map((entry) -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n"));
		result.append(props);
		result.append("\n");
		return result.toString();
	}

	private String formatCompositeStatistics(CompositeStatistics r) {
		StringBuilder result = new StringBuilder(r.getName());
		result.append(" For last ").append(TimeUnit.SECONDS.convert(r.getTime(), TimeUnit.MILLISECONDS))
				.append(" seconds");
		result.append("\n");
		result.append("\n");
		String childrenStat = r.getChildren().stream().map((child) -> formatStatistics(child))
				.collect(Collectors.joining("\n"));
		result.append(childrenStat);
		result.append("\n");
		return result.toString();
	}

	private String formatHeapStatistics(HeapStatistics r) {
		StringBuilder result = new StringBuilder();
		result.append("Min ").append(r.getName()).append(": ").append(r.getMin());
		result.append(" Max ").append(r.getName()).append(": ").append(r.getMax());
		result.append(" Average ").append(r.getName()).append(": ").append(r.getAverage());

		return result.toString();
	}

}