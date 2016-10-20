package com.kiryeyev.serverstat.monitor.heap;

import com.kiryeyev.serverstat.monitor.StatRecord;

/**
 * Stores heap usage statistics
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public final class HeapStatistics implements StatRecord {
	enum Type {
		Free, Used
	};

	final private long min;
	final private long max;
	final private long average;
	final private long time;
	final private Type type;

	/**
	 * Constructs object instance with specified heap usage
	 * 
	 * @param type
	 *            - {@link Type} of heap usage
	 * @param time
	 *            - time period of consolidation in milliseconds
	 * @param min
	 *            - minimal value during time period
	 * @param max
	 *            - maximal value during time period
	 * @param average
	 *            - average value during time period
	 */
	public HeapStatistics(Type type, long time, long min, long max, long average) {
		this.type = type;
		this.time = time;
		this.min = min;
		this.max = max;
		this.average = average;
	}

	/**
	 * Return average value during time period
	 */
	public double getAverage() {
		return average;
	}

	/**
	 * Return minimal value during time period
	 */
	public long getMin() {
		return min;
	}

	/**
	 * maximal value during time period
	 */
	public long getMax() {
		return max;
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public String getName() {
		return type.toString() + " Heap Statistics";
	}

	@Override
	public boolean isStatical() {
		return false;
	}

	/**
	 * Return {@link Type} of heap usage
	 */
	public Type getType() {
		return type;
	}

}