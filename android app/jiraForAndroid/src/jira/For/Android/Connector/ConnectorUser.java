package jira.For.Android.Connector;

import java.io.IOException;
import jira.For.Android.DataTypes.User;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.jira4android.connectors.KSoapExecutor;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

public class ConnectorUser {

    private Connector connector;
    private KSoapExecutor soap = new KSoapExecutor();

    public ConnectorUser() {
        connector = Connector.getInstance();
    }

    User jiraGetUser(String username) throws CommunicationException, AuthorizationException, AuthenticationException {

        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        SoapObject getUser = new SoapObject(connector.getNameSpace(), "getUser");
        getUser.addProperty("token", connector.getToken());
        getUser.addProperty("username", username);

        SoapObject body = soap.execute(getUser, connector.instanceURL, SoapObject.class);

        System.out.println("Mam usera: " + body);

        return new User(body.getPropertySafelyAsString("name"),
                body.getPropertySafelyAsString("fullname"),
                body.getPropertySafelyAsString("email"));
    }
}
