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

class ConnectorStatus {

    private Connector connector;

    ConnectorStatus() {
        connector = Connector.getInstance();
    }

    HashMap<String, Status> jiraGetStatuses() throws IOException,
            XmlPullParserException, 
            RemoteException {

        DLog.i("status", "jestem w connector status");

        SoapObject getStatuses = new SoapObject(connector.getNameSpace(),
                "getStatuses");
        getStatuses.addProperty("token", connector.getToken());

        return downloadFromServer(getStatuses);

    }

    private HashMap<String, Status> downloadFromServer(
            SoapObject getPriorities) throws IOException,
            XmlPullParserException, 
            RemoteException {

        SoapSerializationEnvelope envelope = connector.getResponseFromServer(getPriorities);

        Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
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
