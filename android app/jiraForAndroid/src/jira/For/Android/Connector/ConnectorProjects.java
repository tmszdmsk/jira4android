package jira.For.Android.Connector;

import java.io.IOException;
import java.util.Vector;

import jira.For.Android.DataTypes.Project;
import jira.For.Android.RemoteExceptions.RemoteException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import com.jira4android.connectors.KSoapExecutor;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

class ConnectorProjects {

	private Connector connector;
	private KSoapExecutor soap = new KSoapExecutor();

	// Powyżej jest envelope bo będziemy wyciągać image itp do projektów

	ConnectorProjects() {
		connector = Connector.getInstance();
	}

	Project[] jiraGetProjects(boolean downloadAvatars) throws CommunicationException, AuthorizationException, AuthenticationException{

		SoapObject getProjects = new SoapObject(connector.getNameSpace(),
		        "getProjectsNoSchemes");
		getProjects.addProperty("token", connector.getToken());

	

		Vector<SoapObject> vc = soap.execute(getProjects, connector.instanceURL, Vector.class);
		if (vc == null) return null;

		Project[] projects = new Project[vc.size()];

		SoapObject p;// Pomocnicza zmienna!
		for (int i = 0; i < vc.size(); ++i) {
			p = vc.get(i);

			// Creating new project with all fields
			projects[i] = new Project(
			        p.getPropertySafelyAsString("description"),
			        p.getPropertySafelyAsString("id"),
			        p.getPropertySafelyAsString("issueSecurityScheme"),
			        p.getPropertySafelyAsString("key"),
			        p.getPropertySafelyAsString("lead"),
			        p.getPropertySafelyAsString("name"),
			        p.getPropertySafelyAsString("notificationScheme"),
			        p.getPropertySafelyAsString("permissionScheme"),
			        p.getPropertySafelyAsString("projectUrl"),
			        p.getPropertySafelyAsString("url"));

			if (downloadAvatars) {
				jiraGetProjectAvatar(projects[i]);
			}
		}
		return projects;
	}

	void jiraGetProjectAvatar(Project project) throws CommunicationException, AuthorizationException, AuthenticationException{

		SoapObject getAvatar = new SoapObject(connector.getNameSpace(),
		        "getProjectAvatar");
		getAvatar.addProperty("token", connector.getToken());
		getAvatar.addProperty("projectKey", project.getKey());



		SoapObject body =  soap.execute(getAvatar, connector.instanceURL,SoapObject.class);

		// TODO Trzeba sprawdzić czy rysunek nie jest tym rysunkiem standardowym
		// albo czy już go nie mamy w pamięci ;) np spr pole id lkub inne info
		// wykorzystać
		project.setAvatar(body.getPropertySafelyAsString("base64Data"));
	}
}
