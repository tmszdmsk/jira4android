package jira.For.Android.Connector;

import jira.For.Android.DataTypes.User;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class ConnectorUser {

	private Connector connector;

	public ConnectorUser() {
		connector = Connector.getInstance();
	}

	User jiraGetUser(String username) throws Exception {

		if (username == null) throw new Exception("Bad arguments\nusername:"
		        + username);

		SoapObject getUser = new SoapObject(connector.getNameSpace(), "getUser");
		getUser.addProperty("token", connector.getToken());
		getUser.addProperty("username", username);

		SoapSerializationEnvelope envelope = connector
		        .getResponseFromServer(getUser);

		SoapObject body = (SoapObject) envelope.getResponse();

		System.out.println("Mam usera: " + body);

		return new User(body.getPropertySafelyAsString("name"),
		        body.getPropertySafelyAsString("fullname"),
		        body.getPropertySafelyAsString("email"));
	}
}
