package com.kh.keyboard;


import utils.Err;

public class SecurityCypherException extends ViewPlusException {
	public SecurityCypherException() {
	}

	public SecurityCypherException(String message) {
		super(message);
	}

	public SecurityCypherException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityCypherException(Throwable cause) {
		super(cause);
	}

	public SecurityCypherException(String message, String code) {
		super(message, code);
	}

	public SecurityCypherException(Err.CodeAndErrMsg codeAndErrMsg) {
		super(codeAndErrMsg);
	}
}
