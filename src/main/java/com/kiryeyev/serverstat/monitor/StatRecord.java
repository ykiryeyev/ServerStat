package com.kiryeyev.serverstat.monitor;

/**
 * StatRecord stores information consolidated for some time period
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public interface StatRecord {

	/**
	 * @return name of statistical record
	 */
	public String getName();

	/**
	 * Is current statistical record contains static information
	 * 
	 * @return true if so, false otherwise
	 */
	public boolean isStatical();

	/**
	 * Time period in milliseconds
	 */
	public long getTime();
}
