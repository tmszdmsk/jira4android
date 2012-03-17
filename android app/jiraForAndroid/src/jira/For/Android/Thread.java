package jira.For.Android;

import java.net.BindException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import jira.For.Android.RemoteExceptions.RemoteAuthenticationException;
import jira.For.Android.RemoteExceptions.RemoteException;
import jira.For.Android.RemoteExceptions.RemotePermissionException;
import jira.For.Android.RemoteExceptions.RemoteValidationException;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

public abstract class Thread<TYPE> extends AsyncTask<Void, Void, TYPE> {

	protected Activity activity;
	protected View view;
	protected Exception ex;

	public Thread(Activity activity) {

		this.activity = activity;
		view = activity.getWindow().getDecorView();
	}

	public Thread(View view, Activity activity) {

		this.activity = activity;
		this.view = view;
	}

	/**
	 * View should have progressBar and textViewDataLoadingText
	 */

	protected synchronized void hideProgressBar() {

		view.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.data_is_loading).setVisibility(View.INVISIBLE);
	}

	protected synchronized void setException(Exception ex) {

		DLog.i("ProgressBarThread I get ex and setting it");
		this.ex = ex;
	}

	protected synchronized void showFailInformation() {

		if (ex == null) return;
		String errorMessage = "null msg";

		if (ex instanceof RemoteAuthenticationException) {
			errorMessage = activity
			        .getString(R.string.authentication_failures_or_errors_);
		}
		else if (ex instanceof RemoteException) {
			errorMessage = activity
			        .getString(R.string.problems_on_server_with_data);
		}
		else if (ex instanceof RemotePermissionException) {
			errorMessage = activity
			        .getString(R.string.permissions_are_violated_remotely);
		}
		else if (ex instanceof RemoteValidationException) {
			errorMessage = activity
			        .getString(R.string.remote_data_does_not_validate_properly);
		}
		else if (ex instanceof ConnectException) {
			// Signals that an error occurred while attempting to connect a
			// socket to a remote address and port.
			errorMessage = activity.getString(R.string.connection_refused);
		}
		else if (ex instanceof UnknownHostException) {
			// Thrown to indicate that the IP address of a host could not be
			// determined.
			errorMessage = activity
			        .getString(R.string.login_toast_unresolvedHost);
		}
		else if (ex instanceof SocketTimeoutException) {
			// Signals that a timeout has occurred on a socket read or accept.
			errorMessage = activity
			        .getString(R.string.time_out_has_occurred_on_a_socket_read);
		}
		else if (ex instanceof BindException) {
			// Signals that an error occurred while attempting to bind a socket
			// to a local address and port.
			errorMessage = activity.getString(R.string.bind_socket_problem);
		}
		else if (ex instanceof HttpRetryException) {
			// Thrown to indicate that a HTTP request needs to be retried but
			// cannot be retried automatically, due to streaming mode being
			// enabled.
			errorMessage = activity
			        .getString(R.string.http_needs_to_be_retired);
		}
		else if (ex instanceof MalformedURLException) {
			// Thrown to indicate that a malformed URL has occurred.
			errorMessage = activity.getString(R.string.malformed_url_);
		}
		else if (ex instanceof NoRouteToHostException) {
			// Signals that an error occurred while attempting to connect a
			// socket to a remote address and port.
			errorMessage = activity
			        .getString(R.string.no_route_to_host_i_can_t_connect);
		}
		else if (ex instanceof PortUnreachableException) {
			// Signals that an ICMP Port Unreachable message has been received
			// on a connected datagram.
			errorMessage = activity.getString(R.string.icmp_port_unreachable);
		}
		else if (ex instanceof ProtocolException) {
			// Thrown to indicate that there is an error in the underlying
			// protocol, such as a TCP error.
			errorMessage = activity.getString(R.string.tcp_error);
		}
		else if (ex instanceof SocketException) {
			// Thrown to indicate that there is an error in the underlying
			// protocol, such as a TCP error.
			errorMessage = activity
			        .getString(R.string.socket_problems_tcp_problem);
		}
		else if (ex instanceof UnknownServiceException) {
			// Thrown to indicate that an unknown service exception has
			// occurred.
			errorMessage = activity.getString(R.string.unknown_service_);
		}
		else if (ex instanceof URISyntaxException) {
			// Checked exception thrown to indicate that a string could not be
			// parsed as a URI reference.
			errorMessage = activity.getString(R.string.uri_syntax_problem);
		}
		else {
			errorMessage = activity.getString(R.string.unknown_error);
		}
		showToast(errorMessage);
		DLog.e("ProgressBarThread", "Error:\n" + ex.getMessage());
		ex.printStackTrace();
	}

	/**
	 * Shows toast on the screen.
	 * 
	 * @param str
	 *            text to be shown
	 */
	protected void showToast(String str) {
		if (str == null) return;
		Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
	}
}
