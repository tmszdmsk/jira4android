package jira.For.Android.Connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import jira.For.Android.DLog;
import jira.For.Android.DataTypes.Priority;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.jira4android.connectors.KSoapExecutor;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

class ConnectorPriority {

	private Connector connector;
	private KSoapExecutor soap = new KSoapExecutor();

	ConnectorPriority() {
		connector = Connector.getInstance();
	}

	HashMap<String, Priority> jiraGetPriorities() throws RemoteException,
	        CommunicationException, AuthorizationException,
	        AuthenticationException {

		SoapObject getPriorities = new SoapObject(connector.getNameSpace(),
		        "getPriorities");
		getPriorities.addProperty("token", connector.getToken());

		return downloadFromServer(getPriorities);

	}

	private HashMap<String, Priority> downloadFromServer(
	        SoapObject getPriorities) throws RemoteException,
	        CommunicationException, AuthorizationException,
	        AuthenticationException {

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
