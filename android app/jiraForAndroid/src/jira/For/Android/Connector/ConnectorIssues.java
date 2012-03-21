package jira.For.Android.Connector;

import java.io.IOException;
import java.util.Vector;

import jira.For.Android.DataTypes.Issue;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

class ConnectorIssues {

	private Connector connector;

	public ConnectorIssues() {
		connector = Connector.getInstance();
	}

	Issue[] jiraGetIssues(String query, int howMuch) throws IOException,
	        XmlPullParserException, Exception {

		if (query == null || howMuch < 0) throw new Exception(
		        "Bad arguments\nQuery:" + query + " howMuch:" + howMuch);

		SoapObject getIssues = new SoapObject(connector.getNameSpace(),
		        "getIssuesFromJqlSearch");
		getIssues.addProperty("token", connector.getToken());
		getIssues.addProperty("jqlSearch", query);
		getIssues.addProperty("maxResults", howMuch);

		return downloadFromServer(getIssues);
	}

	Issue[] jiraGetIssuesFromFilterWithLimit(String filterId, int offSet,
	        int maxNumResults) throws IOException, XmlPullParserException,
	        Exception {

		if (filterId == null || offSet < 0 || maxNumResults < 0) throw new Exception(
		        "Wrong arguments\nfilterId:" + filterId + " offSet:" + offSet
		                + " maxNumResults:" + maxNumResults);

		SoapObject getIssuesFromFilter = new SoapObject(
		        connector.getNameSpace(), "getIssuesFromFilterWithLimit");
		getIssuesFromFilter.addProperty("token", connector.getToken());
		getIssuesFromFilter.addProperty("filterId", filterId);
		getIssuesFromFilter.addProperty("offSet", offSet);
		getIssuesFromFilter.addProperty("maxNumResults", maxNumResults);

		return downloadFromServer(getIssuesFromFilter);
	}

	private Issue[] downloadFromServer(SoapObject getIssues) throws IOException, XmlPullParserException, RemoteException {

		SoapSerializationEnvelope envelope = connector
		        .getResponseFromServer(getIssues);

		Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
		if (vc == null) return null;

		Issue[] issues = new Issue[vc.size()];

		SoapObject p;// Pomocnicza zmienna!
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);
			
			String assigneeFullName;
			String assignee = p.getPropertySafelyAsString("assignee");
			if (assignee == null || assignee.compareToIgnoreCase("null") == 0)
				assigneeFullName = "Unassigned";
			else
				assigneeFullName = connector.downloadUserInformation(assignee).getFullname();
			String reporterFullName;
			String reporter = p.getPropertySafelyAsString("reporter");
			if (reporter == null || reporter.compareToIgnoreCase("null") == 0)
				reporterFullName = "NO REPORTER";
			else
				reporterFullName = connector.downloadUserInformation(reporter).getFullname();

			issues[i] = new Issue(p.getPropertySafelyAsString("id"),
					assignee,
					assigneeFullName,
			        p.getPropertySafelyAsString("description"),
			        p.getPropertySafelyAsString("updated"),
			        p.getPropertySafelyAsString("created"),
			        p.getPropertySafelyAsString("duedate"),
			        p.getPropertySafelyAsString("environment"),
			        p.getPropertySafelyAsString("key"),
			        p.getPropertySafelyAsString("priority"),
			        p.getPropertySafelyAsString("project"),
			        reporter,
			        reporterFullName,
			        p.getPropertySafelyAsString("resolution"),
			        p.getPropertySafelyAsString("status"),
			        p.getPropertySafelyAsString("summary"),
			        p.getPropertySafelyAsString("type"), Long.parseLong(p
			                .getPropertySafelyAsString("votes")));
		}
		return issues;
	}

	// TODO Naprawić przyszły bug
	/*-
	http://confluence.atlassian.com/display/JIRA042/Advanced+Searching?clicked=jirahelp#AdvancedSearching-Parent
	Only available if sub-tasks have been enabled by your JIRA administrator.

	Search for all sub-tasks of a particular issue. You can search by Issue Key or by Issue ID (i.e. the number that JIRA automatically allocates to an Issue).

	Note: this field does not support auto-complete.
	 */
}
