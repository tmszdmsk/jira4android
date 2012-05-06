package jira.For.Android.Filters;

import jira.For.Android.DLog;
import jira.For.Android.Thread;
import jira.For.Android.Connector.ConnectorFilters;
import jira.For.Android.DataTypes.Filter;
import android.view.View;
import android.widget.ImageView;

class LoadRemoteFiltersThread extends Thread<Filter[]> {

	private ViewsForFilters viewsForFilters;
	ImageView refresh;
	private final ConnectorFilters connectorFilters;

	public LoadRemoteFiltersThread(View view, FiltersListActivity activity,
	                         ViewsForFilters viewsForFilters, ConnectorFilters connectorFilters) {
		super(view, activity);

		this.viewsForFilters = viewsForFilters;
		this.connectorFilters = connectorFilters;
		refresh = activity.getRefreshButton();
	}
	
	@Override
	protected void onPreExecute() {
		refresh.setEnabled(false); System.out.println("refresh set enabled: false");
	    super.onPreExecute();
	}

	protected synchronized void onPostExecute(Filter[] result) {
		DLog.i("FiltersListActivity", "onPostExecute() <-- i'm here");

		hideProgressBar();
		refresh.setEnabled(true); System.out.println("refresh set enabled: true");
		viewsForFilters.filtersRemoteList = viewsForFilters
		        .getAdapterFromFilters(viewsForFilters.filtersRemote = result);

		// TODO Trzeba to jakoś ładnie zapisać a nie 0 w przyszłości
		// możemy niewiedzieć o którą stronę tu chodzi więc trzeba te 0
		// jakoś ładnie zamienić na coś innego (Przemyśleć)
		// TODO Exception musi też mówić np ,że nie udało się połączuć
		// Time Limit Eexceeded


		viewsForFilters.setWhatIsVisibleInLayout(super.view,
		        viewsForFilters.filtersRemoteList);

		showFailInformation();
	}

	protected synchronized Filter[] doInBackground(Void... params) {
		DLog.i("FiltersListActivity", "doInBackground() <-- i'm here");
		try {
			return connectorFilters.getFilters();
		} catch (Exception e) {
			setException(e);
		}
		return null;
	}
}
