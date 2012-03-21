package jira.For.Android;

import android.util.Log;

public class DLog {

	public static void i(String message) {
		Log.i("jira_for_android", message);
	}

	public static void i(String TAG, String message) {
		Log.i(TAG, message);
	}

	public static void e(String message) {
		Log.e("jira_for_android", message);
	}

	public static void e(String TAG, String message) {
		Log.e(TAG, message);
	}
}
