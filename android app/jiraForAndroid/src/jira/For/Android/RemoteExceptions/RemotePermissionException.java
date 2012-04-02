package jira.For.Android.RemoteExceptions;

/**
 * Exception thrown when permissions are violated remotely.
 */
public class RemotePermissionException extends RemoteException {

	private static final long serialVersionUID = -4395676153610506375L;

	public RemotePermissionException() {
	    super();
	    // TODO Auto-generated constructor stub
    }

	public RemotePermissionException(String detailMessage, Throwable throwable) {
	    super(detailMessage, throwable);
	    // TODO Auto-generated constructor stub
    }

	public RemotePermissionException(String detailMessage) {
	    super(detailMessage);
	    // TODO Auto-generated constructor stub
    }

	public RemotePermissionException(Throwable throwable) {
	    super(throwable);
	    // TODO Auto-generated constructor stub
    }

	

}
