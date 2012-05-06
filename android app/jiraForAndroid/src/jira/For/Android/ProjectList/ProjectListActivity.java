package jira.For.Android.ProjectList;

import jira.For.Android.DLog;
import jira.For.Android.GeneralActivity;
import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorProjects;
import jira.For.Android.DataTypes.Project;
import jira.For.Android.Feedback.FeedbackActivity;
import jira.For.Android.Filters.FiltersListActivity;
import jira.For.Android.Help.HTMLDialog;
import jira.For.Android.Login.LoginActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;


public class ProjectListActivity extends GeneralActivity implements
        OnItemClickListener {

	Project[] projects;
	private LoadProjectsThread thread;
	ProjectListActivity projectListActivity;
	private Context ctx;
	private ImageView refresh;
	@Inject
	private Connector connector;
	@Inject
	private ConnectorProjects connectorProjects;
	
	public ImageView getRefreshButton() {
		return refresh;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		ctx = this;

		// Activity isnt restarting on change orientation look manifest
		DLog.i("ProjectListActivity",
		        "onCreate() ProjectListActivity <-- I'm here!");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_view_activity);
		refresh = (ImageView) findViewById(R.id.image_refresh);

		TextView txtView = (TextView) findViewById(R.id.text);
		txtView.setText(R.string.projects);
		projectListActivity = this;
		thread = new LoadProjectsThread(getWindow().getDecorView(), this, connectorProjects);
		thread.execute();
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.data_is_loading).setVisibility(
				        TextView.VISIBLE);
				findViewById(R.id.progress_bar).setVisibility(TextView.VISIBLE);
				findViewById(R.id.project_list_no_projects).setVisibility(
				        View.INVISIBLE);
				findViewById(R.id.project_list_view).setVisibility(
				        View.INVISIBLE);
				thread = new LoadProjectsThread(getWindow().getDecorView(),
				        projectListActivity, connectorProjects);
				thread.execute();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_logout, menu);
		MenuItem menuItemLogout = menu.findItem(R.id.logout);
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
				        return true;
			        }
		        });
		
		menuItemHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				HTMLDialog dialog = new HTMLDialog(ctx,
				        "project_list_help.html");
				dialog.show();
				return false;
			}
		});
		
		return true;
	}

	@Override
	public void onBackPressed() {
		DLog.i("LoginActivity", "onBackPressed() <--- i'm here");

		// Przerywamy prace wątku
		if (thread != null) thread.cancel(true);
		super.onBackPressed();
	};

	String[] getListOfStringsFromProjects(Project[] projects) {

		if (projects == null || projects.length == 0) return new String[0];
		String fooProjects = "";
		// Getting projects names contatening them with new line then split them
		// to get simple array of strings
		for (int i = 0; i < projects.length; ++i) {
			fooProjects += (projects[i].getName() + "\n");
		}
		return fooProjects.split("\n");
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int pos, long id) {

		// TODO for tests only
		DLog.i("ProjectListActivity", "I selected " + projects[pos].getName()
		        + " selected");

		Intent intent = new Intent(this, FiltersListActivity.class);
		intent.putExtra("projectID", projects[pos].getId());
		startActivity(intent);
	}
}
