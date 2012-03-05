package com.pingaala.sample.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pingaala.sample.test.utils.ImageManager;
import com.pingaala.sample.test.utils.Utils;
/**
 * @author Sheetal Suryan 
 * 
 */
public class ContentActivity extends Activity{

	private static final String TAG = "Vyasa";
	public static ImageView bookMark;
	public ImageView shareing,imageView;
	public ImageView twitterShareing;

	public static ContentActivity context;
	static int bookmarked =0;
	private ImageManager imageManager;
	public static ViewPager contentPager;
	public static ContentPagerAdapter  contentPagerAdapter;
	private static int viewNumber;
	public static TextView pageNumberView;
	private static int _position = 0 ;
	public static int hasRead = 1;
	SharedPreferences preferences;
	private static final int[] ids = {  R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb,R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
		R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.icecream };
	List<String> urls ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;

		imageManager = ImageManager.getImageManager(	this.getApplicationContext(), true);
		urls = new ArrayList<String>();
		for (int i = 0;i < 30;i++){

			urls.add("http://members.home.nl/yourdesktop/highresolution/4.jpg");
			urls.add("http://www.wallpaperpimper.com/wallpaper/High_Resolution/High-Resolution-18-W9UIVPT6MM-1600x1200.jpg");
			urls.add("http://www.wallpaperpimper.com/wallpaper/High_Resolution/High-Resolution-3-ALKVC8G2MB-1600x1200.jpg");
			urls.add("http://i1-win.softpedia-static.com/screenshots/Windows-7-High-Resolution-Regional-Wallpapers_2.jpg");
			urls.add("http://gardenpictures.com/wp-content/uploads/2009/02/high-quality-images-white-tulips-photo-background.jpg");
			urls.add("http://www.newsin3d.com/store/images/uploads/Asteroid_Impacts_Earth_high_resolution.jpg");
			urls.add("http://i1-win.softpedia-static.com/screenshots/Windows-7-High-Resolution-Regional-Wallpapers_2.jpg");
			urls.add("http://upload.wikimedia.org/wikipedia/commons/d/d8/Deerfire_high_res_edit.jpg");
			urls.add("http://www.deviantart.com/download/140085847/smooth_n_smokey_blue_bokeh_by_DyingBeautyStock.jpg");
			urls.add("http://t3.gstatic.com/images?q=tbn:ANd9GcRVrn9-iMM4mYLxtgKlvCY9G7w_alPZkwlMTDfZwzawq6YtwIM8H9yNj6Y0");
			urls.add("http://www.bittbox.com/wp-content/uploads/2009/02/public_domain_astronomy_19.jpg");
			urls.add("http://designfruit.com/dfassets/images/high-res-fresh-foliage-ii-03.jpg");
			urls.add("http://filmstudies.files.wordpress.com/2007/07/film-studies-101-hi-resolution.jpg");
			urls.add("http://www.enpirion.com/Collateral/Documents/English-US/downloadable_shots/EN5322-2hi_res.jpg");
			urls.add("http://www.newsin3d.com/store/images/uploads/Future_City_highresolution.jpg");


		}
		//	Log.i(TAG, "onBackPressed called if " +ContentActivity.hasAlreadyRead + "  mem " +mPages.get(_position).read );

		contentPager = (ViewPager) findViewById(R.id.ombook_viewer_pager);
		contentPagerAdapter = new ContentPagerAdapter();
		ContentActivity.contentPager.setAdapter(contentPagerAdapter);
		ContentActivity.contentPager.setOnPageChangeListener(new PageListener());
		contentPager.setCurrentItem(_position );




	}

	class PageListener extends SimpleOnPageChangeListener {

		public void onPageScrollStateChanged (int state){

			//Log.d(TAG, "onPageScrollStateChanged  state  " + state+ " _position  "+_position + "webView"+webView);


		}
		public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels){

			//Log.d(TAG,"onPageScrolled Target webViewPrevious : " + webViewPrevious + " webView " + webView );
		}
		public void onPageSelected(int position) {

			Utils.logMemory();

			//			pageNumberView.setText("" + (position + 1) + " of " + (viewNumber));
		}
	}




	public void onSwitched(View view, int position) {	}

	@Override
	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.
	}
	@Override
	protected void onResume() {
		super.onResume();

	}
	@Override
	protected void onPause() {
		super.onPause();


		// Another activity is taking focus (this activity is about to be "paused").
	}
	@Override
	protected void onStop() {
		super.onStop();
		// The activity is no longer visible (it is now "stopped")
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// The activity is about to be destroyed.
	}
	@Override
	public void onBackPressed() {


	}


	private class ContentPagerAdapter extends PagerAdapter {
		@Override
		public void destroyItem(View collection, int position, Object o) {
			LinearLayout view = (LinearLayout)o;
			((ViewPager) collection).removeView(view);
			view = null;
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public int getCount() {
			return ids.length;
		}

		@Override
		public Object instantiateItem(View context, int position) {
			LinearLayout ll = new LinearLayout(getApplicationContext());
			ImageView imageView = new ImageView(getApplicationContext());
			imageView.findViewById(R.id.item_image);
		//	imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), ids[position]));
			
			
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(new URL(urls.get(position).toString()).openConnection().getInputStream());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			imageView.setImageBitmap(bitmap);
			ll.addView(imageView);
			((ViewPager) context).addView(ll);

			return ll;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==((LinearLayout)object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
		}
		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}


}