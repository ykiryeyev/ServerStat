package com.kiryeyev.serverstat.monitor.heap;

import java.util.function.Consumer;

import com.kiryeyev.serverstat.monitor.StatRecord;

public final class HeapRecordStatistics implements StatRecord, Consumer<HeapRecord> {
	private long count;
	private long sumFree = 0;
	private long sumUsed = 0;
	private long minFree = Long.MAX_VALUE;
	private long maxFree = Long.MIN_VALUE;
	private long minUsed = Long.MAX_VALUE;
	private long maxUsed = Long.MIN_VALUE;
	final private long time;

	public HeapRecordStatistics(long time) {
		this.time = time;
	}

	@Override
	public void accept(HeapRecord t) {
		minFree = Math.min(minFree, t.getFreeSize());
		maxFree = Math.max(maxFree, t.getFreeSize());
		minUsed = Math.min(minUsed, t.getUsedSize());
		maxUsed = Math.max(maxUsed, t.getUsedSize());
		count ++;
		sumFree += t.getFreeSize();
		sumUsed = sumUsed + t.getUsedSize();
	}
	
	public double getAverageFree() {
		return sumFree / count;
	}

	public double getAverageUsed() {
		return sumUsed / count;
	}
	public long getMinFree() {
		return minFree;
	}

	public long getMaxFree() {
		return maxFree;
	}

	public long getMinUsed() {
		return minUsed;
	}

	public long getMaxUsed() {
		return maxUsed;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String getName() {
		return "Heap Statistics";
	}

	@Override
	public boolean isStatical() {
		return false;
	}

}