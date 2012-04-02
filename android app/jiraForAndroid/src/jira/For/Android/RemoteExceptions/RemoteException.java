package jira.For.Android.RemoteExceptions;

/**
 * A general exception that occurs remotely.
 */
public class RemoteException extends Exception {

	private static final long serialVersionUID = 6983545678045536993L;

	public RemoteException() {
	    super();
	    // TODO Auto-generated constructor stub
    }

	public RemoteException(String detailMessage, Throwable throwable) {
	    super(detailMessage, throwable);
	    // TODO Auto-generated constructor stub
    }

	public RemoteException(String detailMessage) {
	    super(detailMessage);
	    // TODO Auto-generated constructor stub
    }

	public RemoteException(Throwable throwable) {
	    super(throwable);
	    // TODO Auto-generated constructor stub
    }

}
