package com.kiryeyev.serverstat;

import com.kiryeyev.serverstat.monitor.StatRecord;

/**
 * Renderer to convert {@link StatRecord} to user friendly format like HTML,
 * JSON etc.
 * 
 * @author Yevgen_Kiryeyev
 *
 */
public interface StatRenderer {
	/**
	 * Convert {@StatRecor} to some format acceptable for server output. Like
	 * JSON, HTML or pretty printed plain text
	 * 
	 * @param record
	 *            to be rendered
	 * @return String in output format
	 */
	public String render(StatRecord record);
}
