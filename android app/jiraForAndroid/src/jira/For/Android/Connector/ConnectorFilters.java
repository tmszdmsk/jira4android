package jira.For.Android.Connector;

import java.io.IOException;
import java.util.Vector;

import jira.For.Android.DataTypes.Filter;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.jira4android.connectors.KSoapExecutor;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

class ConnectorFilters {

	private Connector connector;
	private KSoapExecutor soap = new KSoapExecutor();
	ConnectorFilters() {
		connector = Connector.getInstance();
	}

	Filter[] jiraGetFilters() throws CommunicationException, AuthorizationException, AuthenticationException{

		SoapObject getFavouriteFilters = new SoapObject(
		        connector.getNameSpace(), "getFavouriteFilters");
		getFavouriteFilters.addProperty("token", connector.getToken());

		Vector<SoapObject> vc = soap.execute(getFavouriteFilters, connector.instanceURL, Vector.class);
		if (vc == null) return null;

		Filter[] filters = new Filter[vc.size()];

		SoapObject p;// Pomocnicza zmienna!
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);

			// TODO zastanowić się czy potyrzebne będzie wyciąganie tych pól czy
			// wystarczą nam niektóre
			filters[i] = new Filter(p.getPropertySafelyAsString("author"),
			        p.getPropertySafelyAsString("description"),
			        p.getPropertySafelyAsString("project"),
			        p.getPropertySafelyAsString("name"),
			        p.getPropertySafelyAsString("id"));
		}

		return filters;
	}
}
