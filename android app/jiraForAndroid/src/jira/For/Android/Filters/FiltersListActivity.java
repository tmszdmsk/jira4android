package jira.For.Android.Filters;

import jira.For.Android.DLog;
import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorFilters;
import jira.For.Android.Feedback.FeedbackActivity;
import jira.For.Android.Help.HTMLDialog;
import jira.For.Android.Login.LoginActivity;
import jira.For.Android.PagerView.MyPagerAdapter;
import jira.For.Android.PagerView.ViewPagerIndicator;
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

public class FiltersListActivity extends RoboFragmentActivity {

	// Package///////////////////////////////////////////////////////////////////
	MyPagerAdapter pagerAdapter;
	ViewPager viewPager;
	ViewPagerIndicator indicator;
	FiltersListActivity filtersListActivity = this;
	private Context ctx;
	private ImageView refreshButton;
	@Inject
	private Connector connector;
	@Inject
	private ConnectorFilters connectorFilters;

	// //////////////////////////////////////////////////////////////////////////

	public ImageView getRefreshButton() {
		return refreshButton;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		ctx = this;

		super.onCreate(savedInstanceState);
		DLog.i("FiltersListActivity",
		        "onCreate() FiltersListActivity <-- I'm here!");
		// Activity isnt restarting on change orientation look manifest
		setContentView(R.layout.filters_activity_layout);
		pagerAdapter = new MyPagerAdapter(new ViewsForFilters(this,
		        this.getBaseContext(), connectorFilters));
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOffscreenPageLimit(10);
		viewPager.setCurrentItem(0);
		indicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
		indicator.init(0, pagerAdapter.getCount(), pagerAdapter);
		// Getting out collors
		int color = getResources().getColor(R.color.page_indicator);
		indicator.setFocusedTextColor(indicator.getColorsFromInt(color));

		TextView txtView = (TextView) findViewById(R.id.text);
		txtView.setText(R.string.filters);

		ImageView homeButton = (ImageView) findViewById(R.id.image_home);
		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DLog.i("Filters Home", "Home button was pressed.");
				// Ends current activity
				finish();
			}
		});
		refreshButton = (ImageView) findViewById(R.id.image_refresh);
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int curitem = viewPager.getCurrentItem();
				pagerAdapter = new MyPagerAdapter(new ViewsForFilters(
				        filtersListActivity, filtersListActivity
				                .getBaseContext(), connectorFilters));
				viewPager.setAdapter(pagerAdapter);
				viewPager.setOffscreenPageLimit(10);
				viewPager.setCurrentItem(curitem, false);
				indicator.init(curitem, pagerAdapter.getCount(), pagerAdapter);
				int color = getResources().getColor(R.color.page_indicator);
				indicator.setFocusedTextColor(indicator.getColorsFromInt(color));

			}
		});
		viewPager.setOnPageChangeListener(indicator);
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
				        connector.jiraLogout();
				        finish();
				        return true;
			        }
		        });

		menuItemHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				HTMLDialog dialog = new HTMLDialog(ctx, "filters_help.html");
				dialog.show();
				return false;
			}
		});

		return true;
	}
}
