package jira.For.Android.PagerView;

import android.view.LayoutInflater;
import android.view.View;

public interface ViewForPagerInterface {

	public String getTitle(int a);

	public View loadView(LayoutInflater inflater, int pos);

	public int getLength();

}
