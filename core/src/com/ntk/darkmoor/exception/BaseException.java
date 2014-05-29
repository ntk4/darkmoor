package com.ntk.darkmoor.exception;

public class BaseException extends RuntimeException {

	protected String errorCause;
	
	protected Throwable t;

	public String getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
}
