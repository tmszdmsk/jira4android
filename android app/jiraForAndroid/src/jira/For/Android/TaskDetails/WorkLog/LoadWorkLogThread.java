package jira.For.Android.TaskDetails.WorkLog;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.view.View;
import android.widget.ListView;
import jira.For.Android.R;
import jira.For.Android.Thread;
import jira.For.Android.Connector.Connector;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.DataTypes.WorkLog;
import jira.For.Android.TaskDetails.TaskDetailsActivity;

public class LoadWorkLogThread extends Thread<List<WorkLog>> {

	Issue task;

	public LoadWorkLogThread(View view, TaskDetailsActivity activity, Issue task) {
		super(view, activity);
		this.task = task;
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
	        return Connector.getInstance().getWorkLog(task.getKey());
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } catch (XmlPullParserException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		return null;
	}

}
