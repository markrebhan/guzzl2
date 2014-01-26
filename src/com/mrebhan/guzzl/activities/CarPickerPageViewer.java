package com.mrebhan.guzzl.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.animations.DepthPageTransformer;
import com.mrebhan.guzzl.fragments.CarPickerFragment;

public class CarPickerPageViewer extends Fragment{

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 2;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;
	

	/**
	 * this method will display the viewpager on the fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pageviewer, container, false);
		// set the pageviewer to the view in XML
		mPager = (ViewPager) view;
		mPagerAdapter = new ScreenSlidePagerAdapter(this.getChildFragmentManager()); 
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageTransformer(true, new DepthPageTransformer());
		setRetainInstance(true);
		return view;
	}

	/**
	 * This private inner class handles setup for adapter
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		// construct object and pass in the base activity fragment manager to
		// handle fragments
		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// OncreateView called on CarPickerFragment
		// Retrieve item from fragment class given specified position
		@Override
		public CarPickerFragment getItem(int position) {
			return CarPickerFragment.create(position);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

}
