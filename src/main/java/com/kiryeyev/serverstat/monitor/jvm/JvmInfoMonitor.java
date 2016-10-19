package com.kiryeyev.serverstat.monitor.jvm;

import java.util.Properties;

import com.kiryeyev.serverstat.monitor.StatMonitor;

public class JvmInfoMonitor implements StatMonitor {
	
	private Properties systemProps;

	public JvmInfoMonitor() {
		systemProps = System.getProperties();
	}

	@Override
	public void updateStat() {
		// TODO Auto-generated method stub

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
