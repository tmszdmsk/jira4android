package jira.For.Android;

import java.util.List;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Singleton;
import com.jira4android.connectors.AuthenthicationService;
import com.jira4android.connectors.KSoapExecutor;

import roboguice.application.RoboApplication;

import android.app.Application;
//TODO DO NOT FORMAT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
@ReportsCrashes(formKey = "dEpfQVY4Nm1OLWdCS1NHZ1BremRoTVE6MQ",
mode = ReportingInteractionMode.NOTIFICATION,
resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
resNotifTickerText = R.string.crash_notif_ticker_text,
resNotifTitle = R.string.crash_notif_title,
resNotifText = R.string.crash_notif_text,
resNotifIcon = android.R.drawable.stat_notify_error, // optional. default is a warning sign
resDialogText = R.string.crash_dialog_text,
resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
resDialogOkToast = R.string.crash_dialog_ok_toast)// optional. displays a Toast message when the user accepts to send a report.)
public class JiraForAndroidApplication extends RoboApplication {
	//TODO DO NOT FORMAT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	@Override
	public void onCreate() {
		//TODO Uncomment when release
		//ACRA.init(this);

		super.onCreate();
		DLog.i("Starting Jira 4 Android!");
	};
	
	@Override
	protected void addApplicationModules(List<Module> modules) {
	    modules.add(new ConnectorsModule());
	}
	

}
