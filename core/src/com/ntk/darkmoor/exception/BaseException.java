package com.ntk.darkmoor.exception;

public class BaseException extends RuntimeException {

	protected String errorCause;
	
	protected Throwable t;

	public BaseException(String errorCause) {
		this.errorCause = errorCause;
	}
	
	public String getErrorCause() {
		return errorCause;
	}
}
