package jira.For.Android.TaskList;

import jira.For.Android.DLog;
import jira.For.Android.Thread;
import jira.For.Android.Connector.ConnectorIssues;
import jira.For.Android.DataTypes.Issue;
import android.util.Log;
import android.widget.ImageView;

public class TaskJQLThread extends Thread<Issue[]> {

	String query, filterId;
	private boolean isListDown;
	ImageView refresh;
	private final ConnectorIssues connectorIssues;

	public TaskJQLThread(String filterId, String query,
	                     TaskListByJQLActivity taskListByJQLActivity, ConnectorIssues connector) {
		super(taskListByJQLActivity);
		this.query = query;
		this.filterId = filterId;
		this.connectorIssues = connector;
		refresh = taskListByJQLActivity.getRefreshButton();

	}

	void notifyChangeOfDataAndAddIssues(Issue[] result) {
		((TaskListByJQLActivity) activity).addIssues(result);
		((TaskListByJQLActivity) activity).notifyAdapterDataSetChanged();
	}

	void notifyChangeOfData() {
		((TaskListByJQLActivity) activity).notifyAdapterDataSetChanged();
	}

	@Override
	protected synchronized void onPreExecute() {
		refresh.setEnabled(false); System.out.println("refresh set enabled: false");
		isListDown = ((TaskListByJQLActivity) activity).getIsListDownloaded();
		Log.d("TaskJQLThread", "Jestem w onPreExecute() of TJQLT, "
		        + "query is: " + query + " {isListDOwn} = {" + isListDown + "}");

		if (isListDown) {
			//super.hideProgressBar();
			System.out.println("tu sie powinien pojaiwac kreciolek");
		}
		((TaskListByJQLActivity) activity).setIsLoading(true);
		if (isListDown) notifyChangeOfData();
		// super.onPreExecute();
	}

	@Override
	protected synchronized void onPostExecute(Issue[] result) {
		DLog.i("TaskJQLThread", "onPostExecute of TJQLT <--- i'm here");
		((TaskListByJQLActivity) activity).setIsLoading(false);

		if (isListDown) {
			DLog.i("TaskJQLThread", "W onPostExecute(): chowamy kreciolka");
		}
		else super.hideProgressBar();

		if (result == null) {
			result = new Issue[0];
			if (isListDown) {
				((TaskListByJQLActivity) activity)
				        .setIsMoreDataAvaliable(false);
			}
		}

			if (!isListDown) {
				Log.d("TaskJQLThread",
				        "W onPostExecute(): listView NIE jest zaladowane");
				((TaskListByJQLActivity) activity)
				        .setListViewOnActivity(result);
			}
			else {
				Log.d("TaskJQLThread",
				        "W onPostExecute(): listView jest zaladowane");

				if (result.length < TaskListByJQLActivity.TASKLIST_COUNT) {
					((TaskListByJQLActivity) activity)
					        .setIsMoreDataAvaliable(false);
					notifyChangeOfDataAndAddIssues(result);
					Log.d("TaskJQLThread",
				        "Długość tablicy result < TASKLIST_COUNT");
				}
				else {
					notifyChangeOfDataAndAddIssues(result);
					Log.d("TaskJQLThread",
				        "Długość tablicy result >= TASKLIST_COUNT");
				}
			}

		if (isListDown) notifyChangeOfData();
		showFailInformation();
		refresh.setEnabled(true); System.out.println("refresh set enabled: true");
	}

	@Override
	protected synchronized Issue[] doInBackground(Void... params) {

		DLog.i("TaskJQLThread", "doInBackground of TJQLT <--- i'm here");
		try {
			// TODO Trzeba coś innego wpisać tutaj jak 50 trzeba się nad tym
			// zastanowić
			if (query != null) {
				((TaskListByJQLActivity) activity).setIsDownloading(true);
				System.out.println("In doInBackground() of TaskJQLThread "
				        + "\"" + query + "\"");
				return connectorIssues.getIssuesByJQL(query,
				        TaskListByJQLActivity.TASKLIST_COUNT);
			}
			else if (filterId != null) {
				((TaskListByJQLActivity) activity).setIsDownloading(true);
				return connectorIssues.getIssuesFromFilterWithLimit(
				        filterId, 0, 30);
			}
			else {
				Log.wtf("TaskListByJQLActivit", "I dont get anny putExtra!!!");
				return null;
			}
		} catch (Exception e) {
			setException(e);
			return null;
		}
	}
}
