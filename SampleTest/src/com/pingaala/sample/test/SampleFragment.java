package com.pingaala.sample.test;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingaala.sample.test.utils.Utils;

public class SampleFragment extends FragmentActivity {
	private ViewPager contentPager;
	private ContentPagerAdapter contentPagerAdapter;

	public static SampleFragment ctx;

	private static TextView pageNumberView;

	private static String TAG = "OMBOOK_VIEWER";
	private static int _position = 0;

	public ProgressDialog progressDialog;
	List <MyFragment> myFragments  = new ArrayList<MyFragment>();

	private static final int[] ids = { R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.icecream };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.startMethodTracing("traceview");


		setContentView(R.layout.main);
		Utils.logMemory();

		ctx = this;
		_position = 0;

		//contentPager = (ViewPager) findViewById(R.id.ombook_viewer_pager);
		//contentPagerAdapter = new ContentPagerAdapter();
		//contentPager.setAdapter(contentPagerAdapter);

		MyFragment myFragment = new MyFragment(ids[0],getResources(),0 );
		myFragments.add(0, myFragment);
		contentPager = (ViewPager)findViewById(R.id.ombook_viewer_pager);
		contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager());
		//	contentPagerAdapter.setRecentListener(this);
		contentPager.setAdapter(contentPagerAdapter);

		contentPager.setCurrentItem(0);
		contentPager.setOnPageChangeListener(new PageListener());

	}



	@Override
	public void onBackPressed() {
		ctx = null;


		//	onDestroy();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		Debug.stopMethodTracing();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "ombook got data requestcode : " + requestCode
				+ " , resultCode : " + resultCode);
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	private static class PageListener extends SimpleOnPageChangeListener {
		public void onPageSelected(int position) {


			Log.d(TAG, "FROM position " + _position + " to " + position);
			_position = position;
		}
	}



	private class ContentPagerAdapter extends FragmentPagerAdapter {
		FragmentManager fm;
		public ContentPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;

		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			Log.d("DESTROY", "destroying view at position " + position);
			((ViewPager) collection).removeView((View) view);
		}
	
		@Override
		public Object instantiateItem(View container, int position) {


			Log.i(TAG, "instantiate  called");
			MyFragment myFragment = new MyFragment(ids[position],getResources(),position );
			myFragments.add(position, myFragment);

			//return super.instantiateItem(container, position);
			return myFragment;
		}
		 

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return ids.length;
		}
		@Override
		public Fragment getItem(int position) {
			Log.i(TAG, "getItem  called");
			MyFragment f = null;
			try{
				f = new MyFragment(ids[position],getResources(),position );
				myFragments.add(position, f);

			}
			catch (Exception e) {
				Log.i(TAG," ",e);
			}
			Log.i(TAG, " myFragment : "+ f + " size : "+myFragments.size());
			return f;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

	}

	public class MyFragment extends Fragment {
		private Resources resources;  // I load my images from different resources installed on the device
		private int id;

		public MyFragment() {
			Log.i(TAG, " MyFragment()  called");
			setRetainInstance(true); // this will prevent the app from crashing on screen rotation
		}

		public MyFragment( int id,Resources resources,int position) {
			this();  // this makes sure that setRetainInstance(true) is always called
			this.resources = resources;
			this.id = id;
			Log.i(TAG, " MyFragment(id)  called");
		}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			Log.d(TAG, "onCreateView hii is called "+resources+ " id "+id);
			View view = inflater.inflate(R.layout.test, container, false);
			ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
			imageView.setImageBitmap(BitmapFactory.decodeResource(resources, id));

			TextView tv = (TextView)view.findViewById(R.id.item);
			tv.setText("testing Textview : "+id);
			imageView.setImageResource(R.drawable.cupcake);


			return view;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			Log.d(TAG, "onActivityCreated hii is called "+resources+ " id "+id);

		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			Log.d(TAG, "onCreate hii is called "+resources+ " id "+id);
			super.onCreate(savedInstanceState);
		}
	}

}
