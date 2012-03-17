package jira.For.Android.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jira.For.Android.DataTypes.DataTypesMethods;
import jira.For.Android.DataTypes.WorkLog;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

class ConnectorWorkLog {

	private Connector connector;

	ConnectorWorkLog() {
		connector = Connector.getInstance();
	}
	
	synchronized List<WorkLog> jiraGetWorklogs(String issueKey) throws IOException,
			XmlPullParserException, Exception {
		
		if (issueKey == null) throw new Exception(
		        "Bad arguments\nIssue key:" + issueKey);
		
		SoapObject getWorklogs = new SoapObject(connector.getNameSpace(),
		        "getWorklogs");
		getWorklogs.addProperty("token", connector.getToken());
		getWorklogs.addProperty("issueKey", issueKey);
		
		return downloadFromServer(getWorklogs);
		
	}
	
	private synchronized List<WorkLog> downloadFromServer(SoapObject getWorklogs) throws Exception {
		
		SoapSerializationEnvelope envelope = connector
		        .getResponseFromServer(getWorklogs);

		Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
		if (vc == null) return null;
		Log.e("worklogi", Integer.toString(vc.size()));
		List<WorkLog> worklogs = new ArrayList<WorkLog>();

		SoapObject p;// Pomocnicza zmienna!
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);
			
			String author = p.getPropertySafelyAsString("author");
			if (author == null || author.compareToIgnoreCase("null") == 0) System.out
			        .println("Ej no tak nie powinno być!!!!!!!!!");
			String created = p.getPropertySafelyAsString("created");
			if (created == null || created.compareToIgnoreCase("null") == 0) System.out
	        		.println("Ej no tak nie powinno być!!!!!!!!!");
			String startDate = p.getPropertySafelyAsString("startDate");
			if (startDate == null || startDate.compareToIgnoreCase("null") == 0) System.out
    				.println("Ej no tak nie powinno być!!!!!!!!!");
			String updated = p.getPropertySafelyAsString("updated");
			if (updated == null || updated.compareToIgnoreCase("null") == 0) System.out
					.println("Ej no tak nie powinno być!!!!!!!!!");

			worklogs.add(new WorkLog(connector.downloadUserInformation(author),
			        p.getPropertySafelyAsString("comment"),
			        p.getPropertySafelyAsString("timeSpent"),
			        p.getPropertySafelyAsString("updateAuthor"),
			        DataTypesMethods.GMTStringToLocalDate(created),
			        DataTypesMethods.GMTStringToLocalDate(startDate),
			        DataTypesMethods.GMTStringToLocalDate(updated),
			        Long.parseLong(p.getPropertySafelyAsString("timeSpentInSeconds"))));
		}
		
		Log.e("worklogi1", Integer.toString(worklogs.size()));
		
		return worklogs;
		
	}

}