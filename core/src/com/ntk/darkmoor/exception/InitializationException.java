package com.ntk.darkmoor.exception;


public class InitializationException extends BaseException {

	public InitializationException(String errorCause) {
		super(errorCause);
	}

	public InitializationException(Exception e) {
		super(e);
	}


}
