package jira.For.Android.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import jira.For.Android.LoginActivity;
import jira.For.Android.Connector.*;

public class ConnectorTest {

	@Test
	public void Test() throws Exception {

		// mock creation
		Connector connector = mock(Connector.class);

	
		when(connector.jiraLogin("test.user", "test.password",
				"http://jira.wmi.amu.edu.pl")).thenReturn(true);
		
		
	}
}
