package com.kiryeyev.serverstat.monitor;

/**
 * Monitor periodically collects system load information and returns
 * consolidated statistics
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public interface StatMonitor {

	/**
	 * Collect monitoring information. This method periodically called by
	 * {@link MonitorThread}
	 */
	public void updateStat();

	/**
	 * Returns consolidated statistics gathered during specified previous period
	 * 
	 * @param time
	 *            - a time in millisecond to get consolidated statistics
	 * @return {@link StatRecord} with gather consolidated statistics
	 */
	StatRecord getStatistic(long time);

	/**
	 * Starts sampling thread
	 */
	void start();
}
