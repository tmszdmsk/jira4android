package jira.For.Android.TaskDetails.Comments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jira.For.Android.R;
import jira.For.Android.Thread;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorComments;
import jira.For.Android.DataTypes.Comment;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.TaskDetails.TaskDetailsActivity;

import org.xmlpull.v1.XmlPullParserException;

import android.view.View;
import android.widget.ListView;

public class LoadCommentsThread extends Thread<List<Comment>> {

	Issue task;

	private final ConnectorComments connectorComments;

	public LoadCommentsThread(View view, TaskDetailsActivity activity,
	                          Issue task, ConnectorComments connectorComments) {
		super(view, activity);
		this.task = task;
		this.connectorComments = connectorComments;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	protected synchronized void onPostExecute(List<Comment> result) {
		super.hideProgressBar();
		// TODO Connector ma zwraca� nulla jak nic nie ma naprawde!
		ListView listView = (ListView) activity
		        .findViewById(R.id.comments_list_listview);
		if (result == null) {
			result = new ArrayList<Comment>();
		}
		CommentsAdapter adapter = new CommentsAdapter(
		        activity.getBaseContext(), result);

		listView.setAdapter(adapter);
		listView.setEmptyView(activity
		        .findViewById(R.id.comments_list_no_comments));
	}

	@Override
	protected synchronized List<Comment> doInBackground(Void... params) {
		try {
			return connectorComments.getComments(task.getKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setException(e);
		}
		return null;
	}

}
