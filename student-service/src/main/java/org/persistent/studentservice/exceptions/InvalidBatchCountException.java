package org.persistent.studentservice.exceptions;

public class InvalidBatchCountException extends RuntimeException {

	public InvalidBatchCountException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBatchCountException(String message) {
		super(message);
	}

}
