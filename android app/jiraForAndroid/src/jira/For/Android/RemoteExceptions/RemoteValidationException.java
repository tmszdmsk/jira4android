package jira.For.Android.RemoteExceptions;

/**
 * Exception thrown when remote data does not validate properly.
 */
public class RemoteValidationException extends RemoteException {

	private static final long serialVersionUID = -2950444335253498191L;

	public RemoteValidationException() {
	    super();
	    // TODO Auto-generated constructor stub
    }

	public RemoteValidationException(String detailMessage, Throwable throwable) {
	    super(detailMessage, throwable);
	    // TODO Auto-generated constructor stub
    }

	public RemoteValidationException(String detailMessage) {
	    super(detailMessage);
	    // TODO Auto-generated constructor stub
    }

	public RemoteValidationException(Throwable throwable) {
	    super(throwable);
	    // TODO Auto-generated constructor stub
    }
	
	
}
