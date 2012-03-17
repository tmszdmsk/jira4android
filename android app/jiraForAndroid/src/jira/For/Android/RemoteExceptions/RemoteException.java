package jira.For.Android.RemoteExceptions;

/**
 * A general exception that occurs remotely.
 */
public class RemoteException extends Exception {

	private static final long serialVersionUID = 6983545678045536993L;

	public RemoteException(String message) {
		super(message);
	}
}
