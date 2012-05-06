package jira.For.Android.TaskDetails.WorkLog;

import java.util.List;

import jira.For.Android.R;
import jira.For.Android.Thread;
import jira.For.Android.Connector.ConnectorWorkLog;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.DataTypes.WorkLog;
import jira.For.Android.TaskDetails.TaskDetailsActivity;
import android.view.View;
import android.widget.ListView;

public class LoadWorkLogThread extends Thread<List<WorkLog>> {

	Issue task;
	private final ConnectorWorkLog connectorWorklog;

	public LoadWorkLogThread(View view, TaskDetailsActivity activity,
	                         Issue task, ConnectorWorkLog connector) {
		super(view, activity);
		this.task = task;
		this.connectorWorklog = connector;
	}

	protected synchronized void onPostExecute(List<WorkLog> result) {
		super.hideProgressBar();
		// TODO Connector ma zwracać nulla jak nic nie ma naprawde!
		if (result == null) {
			view.findViewById(R.id.worklog_list_no_worklog).setVisibility(
			        View.VISIBLE);
		}
		else {
			WorkLogAdapter adapter = new WorkLogAdapter(
			        activity.getBaseContext(), result);
			ListView listView = (ListView) activity
			        .findViewById(R.id.worklog_listview);
			listView.setVisibility(View.VISIBLE);
			listView.setAdapter(adapter);
		}
	}

	@Override
	protected synchronized List<WorkLog> doInBackground(Void... params) {
		try {
			return connectorWorklog.getWorklog(task.getKey());
		} catch (Exception e) {
			setException(e);
		}
		return null;
	}

}
