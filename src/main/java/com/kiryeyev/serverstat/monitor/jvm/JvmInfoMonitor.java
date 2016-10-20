package com.kiryeyev.serverstat.monitor.jvm;

import java.util.Properties;

import com.kiryeyev.serverstat.monitor.StatMonitor;

/**
 * Monitor to collect JVM system information
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public class JvmInfoMonitor implements StatMonitor {

	private Properties systemProps;

	/**
	 * Create monitor object
	 */
	public JvmInfoMonitor() {
		systemProps = System.getProperties();
	}

	@Override
	public void updateStat() {
		// do nothing
	}

	@Override
	public JvmStatistic getStatistic(long time) {
		return new JvmStatistic(systemProps);
	}

	@Override
	public void start() {
		systemProps = System.getProperties();
	}

}
