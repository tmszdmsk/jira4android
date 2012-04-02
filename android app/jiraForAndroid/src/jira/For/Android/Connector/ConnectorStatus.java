package jira.For.Android.Connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import jira.For.Android.DLog;
import jira.For.Android.DataTypes.Status;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.jira4android.connectors.KSoapExecutor;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

class ConnectorStatus {

    private Connector connector;
    private KSoapExecutor soap = new KSoapExecutor();
    ConnectorStatus() {
        connector = Connector.getInstance();
    }

    HashMap<String, Status> jiraGetStatuses() throws CommunicationException, AuthorizationException, AuthenticationException {

        DLog.i("status", "jestem w connector status");

        SoapObject getStatuses = new SoapObject(connector.getNameSpace(),
                "getStatuses");
        getStatuses.addProperty("token", connector.getToken());

        return downloadFromServer(getStatuses);

    }

    private HashMap<String, Status> downloadFromServer(
            SoapObject getPriorities) throws CommunicationException, AuthorizationException, AuthenticationException {


        Vector<SoapObject> vc = soap.execute(getPriorities, connector.instanceURL, Vector.class);
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
                    new Status(tmp, p.getPropertySafelyAsString("name"), p.getPropertySafelyAsString("description"), p.getPropertySafelyAsString("icon")));
        }
        return statuses;
    }
}
