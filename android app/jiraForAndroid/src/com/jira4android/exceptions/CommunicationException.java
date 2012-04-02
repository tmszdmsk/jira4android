package com.jira4android.exceptions;


public class CommunicationException extends Exception{

    private static final long serialVersionUID = 3580656524726312230L;

	public CommunicationException() {
	    super();
    }

	public CommunicationException(String detailMessage, Throwable throwable) {
	    super(detailMessage, throwable);
    }

	public CommunicationException(String detailMessage) {
	    super(detailMessage);
    }

	public CommunicationException(Throwable throwable) {
	    super(throwable);
    }
    


}
