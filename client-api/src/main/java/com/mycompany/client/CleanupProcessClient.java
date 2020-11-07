package com.mycompany.client;

import java.io.InputStream;

/**
 * Client interface for the super enterprise bad date purging code.
 */
public interface CleanupProcessClient {
	/**
	 * Strip invalid dates from a JSON file that is passed in as the InputStream from a FlowFile.
	 *
	 * @param is InputStream opened by ProcessSession
	 * @return a byte array containing the pristine JSON
	 */
    byte[] stripInvalidDates(InputStream is);
}
