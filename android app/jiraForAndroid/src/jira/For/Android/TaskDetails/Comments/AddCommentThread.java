package jira.For.Android.TaskDetails.Comments;

import jira.For.Android.DLog;
import jira.For.Android.R;
import jira.For.Android.Thread;
import jira.For.Android.Connector.ConnectorComments;
import jira.For.Android.DataTypes.Comment;
import jira.For.Android.TaskDetails.TaskDetailsActivity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AddCommentThread extends Thread<Void> {

	private Comment comment;
	private String issueKey;
	private final ConnectorComments connectorComments;

	public AddCommentThread(TaskDetailsActivity taskDetailsActivity,
	                        Comment comment, String issueKey, ConnectorComments connectorComments) {
		super(taskDetailsActivity);
		this.comment = comment;
		this.issueKey = issueKey;
		this.connectorComments = connectorComments;
	}

	@Override
	protected synchronized void onPreExecute() {

		// Showing progressbar activity while adding comment and refreshing list

		// TODO Dodać ukrywania listy tasków
		view.findViewById(R.id.edit_text_comment).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.button_send_comment).setVisibility(
		        View.INVISIBLE);

		view.findViewById(R.id.comments_list_listview).setVisibility(
		        View.INVISIBLE);

		view.findViewById(R.id.data_is_loading).setVisibility(View.VISIBLE);
		view.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
	};

	@Override
	protected Void doInBackground(Void... params) {
		DLog.i("AddCommentThread", "doInBackground() <-- i'm here");
		try {
			connectorComments.addComment(issueKey, comment);
		} catch (Exception e) {
			setException(e);
		}
		return null;
	}

	@Override
	protected synchronized void onPostExecute(Void cos) {
		DLog.i("AddCommentThread", "onPostExecute <--- i'm here");
		hideProgressBar();

		EditText messageOfComment = (EditText) view
		        .findViewById(R.id.edit_text_comment);
		messageOfComment.setVisibility(TextView.VISIBLE);
		view.findViewById(R.id.button_send_comment).setVisibility(View.VISIBLE);
		view.findViewById(R.id.comments_list_listview).setVisibility(
		        View.VISIBLE);

		// Jeżeli udało się wysłać komentarz //TODO zrobić to lepiej
		if (ex == null) {
			
			System.out.println("Wiec wysłałem komentarz?");
			// If we send msg so we clear field/focus etc.
			messageOfComment.setText("");
			messageOfComment.clearFocus();
			InputMethodManager inputMethodManager = (InputMethodManager) activity
			        .getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
			        messageOfComment.getWindowToken(),
			        InputMethodManager.RESULT_UNCHANGED_SHOWN);

			// Adding comment to the list :D move this to thread when add is
			// succesfull
			ListView listView = (ListView) activity
			        .findViewById(R.id.comments_list_listview);
			CommentsAdapter listAdapter = (CommentsAdapter) listView
			        .getAdapter();
			listAdapter.addComment(comment);
			listAdapter.notifyDataSetChanged();
		}
		else {

			System.out
			        .println("Nie udalo się dodać komentarza!!!!!!!!!!!!!!!!!!!");
			showFailInformation();
		}
	}
}
