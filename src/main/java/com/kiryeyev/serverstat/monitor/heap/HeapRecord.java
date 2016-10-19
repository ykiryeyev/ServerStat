package com.kiryeyev.serverstat.monitor.heap;

import com.kiryeyev.serverstat.monitor.HistoryRecord;

public class HeapRecord implements HistoryRecord {

	final private long usedSize;
	final private long freeSize;
	final private long timestamp;

	public HeapRecord(long heapUssage, long freeHeapSize) {
		this.usedSize = heapUssage;
		this.freeSize = freeHeapSize;
		this.timestamp = System.currentTimeMillis();
	}

	public long getUsedSize() {
		return usedSize;
	}
	
	public long getFreeSize() {
		return freeSize;
	}
	
	@Override
	public String getName() {
		return "Heap Info";
	}

	public String toString() {
		StringBuilder result = new StringBuilder(getName());
		result.append(" Used: ").append(usedSize);
		result.append(" Free: ").append(freeSize);
		return result.toString(); 
	}

	public long getTimestamp() {
		return timestamp;
	}
}