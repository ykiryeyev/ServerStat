package com.kiryeyev.serverstat;

import com.kiryeyev.serverstat.monitor.StatRecord;

public interface StatRenderer {
	/**
	 * Convert {@StatRecor} to some format acceptable for server output. 
	 * Like JSON, HTML or pretty printed plain text
	 * @param record to be rendered
	 * @return String in output format
	 */
	public String render( StatRecord record);
}
