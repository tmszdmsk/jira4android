package jira.For.Android.Login;

import javax.net.ssl.SSLException;

import jira.For.Android.DLog;
import jira.For.Android.PreferenceKeyHolder;
import jira.For.Android.R;
import jira.For.Android.Thread;
import jira.For.Android.Connector.Connector;
import jira.For.Android.ProjectList.ProjectListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

class LoginThread extends Thread<Boolean> {

	private String url, usr, pas;
	private SharedPreferences settings;
	private EditText username, password, urlAddress;
	private Button buttonLogin;
	private final Spinner spinner;
	private LoginActivity activityLogin;
	private final Connector connector;
	
	
	public LoginThread(LoginActivity activity, SharedPreferences settings,
	                   String url, String usr, String pas, EditText username,
	                   EditText password, EditText urlAddress,
	                   Button buttonLogin, Spinner spinner, Connector connector) {

		super(activity);
		this.connector = connector;
		activityLogin = (LoginActivity) this.activity;
		this.spinner = spinner;
		DLog.i("LoginActivity", "LoginTaskThread constructor <--- i'm here");

		this.username = username;
		this.password = password;
		this.urlAddress = urlAddress;
		this.buttonLogin = buttonLogin;

		this.pas = pas;
		this.url = url;
		this.usr = usr;
		this.settings = settings;
		
	}

	private boolean showFailInformationGUI(Exception ex) {

		// HINT
		// com.atlassian.jira.rpc.exception.RemoteAuthenticationException
		if (ex instanceof AuthenticationException) {
			username.setError(activity
			        .getString(R.string.login_toast_invalidUsername));
			password.setError(activity
			        .getString(R.string.login_toast_invalidPassword));
			return true;
		}
		else if (ex instanceof AuthorizationException) {
			username.setError(activity
			        .getString(R.string.login_toast_permissionViolated));
			return true;
		}
		else if (ex instanceof CommunicationException) {
			urlAddress.setError(activity.getString(R.string.unknown_host));
			return true;
		}
		else if (ex instanceof SSLException) {
			Toast.makeText(activityLogin, ex.getMessage(), Toast.LENGTH_LONG)
			        .show();
			return true;
		}
		return false;
	}

	private void saveUserDataToSharedPreferences() {
		if (((CheckBox) view.findViewById(R.id.login_checkbox)).isChecked()) {

			DLog.i("Saving user data to SharedPreference");
			boolean saveSuccessful = settings
			        .edit()
			        .putString(PreferenceKeyHolder.PREFERENCE_USER_FIELD, usr)
			        .putString(PreferenceKeyHolder.PREFERENCE_PASSWORD_FIELD,
			                pas)
			        .putString(PreferenceKeyHolder.PREFERENCE_URL_FIELD, url)
			        .putInt(PreferenceKeyHolder.PREFERENCE_HTTP_OR_HTTPS,
			                spinner.getSelectedItemPosition()).commit();
			if (!saveSuccessful) super
			        .showToast("I can't save URL , username , password!");
		}
	}

	@Override
	protected synchronized void onPreExecute() {

		// Showing progressbar activity while loging to server
		urlAddress.setVisibility(TextView.INVISIBLE);
		username.setVisibility(TextView.INVISIBLE);
		password.setVisibility(TextView.INVISIBLE);
		spinner.setVisibility(View.INVISIBLE);
		buttonLogin.setVisibility(TextView.INVISIBLE);
		
		view.findViewById(R.id.login_checkbox)
		        .setVisibility(TextView.INVISIBLE);

		view.findViewById(R.id.data_is_loading).setVisibility(TextView.VISIBLE);

		view.findViewById(R.id.progress_bar).setVisibility(TextView.VISIBLE);
	};

	@Override
	protected synchronized Boolean doInBackground(Void... params) {

		DLog.i("LoginActivity", "doInBackground <--- i'm here");
		boolean returnValue = false;
		try {

			returnValue = connector.jiraLogin(
			        usr,
			        pas.toString(),
			        url,
			        activityLogin.getResources().getStringArray(
			                R.array.http_or_https)[spinner
			                .getSelectedItemPosition()].equals(activityLogin
			                .getString(R.string.item_https)));
			// Connector.getInstance().downloadIssueTypes();
			// Connector.getInstance().downloadPriorities();
		} catch (Exception e) {
			setException(e);
		}
		return returnValue;
	}

	@Override
	protected synchronized void onPostExecute(Boolean loginSuccess) {
		DLog.i("LoginActivity", "onPostExecute <--- i'm here");

		if (loginSuccess.booleanValue()) {
			connector.setIsConnected(true);
			// If CheckBox is checked we save our usr,pas and url to
			// SharedPreference
			saveUserDataToSharedPreferences();

			// Start view with projects
			Intent intent = new Intent(activity, ProjectListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			activity.startActivity(intent);

			// activity.finish();
			activity.overridePendingTransition(0, 0);
		}
		else {
			// If we have saved data and data changed on server (so our
			// saved
			// data is out of date) we clear previous saved.
			settings.edit().clear().commit();
			System.out.println("Removing saved data");
			if (!showFailInformationGUI(ex)) showFailInformation();
			hideAndShowUI();
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		hideAndShowUI();
	}

	private void hideAndShowUI() {
		hideProgressBar();

		spinner.setVisibility(View.VISIBLE);
		urlAddress.setVisibility(TextView.VISIBLE);
		username.setVisibility(TextView.VISIBLE);
		password.setVisibility(TextView.VISIBLE);
		buttonLogin.setVisibility(TextView.VISIBLE);
		view.findViewById(R.id.login_checkbox).setVisibility(TextView.VISIBLE);
		
	}

}
