package com.kiryeyev.serverstat.monitor.heap;

import com.kiryeyev.serverstat.monitor.HistoryRecord;

/**
 * HeapRecord contains JVM heap usage information at some point in time.
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public class HeapRecord implements HistoryRecord {

	final private long usedSize;
	final private long freeSize;
	final private long timestamp;

	/**
	 * Create object with specified heap usage data at current moment
	 * 
	 * @param usedHeapSize
	 *            - size of used Heap
	 * @param freeHeapSize
	 *            - size of free Heap
	 */
	public HeapRecord(long usedHeapSize, long freeHeapSize) {
		this.usedSize = usedHeapSize;
		this.freeSize = freeHeapSize;
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * Returns size of used Heap
	 * 
	 */
	public long getUsedSize() {
		return usedSize;
	}

	/**
	 * Returns size of free Heap
	 */
	public long getFreeSize() {
		return freeSize;
	}

	/**
	 * Returns timestamp of collected information
	 */
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String getName() {
		return "Heap Info";
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(getName());
		result.append(" Used: ").append(usedSize);
		result.append(" Free: ").append(freeSize);
		return result.toString();
	}

}