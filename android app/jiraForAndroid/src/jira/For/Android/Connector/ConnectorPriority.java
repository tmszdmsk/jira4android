package jira.For.Android.Connector;

import java.util.HashMap;
import java.util.Vector;

import jira.For.Android.DataTypes.Priority;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;
@Singleton
public class ConnectorPriority {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	HashMap<String, Priority> jiraGetPriorities()
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {

		SoapObject getPriorities = SoapObjectBuilder.start()
		        .withMethod("getPriorities")
		        .withProperty("token", connector.getToken()).build();

		return downloadFromServer(getPriorities);

	}

	private HashMap<String, Priority> downloadFromServer(
	        SoapObject getPriorities) throws CommunicationException,
	        AuthorizationException, AuthenticationException {

		Vector<SoapObject> vc = soap.execute(getPriorities,
		        connector.instanceURL, Vector.class);

		if (vc == null) {
			return null;
		}

		HashMap<String, Priority> priorities = new HashMap<String, Priority>();
		String tmp;
		SoapObject p;// Temporary variable!
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);

			tmp = p.getPropertySafelyAsString("id");
			priorities.put(
			        tmp,
			        new Priority(tmp, p.getPropertySafelyAsString("name"), p
			                .getPropertySafelyAsString("description"), p
			                .getPropertySafelyAsString("icon"), p
			                .getPropertySafelyAsString("color")));
		}
		return priorities;

	}
}
