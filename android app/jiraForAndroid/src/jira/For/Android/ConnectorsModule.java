package jira.For.Android;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.jira4android.connectors.AuthenthicationService;
import com.jira4android.connectors.KSoapExecutor;

public class ConnectorsModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(AuthenthicationService.class).in(Singleton.class);
		binder.bind(KSoapExecutor.class).in(Singleton.class);
	}
}
