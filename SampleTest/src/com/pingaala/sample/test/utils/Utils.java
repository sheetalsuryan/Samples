package com.pingaala.sample.test.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

public class Utils {
	private static String TAG = "UTILS";
	public static int screenHeight = 0;
	public static int screenWidth = 0;
	public static float density = 0;
	
	public static boolean isOnline(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		try{
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}catch(Exception ex){
			Log.d(TAG, "probably no active networks");
		}
		return false;
		
	}
	
	public static int getScreenOrientation(Activity ctx)
	{
	    Display getOrient = ctx.getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    Log.d(TAG, "width x height"+getOrient.getWidth()+getOrient.getHeight());
	    return orientation;
	}
	
	public static boolean isLandscape(Activity ctx){
		Display getOrient = ctx.getWindowManager().getDefaultDisplay();
		Log.d(TAG, "height : "+getOrient.getHeight());
		Log.d(TAG, "width : "+getOrient.getWidth());
		return getOrient.getHeight()<getOrient.getWidth();
	}
	
	public static boolean isNull(Object obj){
		return obj==null;
	}
	
	public static void logMemory(){
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memoryInfo);

		String memMessage = String.format(
		    "Memory: Pss=%.2f MB, Private=%.2f MB, Shared=%.2f MB, native heap=%f MB NativeHeap =%f",
		    memoryInfo.getTotalPss() / 1024.0,
		    memoryInfo.getTotalPrivateDirty() / 1024.0,
		    memoryInfo.getTotalSharedDirty() / 1024.0,
			Debug.getNativeHeapAllocatedSize()/(1024.0*1024.0),
			(Debug.getNativeHeapSize()/(1024.0*1024.0)));
		Log.w("MEMORY", memMessage);

	}
	
	public static int getPxFromDp(float dp){
		return (int) (density * dp + 0.5f);
	}

	public static boolean isResolutionIncompatible(Activity ctx) {
		DisplayMetrics metrics = new DisplayMetrics();
		 ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 int height = metrics.heightPixels;
		 int width = metrics.widthPixels;
		 Log.d("HEIGHT", "height : "+height);
		 Log.d("WIDTH", "width : "+width);
		 if(height>width?height<426||width<320:width<426||height<320){
			 Log.d("UTIL", "disabling screen");
			 return true;
		 }
		 Log.d("UTIL", "AOK");
		return false;
	}
	
	

	public static Bitmap scaleBitmap(Bitmap bitmap, int heightOffset) {
		//ctx.getWindowManager();
		//this code is for landscape mode
		
		int landscapeHeight = screenWidth;
	//	int landscapeWidth = screenHeight;
		
		int newHeight = landscapeHeight-heightOffset;
		
		float aspect = (float)bitmap.getWidth()/bitmap.getHeight();
		Log.d(TAG, "aspect is : "+aspect);
		int newWidth = (int)(newHeight*aspect);
		Log.d(TAG, "new height is : "+newHeight+" new width is : "+newWidth+" landscapeHeight is "+landscapeHeight);
		Bitmap b = bitmap;
		if(newWidth<bitmap.getWidth()){
			
			Matrix matrix  = new Matrix();
			float scaleWidth = (float)newWidth/bitmap.getWidth();
			float scaleHeight = (float)newHeight/bitmap.getHeight();
			matrix.postScale(scaleWidth, scaleHeight);
			Log.d("HARDCORE SCALING ACTION", "scaling bitmap to : "+newWidth+"x"+newHeight+" from "+bitmap.getWidth()+"x"+bitmap.getHeight()+" where lanscape height is : "+landscapeHeight+" and height offset is : "+heightOffset+"width scale : "+scaleWidth+" and height scale is "+scaleHeight);
			b = Bitmap.createBitmap(bitmap, 0, 0, newWidth, newHeight, matrix, false);
		    bitmap.recycle();
		}else{
			Log.d("NO SCALING", "new widht is : "+newWidth+" and bitmaps width is : "+bitmap.getWidth());
			return bitmap;
		}
	   
	    return b;
	}
	public static void initializeConstants(Activity ctx){
		DisplayMetrics metrics = new DisplayMetrics();
		 ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 Utils.screenHeight = metrics.heightPixels;
		 Utils.screenWidth = metrics.widthPixels;
		 Utils.density = ctx.getResources().getDisplayMetrics().density;
	}
	

}
