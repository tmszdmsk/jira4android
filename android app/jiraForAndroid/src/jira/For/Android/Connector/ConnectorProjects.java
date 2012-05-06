package jira.For.Android.Connector;

import java.util.Vector;

import jira.For.Android.DataTypes.Project;

import org.ksoap2.serialization.SoapObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

@Singleton
public class ConnectorProjects {

	@Inject
	private Connector connector;
	@Inject
	private KSoapExecutor soap;

	public Project[] getProjects(boolean downloadAvatars)
	        throws CommunicationException, AuthorizationException,
	        AuthenticationException {

		SoapObject getProjects = SoapObjectBuilder.start()
		        .withMethod("getProjectsNoSchemes")
		        .withProperty("token", connector.getToken()).build();

		Vector<SoapObject> vc = soap.execute(getProjects, Vector.class);
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

	void jiraGetProjectAvatar(Project project) throws CommunicationException,
	        AuthorizationException, AuthenticationException {

		SoapObject getAvatar = SoapObjectBuilder.start()
		        .withMethod("getProjectAvatar")
		        .withProperty("token", connector.getToken())
		        .withProperty("projectKey", project.getKey()).build();

		SoapObject body = soap.execute(getAvatar, SoapObject.class);

		// TODO Trzeba sprawdzić czy rysunek nie jest tym rysunkiem standardowym
		// albo czy już go nie mamy w pamięci ;) np spr pole id lkub inne info
		// wykorzystać
		project.setAvatar(body.getPropertySafelyAsString("base64Data"));
	}
}
