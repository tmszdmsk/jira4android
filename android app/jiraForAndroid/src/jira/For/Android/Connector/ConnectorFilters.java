package jira.For.Android.Connector;

import java.util.Vector;

import jira.For.Android.DataTypes.Filter;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;
@Singleton
public class ConnectorFilters {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	public Filter[] getFilters() throws CommunicationException,
	        AuthorizationException, AuthenticationException {

		SoapObject getFavouriteFilters = SoapObjectBuilder.start()
		        .withMethod("getFavouriteFilters")
		        .withProperty("token", connector.getToken()).build();

		@SuppressWarnings("unchecked")
        Vector<SoapObject> vc = soap.execute(getFavouriteFilters, Vector.class);
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
