package com.pingaala.sample.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.pingaala.sample.test.R;

public class ImageManager {

	private static final String TAG = "IMAGEMANAGER";
	private HashMap<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
	private File cacheDir;
	private ImageQueue imageQueue = new ImageQueue();
	private Thread imageLoaderThread = new Thread(new ImageQueueManager());
	private static ImageManager imageManager;

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static synchronized ImageManager getImageManager(Context ctx,
			boolean recycleIfNewObject) {
		if (imageManager == null) {
			Log.w(TAG, "BRAND NEW IMAGEMANAGER");
			Log.d(TAG, "IMAGE MANAGER is null");
			return new ImageManager(ctx);
		} else {
			if (recycleIfNewObject) {
				String[] urls = { "", "" };
				Log.w("FREEING MEMORY", "image map size : "
						+ imageManager.imageMap.size());

				imageManager.recycler(urls);
			} else {
				Log.w(TAG,
						"no recycling , just returning old imagemanager with imagemap size as : "
								+ imageManager.imageMap.size());

			}
			return imageManager;
		}
	}

	private ImageManager(Context context) {
		// Make background thread low priority, to avoid affecting UI
		// performance
		imageLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		// Find the dir to save cached images
		String sdState = android.os.Environment.getExternalStorageState();
		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();
			cacheDir = new File(sdDir, context.getString(R.string.cache_dir));
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
			Log.d(TAG, cacheDir.getAbsolutePath());
		}
	}

	public void recycler(String[] urls) {
		Log.w("FREEING MEMORY", "imagemap has : " + imageMap.size()
				+ " elements");
		List<String> removedEntrys = new ArrayList<String>();
		for (Map.Entry<String, Bitmap> entry : imageMap.entrySet()) {
			// Map.Entry<String, Bitmap> entry = entry;
			boolean recycle = true;
			for (String url : urls) {
				if (entry.getKey().equals(url)) {
					recycle = false;
				}
			}
			if (recycle) {
				Log.w("FREEING MEMORY", "recycling url ... " + entry.getKey());
				if(entry.getValue()!=null){
					entry.getValue().recycle();
				}
				removedEntrys.add(entry.getKey());
			}

		}

		for(String s : removedEntrys){
			imageMap.remove(s);
		}
	}




	public void displayImage(String url, Context context, ImageView imageView) {

		Bitmap bitmap = getBitmap(url);
		if (bitmap != null) {
			//	bitmap = Utils.scaleBitmap(bitmap, Utils.getPxFromDp(50));
			//imageView.setScaleType(ScaleType.CENTER_INSIDE);
			imageView.setImageBitmap(bitmap);

			imageMap.put(url, bitmap);
			return;
		}

		if (imageMap.containsKey(url)) {
			imageView.setImageBitmap(imageMap.get(url));
		} else {
			queueImage(url, imageView);
		}
	}

	private void queueImage(String url, ImageView imageView) {
		// This ImageView might have been used for other images, so we clear
		// the queue of old tasks before starting.
		imageQueue.Clean(imageView);
		ImageRef p = new ImageRef(url, imageView);

		synchronized (imageQueue.imageRefs) {
			imageQueue.imageRefs.push(p);
			imageQueue.imageRefs.notifyAll();
		}

		// Start thread if it's not started yet
		if (imageLoaderThread.getState() == Thread.State.NEW)
			imageLoaderThread.start();
	}

	private Bitmap getBitmap(String url) {
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);

		// Is the bitmap in our cache?
		//	Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
		Bitmap bitmap = decodeFile(f);
		if (bitmap != null) {
			Log.d(TAG, "returned " + url + " from cache");
			return bitmap;
		}

		// Nope, have to download it
		try {
			Log.d(TAG, "getting url " + url);
			//	bitmap = BitmapFactory.decodeStream(new URL(url).openConnection()
			//			.getInputStream());

			// save bitmap to cache for later
		//	writeFile(bitmap, f);

			return bitmap;
		} catch (Exception ex) {
			Log.w(TAG, "getting url " + url, ex);

			return null;
		}
	}

	private void writeFile(Bitmap bmp, File f) {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception ex) {
			}
		}
	}

	/** Classes **/

	/**
	 * maps url to imageview
	 * 
	 * @author vaibhav
	 * 
	 */
	private class ImageRef {
		public String url;
		public ImageView imageView;

		/**
		 * 
		 * @param u
		 *            url
		 * @param i
		 *            imageview
		 */
		public ImageRef(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	/**
	 * a stack maintaining references to be retrieved
	 * 
	 * @author vaibhav
	 */
	private class ImageQueue {
		private Stack<ImageRef> imageRefs = new Stack<ImageRef>();

		// removes all instances of this ImageView
		public void Clean(ImageView view) {

			for (int i = 0; i < imageRefs.size();) {
				if (imageRefs.get(i).imageView == view)
					imageRefs.remove(i);
				else
					++i;
			}
		}
	}

	/**
	 * responsible for retrieving images
	 * 
	 * @author vaibhav
	 * 
	 */
	private class ImageQueueManager implements Runnable {
		public void run() {
			try {
				while (true) {
					// Thread waits until there are images in the
					// queue to be retrieved
					if (imageQueue.imageRefs.size() == 0) {
						synchronized (imageQueue.imageRefs) {
							imageQueue.imageRefs.wait();
						}
					}

					// When we have images to be loaded
					if (imageQueue.imageRefs.size() != 0) {
						ImageRef imageToLoad;

						synchronized (imageQueue.imageRefs) {
							imageToLoad = imageQueue.imageRefs.pop();
						}

						Bitmap bmp = getBitmap(imageToLoad.url);
						imageMap.put(imageToLoad.url, bmp);
						Object tag = imageToLoad.imageView.getTag();

						// Make sure we have the right view - thread safety
						// defender
						if (tag != null
								&& ((String) tag).equals(imageToLoad.url)) {
							BitmapDisplayer bmpDisplayer = new BitmapDisplayer(
									bmp, imageToLoad.imageView);

							Activity a = (Activity) imageToLoad.imageView
									.getContext();

							a.runOnUiThread(bmpDisplayer);
						}
					}

					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
			}
		}
	}

	// Used to display bitmap in the UI thread
	/**
	 * Used to display bitmap in the UI thread
	 * 
	 * @author vaibhav
	 * 
	 */
	private class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		/**
		 * 
		 * @param b
		 *            bitmap to be displayed
		 * @param i
		 *            imageview on which bitmap is to be displayed
		 */
		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			} else {
				imageView.setImageResource(R.drawable.icon);
			}
		}
	}
	//sheetal
	public List<String> removedEntrys  = new ArrayList<String>();;
	public void recyclers(String[] urls) {
		Log.w("FREEING MEMORY", "sheetal imagemap has : " + imageMap.size() +" removedEntrys "+removedEntrys.size());

		/*for (Map.Entry<String, Bitmap> entry : imageMap.entrySet()) {
			// Map.Entry<String, Bitmap> entry = entry;
			boolean recycle = true;
			for (String url : urls) {
				if (entry.getKey().equals(url)) {
					recycle = false;
				}
			}
			if (recycle) {
				Log.w("FREEING MEMORY", "recycling url ... " + entry.getKey());
				if(entry.getValue()!=null){
					entry.getValue().recycle();
				}
				removedEntrys.add(entry.getKey());
			}

		}*/
		imageMap.remove(removedEntrys.get(0));

	}

	//decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f){
		try {
			//Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f),null,o);

			//The new size we want to scale to
			final int REQUIRED_SIZE=200;

			//Find the correct scale value. It should be the power of 2.
			int scale=1;
			while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
				scale*=2;

			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {}
		return null;
	}


}
