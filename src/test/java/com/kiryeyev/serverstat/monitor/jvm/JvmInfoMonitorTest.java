package com.kiryeyev.serverstat.monitor.jvm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class JvmInfoMonitorTest {

	@Test
	public void correctlyGotSystemInfo() {
		JvmInfoMonitor monitor = new JvmInfoMonitor();
		JvmStatistic statistic = monitor.getStatistic(0);
		assertNotNull(statistic);
		Map<String, String> statProperties = statistic.getSystemProperties();
		for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
			assertEquals(entry.getValue(), statProperties.get(entry.getKey()));
		}
	}

}
