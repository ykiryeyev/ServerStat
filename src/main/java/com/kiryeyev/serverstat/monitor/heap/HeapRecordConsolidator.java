package com.kiryeyev.serverstat.monitor.heap;

import java.util.function.Consumer;

/**
 * Calculates min, max and average values of free and used heap size during a
 * time period
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public final class HeapRecordConsolidator implements Consumer<HeapRecord> {
	private long count;
	private long sumFree = 0;
	private long sumUsed = 0;
	private long minFree = Long.MAX_VALUE;
	private long maxFree = Long.MIN_VALUE;
	private long minUsed = Long.MAX_VALUE;
	private long maxUsed = Long.MIN_VALUE;
	final private long time;

	/**
	 * Create object instance to calculate statistics for specified time period
	 * 
	 * @param time
	 *            - time period in milliseconds
	 */
	public HeapRecordConsolidator(long time) {
		this.time = time;
	}

	@Override
	public void accept(HeapRecord t) {
		minFree = Math.min(minFree, t.getFreeSize());
		maxFree = Math.max(maxFree, t.getFreeSize());
		minUsed = Math.min(minUsed, t.getUsedSize());
		maxUsed = Math.max(maxUsed, t.getUsedSize());
		count++;
		sumFree += t.getFreeSize();
		sumUsed = sumUsed + t.getUsedSize();
	}

	private long getAverageFree() {
		return sumFree / count;
	}

	private long getAverageUsed() {
		return sumUsed / count;
	}

	/**
	 * Returns calculated statistics as @{link CompositeStatistics} with two
	 * children for free and used heap size
	 */
	public CompositeStatistics getStat() {
		CompositeStatistics heapStat = new CompositeStatistics("Heap Statistics", time, false);
		heapStat.addChild(new HeapStatistics(HeapStatistics.Type.Free, time, minFree, maxFree, getAverageFree()));
		heapStat.addChild(new HeapStatistics(HeapStatistics.Type.Used, time, minUsed, maxUsed, getAverageUsed()));
		return heapStat;
	}
}