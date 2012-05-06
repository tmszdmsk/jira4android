package jira.For.Android.Connector;

import java.util.Vector;

import jira.For.Android.DataTypes.Issue;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

@Singleton
public class ConnectorIssues {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	public Issue[] getIssuesByJQL(String query, int howMuch)
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {

		if (query == null || howMuch < 0) throw new IllegalArgumentException(
		        "Bad arguments\nQuery:" + query + " howMuch:" + howMuch);

		SoapObject getIssues = SoapObjectBuilder.start()
		        .withMethod("getIssuesFromJqlSearch")
		        .withProperty("token", connector.getToken())
		        .withProperty("jqlSearch", query)
		        .withProperty("maxResults", howMuch).build();

		return downloadFromServer(getIssues);
	}

	public Issue[] getIssuesFromFilterWithLimit(String filterId, int offSet,
	        int maxNumResults) throws CommunicationException,
	        AuthorizationException, AuthenticationException {

		if (filterId == null || offSet < 0 || maxNumResults < 0) throw new IllegalArgumentException(
		        "Wrong arguments\nfilterId:" + filterId + " offSet:" + offSet
		                + " maxNumResults:" + maxNumResults);

		SoapObject getIssuesFromFilter = SoapObjectBuilder.start()
		        .withMethod("getIssuesFromFilterWithLimit")
		        .withProperty("token", connector.getToken())
		        .withProperty("filterId", filterId)
		        .withProperty("offSet", offSet)
		        .withProperty("maxNumResults", maxNumResults).build();

		return downloadFromServer(getIssuesFromFilter);
	}

	private Issue[] downloadFromServer(SoapObject getIssues)
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {

		Vector<SoapObject> vc = soap.execute(getIssues, Vector.class);
		if (vc == null) return null;

		Issue[] issues = new Issue[vc.size()];

		SoapObject p;// Pomocnicza zmienna!
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);

			String assigneeFullName;
			String assignee = p.getPropertySafelyAsString("assignee");
			if (assignee == null || assignee.compareToIgnoreCase("null") == 0) assigneeFullName = "Unassigned";
			else assigneeFullName = connector.downloadUserInformation(assignee)
			        .getFullname();
			String reporterFullName;
			String reporter = p.getPropertySafelyAsString("reporter");
			if (reporter == null || reporter.compareToIgnoreCase("null") == 0) reporterFullName = "NO REPORTER";
			else reporterFullName = connector.downloadUserInformation(reporter)
			        .getFullname();

			issues[i] = new Issue(p.getPropertySafelyAsString("id"), assignee,
			        assigneeFullName,
			        p.getPropertySafelyAsString("description"),
			        p.getPropertySafelyAsString("updated"),
			        p.getPropertySafelyAsString("created"),
			        p.getPropertySafelyAsString("duedate"),
			        p.getPropertySafelyAsString("environment"),
			        p.getPropertySafelyAsString("key"),
			        p.getPropertySafelyAsString("priority"),
			        p.getPropertySafelyAsString("project"), reporter,
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
