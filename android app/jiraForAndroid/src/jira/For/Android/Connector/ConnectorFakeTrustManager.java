package jira.For.Android.Connector;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ConnectorFakeTrustManager implements X509TrustManager {

	private static TrustManager[] trustManagers;
	private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

	public void checkClientTrusted(X509Certificate[] chain, String authType)
	        throws CertificateException {}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
	        throws CertificateException {}

	public boolean isClientTrusted(X509Certificate[] chain) {
		return true;
	}

	public boolean isServerTrusted(X509Certificate[] chain) {
		return true;
	}

	public X509Certificate[] getAcceptedIssuers() {
		return _AcceptedIssuers;
	}

	public static void allowAllSSL() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}

		});

		SSLContext context = null;
		if (trustManagers == null) {
			trustManagers = new TrustManager[] {new ConnectorFakeTrustManager()};
		}

		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustManagers, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(context
		        .getSocketFactory());
	}

}
