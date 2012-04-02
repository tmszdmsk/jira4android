package com.jira4android.connectors;

import java.net.URL;

import jira.For.Android.Connector.SoapObjectBuilder;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.Transport;

import roboguice.inject.ContextScoped;

import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;


public class AuthenthicationService {

	private static final String TAG = AuthenthicationService.class.getName();

	@Inject
	private KSoapExecutor soap;
	private String token;
	private URL jiraInstanceURL;

	public void login(String username, String password, URL jiraInstanceURL)
	        throws AuthenticationException, CommunicationException,
	        AuthorizationException, IllegalStateException {
		if(isLoggedIn()){
			throw new IllegalStateException("Called login but already logged in");
		}
		SoapObject loginRequest = SoapObjectBuilder.start()
				.withMethod("login")
		        .withProperty("username", username)
		        .withProperty("password", password)
		        .build();

		this.token = soap.execute(loginRequest, jiraInstanceURL, String.class);
		this.jiraInstanceURL = jiraInstanceURL;
	}
	
	public void logout() throws IllegalStateException{
		if(!isLoggedIn()){
			throw new IllegalStateException("Called logout when not logged in");
		}
		SoapObject logoutRequest = SoapObjectBuilder.start().
				withMethod("logout").
				withProperty("token", token).
				build();
		try {
	        soap.execute(logoutRequest, this.jiraInstanceURL);
        } catch (CommunicationException e) {
        	Log.w(TAG, "Communication exception during logout\n"+e);
        } catch (AuthorizationException e) {
        	Log.w(TAG, "AuthorizationException during logout\n"+e);
        } catch (AuthenticationException e) {
        	Log.w(TAG, "AuthenticationException during logout\n"+e);
        } finally{
        	token=null;
        	jiraInstanceURL=null;
        }
	}
	
	private boolean isLoggedIn(){
		return token!=null;
	}

	@Deprecated
	public String getToken(){
		return token;
	}
}
