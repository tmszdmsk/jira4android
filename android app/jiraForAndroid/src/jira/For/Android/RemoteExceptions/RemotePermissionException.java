package jira.For.Android.RemoteExceptions;

/**
 * Exception thrown when permissions are violated remotely.
 */
public class RemotePermissionException extends RemoteException {

	private static final long serialVersionUID = -4395676153610506375L;

	public RemotePermissionException(String message) {
		super(message);
	}
}
