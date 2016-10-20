package com.kiryeyev.serverstat.monitor.jvm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.kiryeyev.serverstat.monitor.StatRecord;

/**
 * JvmStatistic contains JVM System information
 * 
 * @see {@link System.getProperties()}
 * @author Yevgen_Kiryeyev
 *
 */
public class JvmStatistic implements StatRecord {
	private HashMap<String, String> systemProps;

	/**
	 * Create object instance with specified system properties values
	 * 
	 * @param props
	 *            - a system properties values
	 */
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

	/**
	 * Returns collected System properties as Map<String,String>
	 * 
	 * @see {@link System.getProperties()}
	 */
	public Map<String, String> getSystemProperties() {
		return systemProps;
	}

	@Override
	public long getTime() {
		return 0;
	}
}
