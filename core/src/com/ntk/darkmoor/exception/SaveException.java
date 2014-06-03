package com.ntk.darkmoor.exception;


public class SaveException extends BaseException {

	public SaveException(String errorCause) {
		super(errorCause);
	}

	public SaveException(Exception e) {
		super(e);
	}

}
