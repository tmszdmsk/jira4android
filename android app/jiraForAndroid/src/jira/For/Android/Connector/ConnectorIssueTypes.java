package jira.For.Android.Connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import jira.For.Android.DataTypes.IssueType;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

class ConnectorIssueTypes {

    private Connector connector;

    public ConnectorIssueTypes() {
        connector = Connector.getInstance();
    }

    HashMap<String, IssueType> jiraGetIssueTypes() throws IOException,
            XmlPullParserException, 
            RemoteException {

        SoapObject getIssueTypes = new SoapObject(connector.getNameSpace(),
                "getIssueTypes");
        getIssueTypes.addProperty("token", connector.getToken());

        SoapObject getSubTaskIssueTypes = new SoapObject(
                connector.getNameSpace(), "getSubTaskIssueTypes");
        getSubTaskIssueTypes.addProperty("token", connector.getToken());

        HashMap<String, IssueType> issueTypesAll = new HashMap<String, IssueType>();

        issueTypesAll.putAll(downloadFromServer(getIssueTypes));
        issueTypesAll.putAll(downloadFromServer(getSubTaskIssueTypes));
        return issueTypesAll;

    }

    private HashMap<String, IssueType> downloadFromServer(
            SoapObject getIssueTypes) throws IOException,
            XmlPullParserException,
            RemoteException {

        SoapSerializationEnvelope envelope = connector.getResponseFromServer(getIssueTypes);

        Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
        if (vc == null) {
            return null;
        }

        HashMap<String, IssueType> issueTypes = new HashMap<String, IssueType>();

        SoapObject p;// Temporary variable!
        String tmp;
        for (int i = 0; i < vc.size(); ++i) {
            p = vc.get(i);

            issueTypes.put(
                    tmp = p.getPropertySafelyAsString("id"),
                    new IssueType(tmp, p.getPropertySafelyAsString("name"), p.getPropertySafelyAsString("description"), p.getPropertySafelyAsString("icon"), Boolean.parseBoolean(p.getPropertySafelyAsString("subTask"))));
        }

        return issueTypes;

    }
}
