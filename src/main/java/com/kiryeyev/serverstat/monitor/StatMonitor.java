package com.kiryeyev.serverstat.monitor;

/**
 * Interface for all system monitors
 * @author Yevgen_Kiryeyev
 *
 */
public interface StatMonitor {

	
	/**
	 * Collect information. This method periodically called by {@link MonitorThread} 
	 */
	public void updateStat();

	/**
	 * Returns statistics gathered during specified previous period
	 * @param time - a time in millisecond to get consolidated statistics
	 * @return {@link StatRecord} with gather consolidated statistics
	 */
	StatRecord getStatistic(long time);

	/**
	 * Starts sampling thread
	 */
	void start();
}
