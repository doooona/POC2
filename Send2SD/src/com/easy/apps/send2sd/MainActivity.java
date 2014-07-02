package com.easy.apps.send2sd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import info.hoang8f.widget.FButton;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{
	
	private FButton twitterBtn;
	private FButton openBtn;
	private Uri photoURI;
	private Intent latestIntent;
	private StorageManager mStorageManager;
	private Method mMethodGetPaths;
	private static final String SAVE_FLODER_NAME = "SaveToSD";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//使用不同顏色來區分狀態
		twitterBtn = (FButton) findViewById(R.id.f_twitter_button);
		twitterBtn.setOnClickListener(this);	
		openBtn = (FButton) findViewById(R.id.f_twitter_button2);
		openBtn.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume()
	{
	  super.onResume();
	  
	  if(this.getIntent() != null && this.getIntent().getAction() !=null && this.getIntent().getAction().equals("android.intent.action.SEND")){
			twitterBtn.setButtonColor(this.getResources().getColor(R.color.color_sun_flower));
			twitterBtn.setText(this.getString(R.string.btn_save_it));
			Log.d("@@@", String.format("getIntent %s",this.getIntent().getAction()));
		}else{
			twitterBtn.setButtonColor(this.getResources().getColor(R.color.button_default_color));
			twitterBtn.setText(this.getString(R.string.btn_save_file_to_sd));
			Log.d("@@@", "getIntent null");
		}
		
		this.latestIntent = this.getIntent();
		
		this.checkIntent(this.getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
		 	case R.id.f_twitter_button:
		 		
		 	this.handleIntent();
		 		
			 break;
			 
		 	case R.id.f_twitter_button2:
		 	
		 	break;
		 }
	}
	
	private void checkIntent(Intent intent)
	{
		if(intent==null || intent.getAction() == null){
			
			this.showToast(this.getString(R.string.btn_save_file_to_sd));
			
			return;
		}
		
		if ("android.intent.action.SEND".equals(intent.getAction()))
		{
			Bundle bundle = intent.getExtras();
			Uri data = (Uri)bundle.get(Intent.EXTRA_STREAM);
			photoURI = data;
			
			Log.d("@@@", "hi2");
			
		}else if ("android.intent.action.SEND_MULTIPLE".equals(intent.getAction()))
		{
			
		}
	}
	
	private void showToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	private void saveImage()
	{
		if (photoURI != null) {
			try {
				String fileName = photoURI.toString().substring(
						2 + photoURI.toString().lastIndexOf("/"),
						photoURI.toString().length());
				
				String sdPath = getSavePath();
				
				Log.d("@@@", "sdPath:" + sdPath);
				Log.d("@@@", "fileName:" + fileName);
				
				String parentPath = sdPath + File.separator
						+ MainActivity.SAVE_FLODER_NAME;
				
				Log.d("@@@", "parentPath:" + parentPath);
				
				File sdParentFolder = new File(parentPath);
				
				String finalFilePath = sdPath + File.separator
						+ MainActivity.SAVE_FLODER_NAME + File.separator + "save2SD-images";
				File sdFolder = new File(finalFilePath);
				if (!sdFolder.exists()){
					if(sdFolder.mkdirs())
						Log.d("@@@", "mkdir");
					
				}

				copy(new File(photoURI.getPath()), new File(finalFilePath,
						fileName));

				String[] pathAry = { finalFilePath + File.separator + fileName };

				MediaScannerConnection.scanFile(this, pathAry, null, null);

				Toast.makeText(this, this.getString(R.string.save_suc),
						Toast.LENGTH_LONG).show();
				
				getIntent().removeExtra("key"); 

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			Log.d("@@@", "hi...");
	}
	
	private void saveFile()
	{
		if (photoURI != null) {
			try {
				String fileName = photoURI.toString().substring(
						2 + photoURI.toString().lastIndexOf("/"),
						photoURI.toString().length());
				
				String sdPath = getSavePath();
				
				Log.d("@@@", "sdPath:" + sdPath);
				
				String parentPath = sdPath + File.separator
						+ MainActivity.SAVE_FLODER_NAME;
				
				File sdParentFolder = new File(parentPath);
				
				String finalFilePath = sdPath + File.separator
						+ MainActivity.SAVE_FLODER_NAME + File.separator + "save2SD-files";
				File sdFolder = new File(finalFilePath);
				if (!sdFolder.exists()){
					if(sdFolder.mkdirs())
						Log.d("@@@", "mkdir");
					
				}

				copy(new File(photoURI.getPath()), new File(finalFilePath,
						fileName));

				Toast.makeText(this, this.getString(R.string.save_file_suc),
						Toast.LENGTH_LONG).show();
				
				getIntent().removeExtra("key"); 

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleIntent(){
		
		if(this.latestIntent != null && this.latestIntent.getAction() != null && this.latestIntent.getAction().equals("android.intent.action.SEND")){
			
			// Figure out what to do based on the intent type
		    if (latestIntent.getType().indexOf("image/") != -1) {
		        // Handle intents with image data ...
		    	Log.d("@@@", "save image...");
		    	this.saveImage();
		    	
		    } else if (latestIntent.getType().equals("text/plain")) {
		        // Handle intents with text ...
		    	this.saveFile();
		    	Log.d("@@@", "save file...");
		    }else{
		    	this.saveFile();
		    	Log.d("@@@", "save any file...");
		    }
		    	
		}else
			this.showToast(this.getString(R.string.btn_save_file_to_sd));
		
	}
	
	private String getSavePath(){
		
		int sysVersion = Integer.parseInt(VERSION.SDK);
		
		String sdPath;
		
		Log.d("@@@", String.format("verison: %d", sysVersion));
		
		if(sysVersion > 11 && sysVersion < 19){
			
			String[] paths = this.getVolumePaths();
			sdPath = paths[1];
			
		}else{
			sdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		}
		
		return sdPath;
	}
	
	private void copy(File inFile, File outFile){
		OutputStream out = null;
		try {
			out = new FileOutputStream(outFile);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			copyFile(new FileInputStream(inFile),out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];	
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	private void openFolder()
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
				+ "/Download Facebook photos/");
		intent.setDataAndType(uri, "image/*");
		startActivity(Intent.createChooser(intent, "Open folder"));
	}
	
	public String[] getVolumePaths() {
		String[] paths = null;
		try {
			mStorageManager = (StorageManager)this
					.getSystemService(Activity.STORAGE_SERVICE);
			
			mMethodGetPaths = mStorageManager.getClass()
					.getMethod("getVolumePaths");
			
			paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
			return paths;
	}
	
	private String getCallingAppName()
	{
		String appname = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RecentTaskInfo> recentTasks = am.getRecentTasks(10000,ActivityManager.RECENT_WITH_EXCLUDED);
		
		String packageName = recentTasks.get(1).baseIntent.getComponent().getClassName();
		PackageInfo packageInfo;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(packageName, 0);
		
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return appname;
	}
}
