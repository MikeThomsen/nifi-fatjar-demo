package com.mycompany.nifi;

/**
 * An exception that is thrown by the cleanup handler when it encounters a processing error.
 */
public class CleanupFailedException extends Exception {
	public CleanupFailedException(Exception ex) {
		super(ex);
	}
}
