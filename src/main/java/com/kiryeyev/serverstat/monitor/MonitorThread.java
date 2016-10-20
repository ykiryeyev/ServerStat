package com.kiryeyev.serverstat.monitor;

import java.util.concurrent.TimeUnit;

/**
 * Thread to gather statistics for a sampling monitors
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public class MonitorThread implements Runnable {

	private StatMonitor aggregator;
	private long delay;

	/**
	 * Construct runnable to gather statistic with {@link StatMonitor}
	 * 
	 * @param aggregator
	 *            - a {@link StatMonitor} to collect information by
	 * @param delay
	 *            - a delay between information sampling
	 */
	public MonitorThread(StatMonitor aggregator, long delay) {
		this.aggregator = aggregator;
		this.delay = delay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			aggregator.updateStat();
			try {
				TimeUnit.MILLISECONDS.sleep(delay);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				;
			}
		}

	}
}