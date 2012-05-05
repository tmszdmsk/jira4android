package jira.For.Android.Connector;

import jira.For.Android.DataTypes.User;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

@Singleton
public class ConnectorUser {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	User jiraGetUser(String username) throws CommunicationException,
	        AuthorizationException, AuthenticationException {

		if (username == null) {
			throw new IllegalArgumentException("Username cannot be null");
		}

		SoapObject getUser = SoapObjectBuilder.start().withMethod("getUser")
		        .withProperty("token", connector.getToken())
		        .withProperty("username", username).build();

		SoapObject body = soap.execute(getUser, SoapObject.class);

		System.out.println("Mam usera: " + body);

		return new User(body.getPropertySafelyAsString("name"),
		        body.getPropertySafelyAsString("fullname"),
		        body.getPropertySafelyAsString("email"));
	}
}
