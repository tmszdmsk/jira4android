package jira.For.Android.Login;

import jira.For.Android.DLog;
import jira.For.Android.DefaultPreference;
import jira.For.Android.PreferenceKeyHolder;
import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Feedback.FeedbackActivity;
import jira.For.Android.Help.HTMLDialog;
import roboguice.activity.RoboActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

public class LoginActivity extends RoboActivity {

	// For putExtra
	public static final String pls_do_not_login_me = "pls_do_not_login_me";
	// Layout components
	private EditText urlAddress, username, password;
	private Button buttonLogin;
	private Spinner spinner;
	private InputMethodManager imm;
	private View mainLayout;

	// Thread for canceling
	private LoginThread thread;
	private LoginActivity loginActivity = this;
	public boolean cos = false;
	@Inject
	private Connector connector;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		DLog.i("LoginActivity", "onCreate() <--- i'm here");

		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_activity_layout);

		// Setting INVISIBLE becouse default they are visible ;)
		findViewById(R.id.data_is_loading).setVisibility(TextView.INVISIBLE);
		findViewById(R.id.progress_bar).setVisibility(TextView.INVISIBLE);

		mainLayout = findViewById(R.id.login_activity_linearlay);
		urlAddress = (EditText) findViewById(R.id.login_URL_EditText);
		username = (EditText) findViewById(R.id.login_username_EditText);
		password = (EditText) findViewById(R.id.login_password_EditText);
		imm = (InputMethodManager) loginActivity
		        .getSystemService(Context.INPUT_METHOD_SERVICE);
		findViewById(R.id.login_relativeLayout);

		buttonLogin = (Button) this.findViewById(R.id.login_login_Button);
		// Add action to login button
		buttonLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
				login();
			}
		});

		spinner = (Spinner) findViewById(R.id.spinner_http_Or_https);

		assert (spinner.getCount() == 1);// TODO FOr Test change to 2 not 1
		// For getting saved data like user, password and url
		final SharedPreferences settings = getSharedPreferences(
		        PreferenceKeyHolder.PREFERENCE_SETTINGS_USER_DATA, MODE_PRIVATE);

		spinner.setSelection(settings.getInt(
		        PreferenceKeyHolder.PREFERENCE_HTTP_OR_HTTPS,
		        DefaultPreference.PREFERENCE_HTTP_OR_HTTPS));

		if (settings.contains(PreferenceKeyHolder.PREFERENCE_URL_FIELD)
		        || settings.contains(PreferenceKeyHolder.PREFERENCE_USER_FIELD)
		        || settings
		                .contains(PreferenceKeyHolder.PREFERENCE_PASSWORD_FIELD)) {

			((CheckBox) findViewById(R.id.login_checkbox)).setChecked(true);

			urlAddress.setText(settings.getString(
			        PreferenceKeyHolder.PREFERENCE_URL_FIELD, ""));
			username.setText(settings.getString(
			        PreferenceKeyHolder.PREFERENCE_USER_FIELD, ""));
			password.setText(settings.getString(
			        PreferenceKeyHolder.PREFERENCE_PASSWORD_FIELD, ""));

			Bundle extras = getIntent().getExtras();
			if (extras == null
			        || (!extras.getBoolean(pls_do_not_login_me, false))) login();
		}

		urlAddress.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
				        && (keyCode == KeyEvent.KEYCODE_TAB)) {
					// Perform action on key press
					Log.d("urlAddress", "Tab");
					username.requestFocus();
					return true;
				}

				return false;
			}
		});
		username.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
				        && (keyCode == KeyEvent.KEYCODE_TAB)) {
					// Perform action on key press
					Log.d("username", "Tab");
					password.requestFocus();
					return true;
				}

				return false;
			}
		});

		password.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
				        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					Log.d("password", "Enter");
					imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
					login();
					return true;
				}
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
				        && (keyCode == KeyEvent.KEYCODE_TAB)) {
					// Perform action on key press
					Log.d("password", "Tab");
					imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
					buttonLogin.requestFocus();
					return true;
				}

				return false;
			}
		});

		mainLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_logout, menu);
		MenuItem menuItemFeedback = menu.findItem(R.id.feedback);
		MenuItem menuItemHelp = menu.findItem(R.id.help);

		menuItemFeedback
		        .setOnMenuItemClickListener(new OnMenuItemClickListener() {

			        @Override
			        public boolean onMenuItemClick(MenuItem item) {
				        Intent intent = new Intent(getBaseContext(),
				                FeedbackActivity.class);
				        startActivity(intent);
				        return false;
			        }
		        });

		MenuItem menuItemLogout = menu.findItem(R.id.logout);
		menuItemLogout
		        .setOnMenuItemClickListener(new OnMenuItemClickListener() {

			        @Override
			        public boolean onMenuItemClick(MenuItem item) {
				        if (thread != null) thread.cancel(true);
				        return true;
			        }
		        });

		menuItemHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				HTMLDialog dialog = new HTMLDialog(loginActivity,
				        "login_help.html");
				dialog.show();
				return false;
			}
		});

		return true;
	}

	@Override
	protected void onPause() {
		DLog.i("LoginActivity", "onPause() <--- i'm here");
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		DLog.i("LoginActivity", "onStop() <--- i'm here");
	};

	@Override
	protected void onStart() {
		super.onStart();
		DLog.i("LoginActivity", "onStart() <--- i'm here");
		if (connector == null || connector.getIsConnected()) finish();
		connector.setIsConnected(false);
	};

	@Override
	protected void onResume() {
		DLog.i("LoginActivity", "onResume() <--- i'm here");
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		DLog.i("LoginActivity", "onDestroy() <--- i'm here");

		// if checkbox is unchecked we delete login,pas,url from memory
		if (!((CheckBox) findViewById(R.id.login_checkbox)).isChecked()) {
			SharedPreferences settings = getSharedPreferences(
			        PreferenceKeyHolder.PREFERENCE_SETTINGS_USER_DATA,
			        MODE_PRIVATE);
			settings.edit().clear().commit();
		}

		super.onDestroy();
	};

	@Override
	public void onBackPressed() {
		DLog.i("LoginActivity", "onBackPressed() <--- i'm here");

		// Przerywamy prace wątku
		// TODO ładniue to poprawić :)
		if (thread != null) thread.cancel(true);
		super.onBackPressed();
		// TODO Wyświetlić zapytanie czy na pewno chcemy zamknąć aplikacje
	};

	/**
	 * Performing action of logging. Checks the correctness of data which is
	 * gathered from layout components. Informs user about the errors. This
	 * method is called only when we click on login button
	 */
	private void login() {
		DLog.i("LoginActivity", "login() <--- i'm here");

		String url = getUrlAddress();
		String usr = getUsername();
		String pas = getPassword();
		if (url == null) urlAddress
		        .setError(getString(R.string.login_toast_wrongURL));
		else if (usr == null) username
		        .setError(getString(R.string.login_toast_invalidUsername));
		else if (pas == null) password
		        .setError(getString(R.string.login_toast_invalidPassword));
		else if (!connector.doWeHaveInternet(this)) Toast
		        .makeText(this, getString(R.string.login_toast_no_internet),
		                Toast.LENGTH_SHORT).show();
		else {
			SharedPreferences settings = getSharedPreferences(
			        PreferenceKeyHolder.PREFERENCE_SETTINGS_USER_DATA,
			        MODE_PRIVATE);
			
			// Execution of new thread for login
			thread = new LoginThread(this, settings, url, usr, pas, username,
			        password, urlAddress, buttonLogin, spinner, connector);
			thread.execute();
		}
	}

	/**
	 * Method gets the value from urlAddress and validates it.
	 * 
	 * @return valid URL or null
	 */
	private String getUrlAddress() {
		String str = urlAddress.getText().toString().trim();
		return str;
	}

	/**
	 * Get the name of the user.
	 * 
	 * @return trimmed String from username component or null
	 */
	private String getUsername() {
		// Get username form EditText

		Editable str = username.getText();
		if (str == null || str.length() == 0) {
			return null;
		}
		return str.toString().trim();
	}

	/**
	 * Get the password of the user.
	 * 
	 * @return password as a CharSequence or null
	 */
	private String getPassword() {
		// Get password form EditText
		String cs = password.getText().toString();
		if (cs == null || cs.length() == 0) {
			return null;
		}
		return cs;
	}
}
