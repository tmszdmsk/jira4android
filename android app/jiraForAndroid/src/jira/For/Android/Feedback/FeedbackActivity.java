package jira.For.Android.Feedback;

import jira.For.Android.GeneralActivity;
import jira.For.Android.R;

import org.acra.ErrorReporter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends GeneralActivity {

	private EditText textWithFeedback;
	private final String FEEDBACK = "feedback:";
	FeedbackActivity feedbackActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.feedback_activity);

		textWithFeedback = (EditText) findViewById(R.id.feedbackForm);

		Button buttonSend = (Button) findViewById(R.id.buttonSend);
		Button buttonBack = (Button) findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				googleAnalyticsTracker.trackEvent("Clicks", // Category
				        "Button", // Action
				        "send feedback", // Label
				        1); // Value

				ErrorReporter.getInstance().putCustomData(FEEDBACK,
				        textWithFeedback.getText().toString());
				ErrorReporter.getInstance().handleSilentException(
				        new Throwable("Feedback information"));

				Toast.makeText(
				        feedbackActivity,
				        feedbackActivity
				                .getString(R.string.thanks_for_feedback),
				        Toast.LENGTH_SHORT).show();

				finish();
			}
		});
	}
}
