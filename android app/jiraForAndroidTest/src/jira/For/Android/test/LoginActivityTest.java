package jira.For.Android.test;

import jira.For.Android.LoginActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class LoginActivityTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {

	private LoginActivity act;
	private TextView titleView;

	public LoginActivityTest() {
		super("jira.For.Android", LoginActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		act = this.getActivity();

	}
}
