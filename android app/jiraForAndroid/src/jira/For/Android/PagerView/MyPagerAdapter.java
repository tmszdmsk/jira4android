package jira.For.Android.PagerView;

import jira.For.Android.DLog;
import jira.For.Android.PagerView.ViewPagerIndicator.PageInfoProvider;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class MyPagerAdapter extends PagerAdapter implements PageInfoProvider {

	ViewForPagerInterface myViewForPager;

	public MyPagerAdapter(ViewForPagerInterface myViewForPager) {
		super();

		DLog.i("MyPagerAdaptrer", "onCreate <-- I'm here!");
		this.myViewForPager = myViewForPager;
	}

	// names of displayed tabs

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// DLog.i("destroyItem", "arg0 == " + arg0.getClass().toString());
		// DLog.i("destroyItem", "arg1 == " + arg1);
		// DLog.i("destroyItem", "arg2 == " + arg2.getClass().toString());
		// ((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public void finishUpdate(View arg0) {

		/*
		 * int color = arg0.getResources().getColor(R.color.page_indicator);
		 * ((ViewPagerIndicator) arg0)
		 * .setFocusedTextColor(((ViewPagerIndicator) arg0)
		 * .getColorsFromInt(color));
		 */

	}

	@Override
	public int getCount() {
		return myViewForPager.getLength();
	}

	// function displaying chosen tab
	@Override
	public Object instantiateItem(View collection, int pos) {
		if (pos >= getCount() || ((ViewPager) collection).getChildCount() > pos) return null;
		Log.v("MyPagerAdapter", "instantiateItem");
		Log.v("MyPagerAdapter.instantiateItem",
		        "collection.getChildCount() == "
		                + ((ViewPager) collection).getChildCount());
		LayoutInflater inflater = (LayoutInflater) collection.getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = myViewForPager.loadView(inflater, pos);// myViewForPager.getView(inflater,
		// Tabs.fromInt(pos));
		if (view == null) Log.w("instantiateItem", "view==null");
		((ViewPager) collection).addView(view, 0);
		return view;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTitle(int pos) {
		return myViewForPager.getTitle(pos);
	}

}
