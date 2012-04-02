package com.jira4android.exceptions;


public class AuthorizationException extends Exception {

    private static final long serialVersionUID = -488239705540216223L;

	public AuthorizationException() {
	    super();
    }

	public AuthorizationException(String detailMessage, Throwable throwable) {
	    super(detailMessage, throwable);
    }

	public AuthorizationException(String detailMessage) {
	    super(detailMessage);
    }

	public AuthorizationException(Throwable throwable) {
	    super(throwable);
    }

    
}
