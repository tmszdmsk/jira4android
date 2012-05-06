package jira.For.Android.TaskList;

import java.util.Arrays;

import jira.For.Android.DLog;
import jira.For.Android.GeneralActivity;
import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorIssues;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.Feedback.FeedbackActivity;
import jira.For.Android.Help.HTMLDialog;
import jira.For.Android.Login.LoginActivity;
import jira.For.Android.ProjectList.ProjectListActivity;
import jira.For.Android.TaskDetails.TaskDetailsActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;

public class TaskListByJQLActivity extends GeneralActivity {

	// Issue[] issues;
	private TaskListByJQLActivity taskListByJQLACtivity;
	public final static int TASKLIST_COUNT = 7;
	public final static int STARTING_POINT = TASKLIST_COUNT / 3;
	ListView listView;
	MyAdapter adapter;
	private boolean isListDownloaded;
	private boolean isDownloading;
	private boolean isLoading;
	private boolean isMoreDataAvaliable;
	private String filterId;
	private String query;
	private TaskJQLThread thread;
	private Context ctx;
	private ImageView refreshButton;
	@Inject
	private Connector connector;
	@Inject
	private ConnectorIssues connectorIssues;

	/*
	 * public int getIssuesSize() { return issues.size(); }
	 */

	public ImageView getRefreshButton() {
		return refreshButton;
	}

	public String getFilterId() {
		return filterId;
	}

	public String getQuery() {
		return query;
	}

	public void notifyAdapterDataSetChanged() {
		adapter.notifyDataSetChanged();
		Log.d("TaskListByJQLActivity",
		        "notifyAdapterDataSetChanged() <---- Tu byłem");
	}

	public void setIsListDownloaded(boolean ifIs) {
		isListDownloaded = ifIs;
	}

	public boolean getIsListDownloaded() {
		return isListDownloaded;
	}

	public void setIsDownloading(boolean ifIs) {
		isDownloading = ifIs;
	}

	public void setIsLoading(boolean ifIs) {
		isLoading = ifIs;
	}

	public void setIsMoreDataAvaliable(boolean ifIs) {
		System.out.println("Zmienilem setIsMoreDataAvaliable() na: " + ifIs);
		isMoreDataAvaliable = ifIs;
	}

	public boolean getIsLoading() {
		return isLoading;
	}

	public boolean getIsMoreDataAvaliable() {
		return isMoreDataAvaliable;
	}

	public boolean getIsDownloading() {
		return isDownloading;
	}

	public void addIssues(Issue[] issues) {
		this.adapter.getIssues().addAll(Arrays.asList(issues));
	}

	public Issue getIssueAt(int pos) {
		return adapter.getIssue(pos);
	}

	void setListViewOnActivity(Issue[] result) {

		if (result.length < TASKLIST_COUNT) {
			setIsMoreDataAvaliable(false);
		}
		else {
			setIsMoreDataAvaliable(true);
		}

		listView = (ListView) findViewById(R.id.taskListByJQLActivityListView);
		listView.setEmptyView(findViewById(R.id.issues_list_no_issues));
		// listView = (ListView)
		// getLayoutInflater().inflate(R.id.taskListByJQLActivityListView,
		// null);
		listView.setVisibility(View.VISIBLE);
		listView.setAdapter(new MyAdapter(this, result, this, connector, connectorIssues));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
			        long arg3) {
				final Intent taskDetails = new Intent(
				        TaskListByJQLActivity.this, TaskDetailsActivity.class);

				taskDetails.putExtra("task", getIssueAt(pos));

				TaskListByJQLActivity.this.startActivity(taskDetails);
			}
		});
		adapter = (MyAdapter) listView.getAdapter();
		System.out.println("Ustawilem listView");
		setIsListDownloaded(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		ctx = this;

		super.onCreate(savedInstanceState);
		DLog.i("TaskListByJQLActivity", "onCreate <-- i'm here");
		// Activity isnt restarting on change orientation look manifest
		setContentView(R.layout.task_list_by_jql_layout);

		Bundle extras = getIntent().getExtras();

		filterId = extras.getString("filterId");
		query = extras.getString("JQL");

		TextView txtView = (TextView) findViewById(R.id.text);
		txtView.setText(extras.getString("filterName"));// TODO Export this to
		                                                // strings

		ImageView homeButton = (ImageView) findViewById(R.id.image_home);
		taskListByJQLACtivity = this;
		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DLog.i("Filters Home", "Home button was pressed.");

				// Ends all activities turned on after ProjectListActivity
				Intent intent = new Intent(getBaseContext(),
				        ProjectListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		refreshButton = (ImageView) findViewById(R.id.image_refresh);
		// TODO Trzeba zaktualizować issues?
		thread = new TaskJQLThread(filterId, query, this, connectorIssues);
		thread.execute();
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setIsListDownloaded(false);
				taskListByJQLACtivity.findViewById(
				        R.id.taskListByJQLActivityListView).setVisibility(
				        View.INVISIBLE);
				taskListByJQLACtivity.findViewById(R.id.issues_list_no_issues)
				        .setVisibility(View.INVISIBLE);
				taskListByJQLACtivity.findViewById(R.id.data_is_loading)
				        .setVisibility(View.VISIBLE);
				taskListByJQLACtivity.findViewById(R.id.progress_bar)
				        .setVisibility(View.VISIBLE);

				thread = new TaskJQLThread(filterId, query,
				        taskListByJQLACtivity,connectorIssues);
				thread.execute();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_logout, menu);
		MenuItem menuItemFeedback = menu.findItem(R.id.feedback);
		MenuItem menuItemHelp = menu.findItem(R.id.help);

		menuItemFeedback
		        .setOnMenuItemClickListener(new OnMenuItemClickListener() {

			        @Override
			        public boolean onMenuItemClick(MenuItem item) {
				        Intent intent = new Intent(getBaseContext(),
				                FeedbackActivity.class);
				        startActivity(intent);
				        return false;
			        }
		        });
		MenuItem menuItemLogout = menu.findItem(R.id.logout);
		menuItemLogout
		        .setOnMenuItemClickListener(new OnMenuItemClickListener() {

			        @Override
			        public boolean onMenuItemClick(MenuItem item) {
				        // TODO czy podłączyć?
				        // if (thread != null) thread.cancel(true);
				        Intent intent = new Intent(getBaseContext(),
				                LoginActivity.class);
				        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				        intent.putExtra(LoginActivity.pls_do_not_login_me, true);
				        startActivity(intent);
				        finish();
				        connector.jiraLogout();
				        System.out.println("ZROBILEM KUPE!!");
				        return true;
			        }
		        });

		menuItemHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				HTMLDialog dialog = new HTMLDialog(ctx,
				        "filters_results_help.html");
				dialog.show();
				return false;
			}
		});

		return true;
	}

	@Override
	public void onBackPressed() {
		DLog.i("TaskListByJQLActivity", "onBackPressed() <--- i'm here");

		// Przerywamy prace wątku
		// TODO ładniue to poprawić :)
		if (thread != null) thread.cancel(true);
		super.onBackPressed();
		// TODO Wyświetlić zapytanie czy na pewno chcemy zamknąć aplikacje
	};
}
