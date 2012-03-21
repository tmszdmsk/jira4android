package jira.For.Android.RemoteExceptions;

/**
 * An exception thrown for remote authentication failures or errors.
 */
public class RemoteAuthenticationException extends RemoteException {

	private static final long serialVersionUID = -3637390785832250983L;

	public RemoteAuthenticationException(String message) {
		super(message);
	}
}
