package com.jira4android.connectors.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import jira.For.Android.RemoteExceptions.RemoteAuthenticationException;
import jira.For.Android.RemoteExceptions.RemoteException;
import jira.For.Android.RemoteExceptions.RemotePermissionException;
import jira.For.Android.RemoteExceptions.RemoteValidationException;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.google.inject.Singleton;
import com.jira4android.exceptions.AuthenticationException;
import com.jira4android.exceptions.AuthorizationException;
import com.jira4android.exceptions.CommunicationException;

@Singleton
public class KSoapExecutor {

	private static final int TIMEOUT = 7000;
	private static final String WSDL_PATH = "/rpc/soap/jirasoapservice-v2?wsdl";

	@SuppressWarnings("unchecked")
	public <T> T execute(SoapObject soapObject, URL instanceURL,
	        Class<T> clazz) throws CommunicationException,
	        AuthorizationException, AuthenticationException {
		
		Log.i(KSoapExecutor.class.getName(), "executing command with soapObject:\n"+soapObject);
		Transport transport = getTransport(instanceURL);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
		        SoapSerializationEnvelope.VER11);
		envelope.setOutputSoapObject(soapObject);
		try {
			transport.call("", envelope);
		} catch (IOException e) {
			throw new CommunicationException(e);
		} catch (XmlPullParserException e) {
			throw new CommunicationException(e);
		}

		T result = null;
		try {

			result = (T) envelope.getResponse();
			SoapObject env = (SoapObject) envelope.bodyIn;
			Object property = env.getProperty(0);
			
		} catch (SoapFault e) {
			handleRemoteExceptions(e);
		}
		Log.i(KSoapExecutor.class.getName(), "result returned:\n"+result);
		return result;
	}
	
	public void execute(SoapObject soapObject, URL instanceURL) throws CommunicationException, AuthorizationException, AuthenticationException{
		this.execute(soapObject, instanceURL, Object.class);
	}

	private Transport getTransport(URL jiraInstanceURL) {
		URL transportURL = getURLwithWSDLPath(jiraInstanceURL);
		if ("http".equals(transportURL.getProtocol())) {
			return new HttpTransportSE(transportURL.toExternalForm(), TIMEOUT);
		}
		else if ("https".equals(transportURL.getProtocol())) {
			return new HttpsTransportSE(transportURL.getHost(),
			        transportURL.getPort(), transportURL.getFile(), TIMEOUT);
		}
		else {
			throw new IllegalArgumentException(
			        "Doesn't support other protocols than HTTP & HTTPS for now");
		}
	}
	
	private void handleRemoteExceptions(SoapFault ex)
	        throws AuthorizationException, AuthenticationException,
	        CommunicationException {
		try {
			throwJiraException(ex);
		} catch (RemotePermissionException e) {
			throw new AuthorizationException(e);
		} catch (RemoteAuthenticationException e) {
			throw new AuthenticationException(e);
		} catch (RemoteValidationException e) {
			throw new CommunicationException(e);
		} catch (RemoteException e) {
			throw new CommunicationException(e);
		}

	}

	private void throwJiraException(SoapFault ex)
	        throws RemotePermissionException, RemoteAuthenticationException,
	        RemoteValidationException, RemoteException {
		String msg = ex.getMessage();

		if (msg.contains("RemotePermissionException")) {
			throw new RemotePermissionException(ex);
		}
		else if (msg.contains("RemoteAuthenticationException")) {
			throw new RemoteAuthenticationException(ex);
		}
		else if (msg.contains("RemoteValidationException")) {
			throw new RemoteValidationException(ex);
		}
		else {
			throw new RemoteException(ex);
		}

	}

	private URL getURLwithWSDLPath(URL jiraInstanceURL) {
		URL result;
		try {
			result = new URL(jiraInstanceURL.getProtocol(),
			        jiraInstanceURL.getHost(), jiraInstanceURL.getPort(),
			        jiraInstanceURL.getFile() + WSDL_PATH);
		} catch (MalformedURLException e) {
			String msg = "This shouldn't happen because we only modify URL we received";
			Log.wtf(getClass().getName(), msg);
			throw new RuntimeException(msg);
		}
		return result;
	}
}
