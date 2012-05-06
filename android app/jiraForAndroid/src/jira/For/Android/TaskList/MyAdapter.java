package jira.For.Android.TaskList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorIssues;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.ImagesCacher.ImagesCacher;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

// Using in TaskListByJQLActivity
class MyAdapter extends BaseAdapter implements ListAdapter {

	private LayoutInflater inflater;
	private List<Issue> issues;
	private TaskJQLThread task;
	private TaskListByJQLActivity activ;
	private String pattern = "yyyy-MM-dd HH:mm";
	private Date date;
	SimpleDateFormat simdatform = new SimpleDateFormat(pattern);
	private View v;
	private ConnectorIssues connectorIssues;
	private Connector connector;

	public MyAdapter(Context context, Issue[] issues,
	                 TaskListByJQLActivity taskListByJQLActivity,
	                 Connector connector, ConnectorIssues connectorIssues) {
		this.connector = connector;
		this.connectorIssues = connectorIssues;
		this.inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.issues = Collections.synchronizedList(new ArrayList<Issue>(Arrays
		        .asList(issues)));
		this.activ = taskListByJQLActivity;
		v = inflater.inflate(R.layout.progress_bar_layout, null);
	}

	String getFilterId() {
		return activ.getFilterId();
	}

	String getQuery() {
		return activ.getQuery();
	}

	List<Issue> getIssues() {
		return issues;
	}

	Issue getIssue(int pos) {
		return issues.get(pos);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public int getCount() {
		if (activ.getIsLoading()) return issues.size() + 1;
		else return issues.size();
	}

	@Override
	public String getItem(int position) {
		return getIssue(position).toString();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	void loadIfPossible(int position) {
		// Sysout do testow
		System.out.println("CZYJEST: " + activ.getIsMoreDataAvaliable()
		        + " pozycja: " + position + " size1: " + getCount()
		        + " size2: " + issues.size() + " loading: "
		        + activ.getIsLoading());
		// koniec sysouta

		Log.d("MyAdapter", "jestem w loadIfPossible()");

		if (activ.getIsLoading()) date = (issues.get(issues.size() - 2))
		        .getUpdatedAsDate();
		else date = (issues.get(issues.size() - 1)).getUpdatedAsDate();
		System.out.println("!!!!!!!!!!!!!!Jestem w getView i date wynosi: \""
		        + date + "\"\n i query wynosi: " + getQuery()
		        + " isMoreDataAvaliable: " + activ.getIsMoreDataAvaliable()
		        + "!!!!!!!!!!!!!!!!!!!!");
		if (task == null) {
			Log.d("MyAdapter", "AsyncTask jest ładowany PIERWSZY raz.");
			String jaco = "updated < \'" + simdatform.format(date) + "\' And "
			        + getQuery();
			System.out.println("Jajco: " + jaco);

			task = new TaskJQLThread(getFilterId(), jaco, activ,
			        connectorIssues);
			task.execute();
		}

		else if (task.getStatus() != AsyncTask.Status.RUNNING) {

			System.out.println("Odpalam taska!!!!!!!!!!!!!@!@!@!@!@@");
			task = new TaskJQLThread(getFilterId(), "updated < \'"
			        + simdatform.format(date) + "\' And " + getQuery(), activ,
			        connectorIssues);
			task.execute();
		}
	}

	/**
	 * Sets the values of layout components.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (position == issues.size() && activ.getIsLoading()) {
			Log.d("MyAdapter", "getView: zaraz dam kreciolka");
			// return convertView.findViewById(R.id.progress_bar);
			convertView = v;
			return convertView;
		}
		else {
			if (convertView == null || convertView == v) {
				convertView = inflater.inflate(
				        R.layout.row_task_list_by_jql_layout, null);
			}
			// Ustawianie view

			// Getting IssueType image
			ImageView taskType = (ImageView) convertView
			        .findViewById(R.id.taskType);
			taskType.setImageBitmap(ImagesCacher.getInstance().issuesTypesBitmaps
			        .get(issues.get(position).getType()));

			ImageView taskPriory = (ImageView) convertView
			        .findViewById(R.id.taskPriority);
			taskPriory
			        .setImageBitmap(ImagesCacher.getInstance().issuesPrioritesBitmaps
			                .get(issues.get(position).getPriority()));

			LinearLayout taskColor = (LinearLayout) convertView
			        .findViewById(R.id.taskColor);
			taskColor.setBackgroundColor(Color
			        .parseColor(connector.getPriority(
			                issues.get(position).getPriority()).getColor()));

			TextView key = (TextView) convertView
			        .findViewById(R.id.rowTaskListByJQLActivityTaskKeyTextView);
			key.setText(getIssue(position).getKey());

			TextView summary = (TextView) convertView
			        .findViewById(R.id.rowTaskListByJQLActivityTaskSummaryTextView);
			summary.setText(getIssue(position).getSummary());

			TextView assignee = (TextView) convertView
			        .findViewById(R.id.rowTaskListByJQLActivity_asignee_TextView);
			assignee.setText(getIssue(position).getAssigneeFullName());

			TextView type = (TextView) convertView
			        .findViewById(R.id.rowTaskListByJQLActivity_subtask_TextView);
			String str = getIssue(position).getType();

			if (str.equals("8") || str.equals("5")) {
				type.setText("sub");
			}
			else {
				type.setText("task");
			}
			// Koniec ustawiania view

			if ((position >= (issues.size() - TaskListByJQLActivity.STARTING_POINT))
			        && activ.getIsMoreDataAvaliable() && !activ.getIsLoading()) {
				// Ładujemy wiecej taskow z serwera
				loadIfPossible(position);
			}
			return convertView;

		}
	}
}
