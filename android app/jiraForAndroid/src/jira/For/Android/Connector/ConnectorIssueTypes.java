package jira.For.Android.Connector;

import java.util.HashMap;
import java.util.Vector;

import jira.For.Android.DataTypes.IssueType;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

@Singleton
public class ConnectorIssueTypes {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	HashMap<String, IssueType> jiraGetIssueTypes()
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {

		SoapObject getIssueTypes = SoapObjectBuilder.start()
		        .withMethod("getIssueTypes")
		        .withProperty("token", connector.getToken()).build();

		SoapObject getSubTaskIssueTypes = SoapObjectBuilder.start()
		        .withMethod("getSubTaskIssueTypes")
		        .withProperty("token", connector.getToken()).build();

		HashMap<String, IssueType> issueTypesAll = new HashMap<String, IssueType>();

		issueTypesAll.putAll(downloadFromServer(getIssueTypes));
		issueTypesAll.putAll(downloadFromServer(getSubTaskIssueTypes));
		return issueTypesAll;

	}

	private HashMap<String, IssueType> downloadFromServer(
	        SoapObject getIssueTypes) throws CommunicationException,
	        AuthorizationException, AuthenticationException {

		Vector<SoapObject> vc = soap.execute(getIssueTypes, Vector.class);

		if (vc == null) {
			return null;
		}

		HashMap<String, IssueType> issueTypes = new HashMap<String, IssueType>();

		SoapObject p;// Temporary variable!
		String tmp;
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);

			issueTypes.put(
			        tmp = p.getPropertySafelyAsString("id"),
			        new IssueType(tmp, p.getPropertySafelyAsString("name"), p
			                .getPropertySafelyAsString("description"), p
			                .getPropertySafelyAsString("icon"), Boolean
			                .parseBoolean(p
			                        .getPropertySafelyAsString("subTask"))));
		}

		return issueTypes;

	}
}
