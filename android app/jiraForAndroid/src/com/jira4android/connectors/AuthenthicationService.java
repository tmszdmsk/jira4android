package com.jira4android.connectors;

import java.net.URL;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.connectors.utils.KSoapExecutor;
import com.jira4android.connectors.utils.SoapObjectBuilder;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

@Singleton
public class AuthenthicationService {

	private static final String TAG = AuthenthicationService.class.getName();

	@Inject
	private KSoapExecutor soap;
	private String token;
	private URL jiraInstanceURL;

	public void login(String username, String password, URL jiraInstanceURL)
	        throws AuthenticationException, CommunicationException,
	        AuthorizationException, IllegalStateException {
		SoapObject loginRequest = SoapObjectBuilder.start().withMethod("login")
		        .withProperty("username", username)
		        .withProperty("password", password).build();

		this.token = soap.execute(loginRequest, jiraInstanceURL, String.class);
		this.jiraInstanceURL = jiraInstanceURL;
	}


	public void logout() throws IllegalStateException {
		SoapObject logoutRequest = SoapObjectBuilder.start()
		        .withMethod("logout").withProperty("token", token).build();
		try {
			soap.execute(logoutRequest, this.jiraInstanceURL);
		} catch (CommunicationException e) {
			Log.w(TAG, "Communication exception during logout\n" + e);
		} catch (AuthorizationException e) {
			Log.w(TAG, "AuthorizationException during logout\n" + e);
		} catch (AuthenticationException e) {
			Log.w(TAG, "AuthenticationException during logout\n" + e);
		} finally {
			token = null;
			jiraInstanceURL = null;
		}
	}

	public URL getServerUrl() {
		if(jiraInstanceURL==null){
			throw new IllegalStateException("cannot return server url because not connected to any, some went wrong");
		}
		return jiraInstanceURL;
	}

	@Deprecated
	public String getToken() {
		return token;
	}

}
