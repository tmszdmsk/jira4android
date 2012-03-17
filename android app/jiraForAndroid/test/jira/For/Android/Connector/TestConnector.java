package jira.For.Android.Connector;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.sun.xml.internal.bind.AnyTypeAdapter;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TestConnector {

	@Test
	public void ConnectorLoginTest() throws Exception {

		HttpTransportSE transportSE = mock(HttpTransportSE.class);
		Connector connector = new Connector(transportSE);

		SoapSerializationEnvelope envelope = mock(SoapSerializationEnvelope.class);
		
		when(connector.getNewEnvelope()).thenReturn(envelope);
		when(envelope.getResponse()).thenReturn("NIE MA CHLEBA!");
		
		SoapObject login = new SoapObject(connector.getNameSpace(), "login");
		login.addProperty("username", "t");
		login.addProperty("password", "p");
		
		connector.jiraLogin("t", "p", "l");
		
		verify(envelope).setOutputSoapObject(eq(login));
		
		//System.out.println(envelope.bodyIn);

//		assertTrue(connector.getJiraUrl().compareTo(
//				"l" + "/rpc/soap/jirasoapservice-v2?wsdl") == 0);
	}
}
