package jira.For.Android.Connector;

import java.io.IOException;
import java.util.Vector;

import jira.For.Android.DataTypes.Filter;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

class ConnectorFilters {

	private Connector connector;

	ConnectorFilters() {
		connector = Connector.getInstance();
	}

	Filter[] jiraGetFilters() throws IOException, XmlPullParserException, RemoteException {

		SoapObject getFavouriteFilters = new SoapObject(
		        connector.getNameSpace(), "getFavouriteFilters");
		getFavouriteFilters.addProperty("token", connector.getToken());

		SoapSerializationEnvelope envelope = connector
		        .getResponseFromServer(getFavouriteFilters);

		Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
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
