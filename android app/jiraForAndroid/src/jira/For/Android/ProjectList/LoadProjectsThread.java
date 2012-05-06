package jira.For.Android.ProjectList;

import jira.For.Android.DLog;
import jira.For.Android.R;
import jira.For.Android.Thread;
import jira.For.Android.Connector.ConnectorProjects;
import jira.For.Android.DataTypes.Project;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class LoadProjectsThread extends Thread<Project[]> {

	private final ConnectorProjects connectorProjects;

	public LoadProjectsThread(View view,
	                          ProjectListActivity projectListActivity,
	                          ConnectorProjects connectorProjects) {
		super(view, projectListActivity);

		activity = projectListActivity;
		this.connectorProjects = connectorProjects;
		refresh = projectListActivity.getRefreshButton();
	}

	ImageView refresh;

	@Override
	protected void onPreExecute() {
		refresh.setEnabled(false);
		System.out.println("refresh set enabled: false");
		super.onPreExecute();
	}

	protected synchronized Project[] doInBackground(Void... params) {
		DLog.i("ProjectListActivity", "doInBackground() <-- i'm here");

		try {
			// TODO Trzeba obsłużyć czy uzytkownik chce pobierać rysunki do
			// projektów
			return connectorProjects.getProjects(true);
		} catch (Exception e) {
			setException(e);
		}
		return null;
	}

	protected synchronized void onPostExecute(Project[] result) {

		super.hideProgressBar();
		DLog.i("ProjectListActivity", "onPostExecute() <-- i'm here");

		// TODO Connector ma zwracać nulla jak nic nie ma naprawde!
		if (result == null) {
			result = new Project[0];
		}

		ProjectAdapter adapter = new ProjectAdapter(
		        activity,
		        ((ProjectListActivity) activity)
		                .getListOfStringsFromProjects(((ProjectListActivity) activity).projects = result),
		        ((ProjectListActivity) activity).projects);

		ListView listView = (ListView) activity
		        .findViewById(R.id.project_list_view);
		listView.setEmptyView(view.findViewById(R.id.project_list_no_projects));
		listView.setVisibility(View.VISIBLE);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(((ProjectListActivity) activity));

		showFailInformation();
		refresh.setEnabled(true);
		System.out.println("refresh set enabled: true");
	}
}
