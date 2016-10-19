package com.kiryeyev.serverstat.monitor.jvm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.kiryeyev.serverstat.monitor.StatRecord;

public class JvmStatistic implements StatRecord {
	private HashMap<String, String> systemProps;

	public JvmStatistic(Properties props) {
		this.systemProps = new HashMap<>();
		for (Entry<Object, Object> entry : props.entrySet()) {
			this.systemProps.put((String) entry.getKey(), (String) entry.getValue());
		}
	}

	@Override
	public String getName() {
		return "JVM System Information";
	}

	@Override
	public boolean isStatical() {
		return true;
	}

	public Map<String, String> getSystemProperties() {
		return systemProps;
	}
}
