package jira.For.Android.Connector;

import java.io.IOException;
import jira.For.Android.DataTypes.User;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

public class ConnectorUser {

    private Connector connector;

    public ConnectorUser() {
        connector = Connector.getInstance();
    }

    User jiraGetUser(String username) throws IOException, XmlPullParserException, RemoteException {

        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        SoapObject getUser = new SoapObject(connector.getNameSpace(), "getUser");
        getUser.addProperty("token", connector.getToken());
        getUser.addProperty("username", username);

        SoapSerializationEnvelope envelope = connector.getResponseFromServer(getUser);

        SoapObject body = (SoapObject) envelope.getResponse();

        System.out.println("Mam usera: " + body);

        return new User(body.getPropertySafelyAsString("name"),
                body.getPropertySafelyAsString("fullname"),
                body.getPropertySafelyAsString("email"));
    }
}
