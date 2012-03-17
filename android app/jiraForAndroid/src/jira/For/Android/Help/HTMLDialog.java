package jira.For.Android.Help;

import jira.For.Android.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.webkit.WebView;


public class HTMLDialog extends Dialog {

	public HTMLDialog(Context context, String filename) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);
		WebView wv = (WebView) this.findViewById(R.id.help_WebView);
		wv.loadUrl("file:///android_asset/" + filename);
	}

}
