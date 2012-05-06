package jira.For.Android.TaskDetails;

import jira.For.Android.DLog;
import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorComments;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.Feedback.FeedbackActivity;
import jira.For.Android.Help.HTMLDialog;
import jira.For.Android.Login.LoginActivity;
import jira.For.Android.PagerView.MyPagerAdapter;
import jira.For.Android.PagerView.ViewPagerIndicator;
import jira.For.Android.ProjectList.ProjectListActivity;
import roboguice.activity.RoboFragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;

public class TaskDetailsActivity extends RoboFragmentActivity {

	enum Tabs {
		basicInfo, comments, worklog, attachments;

		static Tabs fromInt(int a) {
			switch (a) {
				case 0:
					return comments;
				case 1:
					return basicInfo;
				case 2:
					return worklog;
				case 3:
					return attachments;
			}
			return null;
		}
	}

	// Displayed task
	private Issue task;

	private Context ctx;

	MyPagerAdapter pagerAdapter;
	ViewPager viewPager;
	ViewPagerIndicator indicator;
	TaskDetailsActivity taskDetailsActivity = this;
	@Inject
	private Connector connector;
	@Inject
	private ConnectorComments connectorComments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ctx = this;

		// Activity isnt restarting on change orientation look manifest
		setContentView(R.layout.taskdetailsactivitylayout);

		ImageView homeButton = (ImageView) findViewById(R.id.image_home);
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

		// taking task form getExtras
		Bundle extras = getIntent().getExtras();
		task = (Issue) extras.getSerializable("task");

		// setting adapter and its layout
		pagerAdapter = new MyPagerAdapter(new ViewsForTaskDetails(this, task, connector, connectorComments));
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOffscreenPageLimit(10);

		// setting currently displayed window as "Basic Info"
		viewPager.setCurrentItem(1);
		indicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
		viewPager.setOnPageChangeListener(indicator);
		indicator.init(1, pagerAdapter.getCount(), pagerAdapter);
		indicator.setFocusedTextColor(new int[] {255, 0, 0});

		// setting project name on the bar at the top
		TextView projectNameTextView = (TextView) findViewById(R.id.text);
		projectNameTextView.setText(task.getKey());

		// Getting out colors
		int color = getResources().getColor(R.color.page_indicator);
		indicator.setFocusedTextColor(indicator.getColorsFromInt(color));

		ImageView refreshButton = (ImageView) findViewById(R.id.image_refresh);
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int curitem = viewPager.getCurrentItem();
				pagerAdapter = new MyPagerAdapter(new ViewsForTaskDetails(
				        taskDetailsActivity, task, connector, connectorComments));
				viewPager.setAdapter(pagerAdapter);
				viewPager.setOffscreenPageLimit(10);
				indicator.init(curitem, pagerAdapter.getCount(), pagerAdapter);
				viewPager.setCurrentItem(curitem, false);
				int color = getResources().getColor(R.color.page_indicator);
				indicator.setFocusedTextColor(indicator.getColorsFromInt(color));

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_logout, menu);
		MenuItem menuItemLogout = menu.findItem(R.id.logout);
		MenuItem menuItemHelp = menu.findItem(R.id.help);

		MenuItem menuItemFeedback = menu.findItem(R.id.feedback);
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
				        "task_details_help.html");
				dialog.show();
				return false;
			}
		});
		return true;
	}

}
