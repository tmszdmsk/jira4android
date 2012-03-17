package jira.For.Android.Feedback;

import jira.For.Android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends Activity {

	private EditText textWithFeedback;

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
				finish();
			}
		});

		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(
				        android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");

				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				        getString(R.string.mailForFeedback));
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				        "Jira4Android-Feedback");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				        textWithFeedback.getText().toString());
				startActivity(Intent.createChooser(emailIntent, "Send e-mail."));
			}
		});
	}
}
