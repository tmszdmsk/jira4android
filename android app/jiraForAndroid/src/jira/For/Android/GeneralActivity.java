package jira.For.Android;

import roboguice.activity.RoboActivity;
import android.os.Bundle;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public abstract class GeneralActivity extends RoboActivity {

	protected GoogleAnalyticsTracker googleAnalyticsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		googleAnalyticsTracker = GoogleAnalyticsTracker.getInstance();
		googleAnalyticsTracker.startNewSession("UA-30681839-2", this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		googleAnalyticsTracker.stopSession();
	}
}
