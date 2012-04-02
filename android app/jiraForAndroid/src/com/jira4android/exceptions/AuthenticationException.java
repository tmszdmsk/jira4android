package com.jira4android.exceptions;


public class AuthenticationException extends Exception {

    private static final long serialVersionUID = -3543125205628803071L;

	public AuthenticationException() {
	    super();
    }

	public AuthenticationException(String detailMessage, Throwable throwable) {
	    super(detailMessage, throwable);
    }

	public AuthenticationException(String detailMessage) {
	    super(detailMessage);
    }

	public AuthenticationException(Throwable throwable) {
	    super(throwable);
    }

    
}
