package com.kiryeyev.serverstat.monitor.heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kiryeyev.serverstat.monitor.StatRecord;

/**
 * CompositeStatistics stores a group of StatRecords which are consolidated for
 * a same time period and are gathered by a same monitor
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public class CompositeStatistics implements StatRecord {

	private String name;
	private boolean isStatic;
	private List<StatRecord> children = new ArrayList<>();
	private long time;

	/**
	 * Create an object instance with specified name and specified static status
	 * 
	 * @param name
	 *            - a name of statistical information
	 * @param isStatic
	 *            - is information static
	 */
	public CompositeStatistics(String name, long time, boolean isStatic) {
		this.name = name;
		this.time = time;
		this.isStatic = isStatic;
	}

	/**
	 * Add a child {@link StatRecord} to group
	 * 
	 * @param child
	 *            - {@link StatRecord} to be added
	 */
	public void addChild(StatRecord child) {
		children.add(child);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isStatical() {
		return isStatic;
	}

	/**
	 * Returns list of child {@link StatRecord}
	 */
	public List<StatRecord> getChildren() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public long getTime() {
		return time;
	}

}
