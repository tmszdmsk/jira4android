package jira.For.Android.Connector;

import java.util.Vector;

import jira.For.Android.DataTypes.Project;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

class ConnectorProjects {

	private Connector connector;

	// Powyżej jest envelope bo będziemy wyciągać image itp do projektów

	ConnectorProjects() {
		connector = Connector.getInstance();
	}

	Project[] jiraGetProjects(boolean downloadAvatars) throws Exception {

		SoapObject getProjects = new SoapObject(connector.getNameSpace(),
		        "getProjectsNoSchemes");
		getProjects.addProperty("token", connector.getToken());

		SoapSerializationEnvelope envelope = connector
		        .getResponseFromServer(getProjects);

		Vector<SoapObject> vc = connector.getSoapObjectsFromResponse(envelope);
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

	void jiraGetProjectAvatar(Project project) throws Exception {

		SoapObject getAvatar = new SoapObject(connector.getNameSpace(),
		        "getProjectAvatar");
		getAvatar.addProperty("token", connector.getToken());
		getAvatar.addProperty("projectKey", project.getKey());

		SoapSerializationEnvelope envelope = connector
		        .getResponseFromServer(getAvatar);

		SoapObject body = (SoapObject) envelope.getResponse();

		// TODO Trzeba sprawdzić czy rysunek nie jest tym rysunkiem standardowym
		// albo czy już go nie mamy w pamięci ;) np spr pole id lkub inne info
		// wykorzystać
		project.setAvatar(body.getPropertySafelyAsString("base64Data"));
	}
}
