package com.kiryeyev.serverstat.monitor;

/**
 * Common interface for all statistical records
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
	 * Is current statistical record contains static information?
	 * 
	 * @return true if so, false otherwise
	 */
	public boolean isStatical();
}
