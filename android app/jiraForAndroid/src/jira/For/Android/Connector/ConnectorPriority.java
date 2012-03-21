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

class ConnectorPriority {

    private Connector connector;

    ConnectorPriority() {
        connector = Connector.getInstance();
    }

    HashMap<String, Priority> jiraGetPriorities() throws IOException,
            XmlPullParserException, 
            RemoteException {

        SoapObject getPriorities = new SoapObject(connector.getNameSpace(),
                "getPriorities");
        getPriorities.addProperty("token", connector.getToken());

        return downloadFromServer(getPriorities);

    }

    private HashMap<String, Priority> downloadFromServer(
            SoapObject getPriorities) throws IOException,
            XmlPullParserException, 
            RemoteException {

        SoapSerializationEnvelope envelope = connector.getResponseFromServer(getPriorities);

        Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
        if (vc == null) {
            return null;
        }

        System.out.println("Odp serva: " + envelope.bodyIn);

        HashMap<String, Priority> priorities = new HashMap<String, Priority>();

        DLog.i("ilosc priorytetow", String.valueOf(vc.size()));

        String tmp;
        SoapObject p;// Temporary variable!
        for (int i = 0; i < vc.size(); ++i) {
            p = vc.get(i);

            tmp = p.getPropertySafelyAsString("id");
            priorities.put(
                    tmp,
                    new Priority(tmp, p.getPropertySafelyAsString("name"), p.getPropertySafelyAsString("description"), p.getPropertySafelyAsString("icon"), p.getPropertySafelyAsString("color")));
        }
        return priorities;

    }
}
