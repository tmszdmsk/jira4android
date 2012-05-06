package jira.For.Android.Filters;

import jira.For.Android.DLog;
import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorFilters;
import jira.For.Android.DataTypes.Filter;
import jira.For.Android.PagerView.ViewForPagerInterface;
import jira.For.Android.TaskList.TaskListByJQLActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

class ViewsForFilters implements ViewForPagerInterface {

	public ViewsForFilters(FiltersListActivity filtersListActivity, Context ctx, ConnectorFilters connectorFilters) {

		this.connectorFilters = connectorFilters;
		DLog.i("JiraPagerAdapter",
		        "Creating JiraPagerAdapter constructor <--- I'm here");
		this.ctx = ctx;
		Filter[] tmp = {
		        new Filter(ctx.getString(R.string.filters_list_1_name),
		                ctx.getString(R.string.filters_list_1_JQL)),
		        new Filter(ctx.getString(R.string.filters_list_2_name),
		                ctx.getString(R.string.filters_list_2_JQL)),
		        new Filter(ctx.getString(R.string.filters_list_3_name),
		                ctx.getString(R.string.filters_list_3_JQL)),
		        new Filter(ctx.getString(R.string.filters_list_4_name),
		                ctx.getString(R.string.filters_list_4_JQL)),
		        new Filter(ctx.getString(R.string.filters_list_5_name),
		                ctx.getString(R.string.filters_list_5_JQL)),
		        new Filter(ctx.getString(R.string.filters_list_6_name),
		                ctx.getString(R.string.filters_list_6_JQL)),
		        new Filter(ctx.getString(R.string.filters_list_7_name),
		                ctx.getString(R.string.filters_list_7_JQL)),
		        new Filter(ctx.getString(R.string.filters_list_8_name),
		                ctx.getString(R.string.filters_list_8_JQL))};
		filtersLocal = tmp;
		String[] tmp1 = {ctx.getString(R.string.filters_title1),
		        ctx.getString(R.string.filters_title2)};
		titles = tmp1;
		this.filtersListActivity = filtersListActivity;
		filtersLocalList = getAdapterFromFilters(filtersLocal);
	}

	ArrayAdapter<String> filtersLocalList;
	ArrayAdapter<String> filtersRemoteList;
	FiltersListActivity filtersListActivity;
	Context ctx;

	/*
	 * private final String[] titles = {ctx.getString(R.string.filters_title1),
	 * ctx.getString(R.string.filters_title2)};
	 */
	private String[] titles;
	Filter[] filtersRemote;
	Filter[] filtersLocal;
	private ConnectorFilters connectorFilters;

	/*
	 * final Filter[] filtersLocal = { new
	 * Filter(ctx.getString(R.string.filters_list_1_name),
	 * ctx.getString(R.string.filters_list_1_JQL)), new
	 * Filter(ctx.getString(R.string.filters_list_2_name),
	 * ctx.getString(R.string.filters_list_2_JQL)), new
	 * Filter(ctx.getString(R.string.filters_list_3_name),
	 * ctx.getString(R.string.filters_list_3_JQL)), new
	 * Filter(ctx.getString(R.string.filters_list_4_name),
	 * ctx.getString(R.string.filters_list_4_JQL)), new
	 * Filter(ctx.getString(R.string.filters_list_5_name),
	 * ctx.getString(R.string.filters_list_5_JQL)), new
	 * Filter(ctx.getString(R.string.filters_list_6_name),
	 * ctx.getString(R.string.filters_list_6_JQL)), new
	 * Filter(ctx.getString(R.string.filters_list_7_name),
	 * ctx.getString(R.string.filters_list_7_JQL)), new
	 * Filter(ctx.getString(R.string.filters_list_8_name),
	 * ctx.getString(R.string.filters_list_8_JQL))};
	 */

	ArrayAdapter<String> getAdapterFromFilters(Filter[] filters) {

		if (filters == null) return null;
		String fooFilters = "";
		// Getting filters names ,contatening them with new line then split
		// them
		// to get simple array of strings

		for (int i = 0; i < filters.length; ++i) {
			fooFilters += (filters[i].getName() + "\n");
		}

		return new ArrayAdapter<String>(filtersListActivity,
		        R.layout.simple_black_font_row, R.id.simple_black_TextView,
		        fooFilters.split("\n"));
	}

	@Override
	public String getTitle(int a) {
		return titles[a];
	}

	@Override
	public View loadView(LayoutInflater inflater, int pos) {
		ArrayAdapter<String> adapter = null;
		View tmpview = inflater.inflate(R.layout.filters_list, null);
		switch (pos) {

			case 0:
				// Page with local filters
				adapter = filtersLocalList;
			break;
			case 1:
				// Page with remote filters
				if (filtersRemote == null) new LoadRemoteFiltersThread(tmpview,
				        filtersListActivity, this, connectorFilters).execute();
				else adapter = filtersRemoteList;
			break;
		}

		View view = setListView(tmpview, adapter);
		return view;
	}

	View setWhatIsVisibleInLayout(View view, ArrayAdapter<String> adapter) {

		if (adapter == null) return view;

		view.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.data_is_loading).setVisibility(View.INVISIBLE);

		if (adapter.getItem(0).trim().length() != 0) {
			ListView listView = (ListView) view
			        .findViewById(R.id.filters_list_view_filters);
			listView.setVisibility(View.VISIBLE);
			listView.setAdapter(adapter);
			listView.setClickable(true);
			listView.setOnItemClickListener(new AdapterFilters(
			        filtersListActivity, this));
		}
		else {
			TextView textView = (TextView) view
			        .findViewById(R.id.filters_list_no_filters_view);
			textView.setVisibility(View.VISIBLE);
		}
		return view;
	}

	private View setListView(View view, ArrayAdapter<String> adapter) {
		return setWhatIsVisibleInLayout(view, adapter);
	}

	@Override
	public int getLength() {
		return titles.length;
	}

}

class AdapterFilters implements AdapterView.OnItemClickListener {

	private FiltersListActivity filtersList;
	private ViewsForFilters viewsForFilters;

	AdapterFilters(FiltersListActivity filtersList,
	               ViewsForFilters viewsForFilters) {
		this.filtersList = filtersList;
		this.viewsForFilters = viewsForFilters;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

		DLog.i("AdapterFilters", "AdapterFilters onItemClick() <-- I'm here");

		Bundle extras = filtersList.getIntent().getExtras();

		Filter[] currentFilterPage = null;
		switch (filtersList.indicator.getCurrentPosition()) {
			case 0:
				currentFilterPage = viewsForFilters.filtersLocal;
			break;
			case 1:
				currentFilterPage = viewsForFilters.filtersRemote;
			break;
		}
		Intent intent = new Intent(filtersList, TaskListByJQLActivity.class);

		// Tylko filtry z servera mają to pole ustawione na null
		// TODO zrobić jakoś ładniej rozróżnianie czy to jest filtr
		// z sewrvera czy lokalny

		if (currentFilterPage[pos].getJql() == null
		        && currentFilterPage[pos].getId() != null) {
			intent.putExtra("filterId", currentFilterPage[pos].getId());

			// TODO for tests only
			DLog.i("FiltersListActivity", "I selected: \n "
			        + currentFilterPage[pos].getName() + "\n" + "project="
			        + extras.getString("projectID") + " and filter id:"
			        + currentFilterPage[pos].getId());
		}
		else {

			String query = "project=" + extras.getString("projectID") + " and "
			        + currentFilterPage[pos].getJql();
			intent.putExtra("JQL", query);

			DLog.i("FiltersListActivity", "I selected: \n "
			        + currentFilterPage[pos].getName() + "\n" + query);
		}
		intent.putExtra("filterName", currentFilterPage[pos].getName());
		filtersList.startActivity(intent);
	}
}
