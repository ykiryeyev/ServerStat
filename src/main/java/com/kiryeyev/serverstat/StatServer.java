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
import com.kiryeyev.serverstat.monitor.heap.HeapMonitor;
import com.kiryeyev.serverstat.monitor.heap.HeapRecordStatistics;
import com.kiryeyev.serverstat.monitor.jvm.JvmInfoMonitor;
import com.kiryeyev.serverstat.monitor.jvm.JvmStatistic;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

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

class StatHandler implements HttpHandler {
	private StatMonitor aggregator = new HeapMonitor();
	private JvmInfoMonitor jvmMonitor = new JvmInfoMonitor();
	private Map<Class<?>, StatRenderer> renderers = new HashMap<>();

	public StatHandler() {
		renderers.put(HeapRecordStatistics.class, (r) -> formatStatistics((HeapRecordStatistics) r));
		renderers.put(JvmStatistic.class, (r) -> formatSystemProps((JvmStatistic) r));
	}

	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, 0);

			OutputStream responseBody = exchange.getResponseBody();
			StringBuilder response = new StringBuilder("WebServer Statistics");
			response.append("\n");

			StatRecord statistics;
			statistics = jvmMonitor.getStatistic(0);
			response.append(formatStatistics(statistics));
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS));
			response.append(formatStatistics(statistics));
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
			response.append(formatStatistics(statistics));
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
			response.append(formatStatistics(statistics));
			statistics = aggregator.getStatistic(TimeUnit.MILLISECONDS.convert(3, TimeUnit.MINUTES));
			response.append(formatStatistics(statistics));

			responseBody.write(response.toString().getBytes());
			responseBody.close();
		}
	}

	private String formatStatistics(StatRecord record) {
		StatRenderer renderer = renderers.get(record.getClass());
		return renderer.render(record);
	}

	private String formatStatistics(HeapRecordStatistics statistics) {
		StringBuilder result = new StringBuilder("Heap Statistics for ")
				.append(TimeUnit.SECONDS.convert(statistics.getTime(), TimeUnit.MILLISECONDS));
		result.append("\n");
		result.append("Min Free Heap: ").append(statistics.getMinFree()).append(" Max Free Heap: ")
				.append(statistics.getMaxFree()).append(" Average Free Heap: ").append(statistics.getAverageFree());
		result.append("\n");
		result.append("Min Used Heap: ").append(statistics.getMinUsed()).append(" Max Used Heap: ")
				.append(statistics.getMaxUsed()).append(" Average Used Heap: ").append(statistics.getAverageUsed());
		result.append("\n");
		return result.toString();
	}
	
	private String formatSystemProps( JvmStatistic systemStat) {
		StringBuilder result = new StringBuilder("JVM System Info");
		result.append("\n");
		String props = systemStat.getSystemProperties().entrySet().stream()
			.map((entry) -> entry.getKey()+": "+entry.getValue())
			.collect(Collectors.joining("\n"));
		result.append(props);
		return result.toString();
	}
}