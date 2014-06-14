package com.ntk.darkmoor.exception;


public class LoadException extends BaseException {

	public LoadException(String errorCause) {
		super(errorCause);
	}

	public LoadException(Exception e) {
		super(e);
	}

}
