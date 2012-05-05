package jira.For.Android.Connector;

import java.util.HashMap;
import java.util.Vector;

import jira.For.Android.DLog;
import jira.For.Android.DataTypes.Status;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

@Singleton
public class ConnectorStatus {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	public HashMap<String, Status> jiraGetStatuses()
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {

		DLog.i("status", "jestem w connector status");

		SoapObject getStatuses = SoapObjectBuilder.start()
		        .withMethod("getStatuses")
		        .withProperty("token", connector.getToken()).build();

		return downloadFromServer(getStatuses);

	}

	private HashMap<String, Status>
	        downloadFromServer(SoapObject getPriorities)
	                throws CommunicationException, AuthorizationException,
	                AuthenticationException {

		Vector<SoapObject> vc = soap.execute(getPriorities, Vector.class);
		if (vc == null) {
			return null;
		}

		HashMap<String, Status> statuses = new HashMap<String, Status>();
		String tmp;
		SoapObject p;
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);

			tmp = p.getPropertySafelyAsString("id");
			statuses.put(
			        tmp,
			        new Status(tmp, p.getPropertySafelyAsString("name"), p
			                .getPropertySafelyAsString("description"), p
			                .getPropertySafelyAsString("icon")));
		}
		return statuses;
	}
}
