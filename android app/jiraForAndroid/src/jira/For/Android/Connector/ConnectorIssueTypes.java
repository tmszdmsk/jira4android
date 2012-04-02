package jira.For.Android.Connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import jira.For.Android.DataTypes.IssueType;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.jira4android.connectors.KSoapExecutor;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

class ConnectorIssueTypes {

	private Connector connector;

	public ConnectorIssueTypes() {
		connector = Connector.getInstance();
	}

	HashMap<String, IssueType> jiraGetIssueTypes() throws IOException,
            XmlPullParserException, 
            RemoteException {

        SoapObject getIssueTypes = SoapObjectBuilder.start().withMethod("getIssueTypes").
        		withProperty("token", connector.getToken()).build();

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
	        XmlPullParserException, RemoteException {

		KSoapExecutor soap = new KSoapExecutor();
		Vector<SoapObject> vc=null;
        try {
	        vc = soap.execute(getIssueTypes, Connector.instanceURL, Vector.class);
        } catch (CommunicationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } catch (AuthorizationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } catch (AuthenticationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
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
			        new IssueType(tmp, p.getPropertySafelyAsString("name"), p
			                .getPropertySafelyAsString("description"), p
			                .getPropertySafelyAsString("icon"), Boolean
			                .parseBoolean(p
			                        .getPropertySafelyAsString("subTask"))));
		}

		return issueTypes;

	}
}
