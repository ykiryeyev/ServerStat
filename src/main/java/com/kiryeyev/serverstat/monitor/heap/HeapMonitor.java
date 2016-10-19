package com.kiryeyev.serverstat.monitor.heap;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import com.kiryeyev.serverstat.monitor.MonitorThread;
import com.kiryeyev.serverstat.monitor.StatMonitor;

/**
 * Monitor to collect heap usage statistics
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public class HeapMonitor implements StatMonitor {
	private static final long MONITOR_DELAY = 100l;
	private static final long HISTORY_SIZE = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES) / MONITOR_DELAY;
	private ConcurrentLinkedQueue<HeapRecord> statLog = new ConcurrentLinkedQueue<>();
	private Thread monitorThread;

	/**
	 * Constructs {@link HeapMonitor} object 
	 * 
	 */
	public HeapMonitor() {
//		start();
	}

	
	/**
	 * Starts sampling thread
	 */
	@Override
	public void start() {
		if (monitorThread != null) {
			monitorThread = new Thread(new MonitorThread(this, MONITOR_DELAY), "HeapMonitor");
			monitorThread.setDaemon(true);
			monitorThread.start();
		}
	}

	@Override
	public HeapRecordStatistics getStatistic(long time) {
		HeapRecordStatistics statistics = statLog.stream().limit(time / MONITOR_DELAY).collect(
				() -> new HeapRecordStatistics(time), (r, t) -> r.accept((HeapRecord) t), (l, r) -> l.andThen(r));
		return statistics;
	}

	@Override
	public void updateStat() {
		statLog.add(currentState());
		if (statLog.size() > HISTORY_SIZE) {
			statLog.poll();
		}
	}

	HeapRecord currentState() {
		long freeMemory = Runtime.getRuntime().freeMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		return new HeapRecord((totalMemory - freeMemory) / 1024l, freeMemory / 1024l);
	}

}
