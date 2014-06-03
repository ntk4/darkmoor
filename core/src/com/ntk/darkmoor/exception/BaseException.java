package com.ntk.darkmoor.exception;

public class BaseException extends RuntimeException {

	protected String errorCause;
	
	protected Throwable throwable;

	public BaseException(String errorCause) {
		this.errorCause = errorCause;
	}
	
	public BaseException (Throwable t) {
		this.throwable = t;
		errorCause = t.getMessage();
	}
	
	public String getErrorCause() {
		return errorCause;
	}
}
