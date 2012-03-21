package jira.For.Android.RemoteExceptions;

/**
 * Exception thrown when remote data does not validate properly.
 */
public class RemoteValidationException extends RemoteException {

	private static final long serialVersionUID = -2950444335253498191L;

	public RemoteValidationException(String message) {
		super(message);
	}
}
