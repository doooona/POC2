package com.easy.apps.afacebookphoto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Uri photoURI;
	private Button mButton;
	private boolean mStat = false;
	private ImageView iv = null;
	private TextView tv = null;
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		adView = (AdView)findViewById(R.id.adView);
	    adView.loadAd(new AdRequest());
		
		initUI();
		praseIntent();
		//saveImage();
		
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	  	 super.onCreateOptionsMenu(menu);
	  	 
	  	 MenuItem item2=menu.add(1,3,0,this.getString(R.string.menu_item1));
	       item2.setIcon(android.R.drawable.ic_menu_more);
	  	 
	     MenuItem item1=menu.add(1,2,0,this.getString(R.string.menu_item2));
	       item1.setIcon(android.R.drawable.ic_menu_add);
	       
	       MenuItem item3=menu.add(1,4,0,this.getString(R.string.menu_item3));
	       item3.setIcon(android.R.drawable.ic_menu_add);
	       
	  	
	       return true;
	  }
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	  	switch (item.getItemId()) {
	  		
	  	case 4:
	  		
	  		Intent intentlock = new Intent("android.intent.action.VIEW");
	  		intentlock.setData(Uri.parse("market://details?id=com.easy.apps.privacylock"));
              startActivity(intentlock);
	  		
	  		break;
	  	
	  		case 2:
	  			
	  			Intent intentPro = new Intent("android.intent.action.VIEW");
	  			intentPro.setData(Uri.parse("market://details?id=com.easy.apps.afacebookphoto"));
	              startActivity(intentPro);
	  			
	  			break;
	  			
	  		case 3:

	              Intent intent = new Intent("android.intent.action.VIEW");
	              intent.setData(Uri.parse("market://search?q=carter.ten&c=apps"));
	              startActivity(intent);
	              
	  			break;
	  	}
	  	
	  	return true;
	  }
	
	@Override
    protected void onDestroy(){
		if(adView!=null)
			adView.destroy();
		
    	super.onDestroy();
	}
	
	private void initUI(){
		mButton = (Button)this.findViewById(R.id.button1);
		iv = (ImageView)this.findViewById(R.id.imageView1);
		tv = (TextView)this.findViewById(R.id.textView1);
		
		mButton.setOnClickListener(new OnClickListener(){
			boolean falg = false;
			@Override
			public void onClick(View arg0) {
				
				if(mStat){
					saveImage();
					
					Crouton.makeText(MainActivity.this,
							MainActivity.this.getString(R.string.save_suc),
							Style.CONFIRM).show();
					
					mStat = false;
					mButton.setText(R.string.back);
					falg = true;
					
				}else{
					try{
						if(falg)
							finish();
						Intent intent = new Intent("android.intent.category.LAUNCHER");
	            		intent.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
	            		startActivity(intent);
	            		finish();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		});
	}
	
	private void praseIntent(){
		Intent fbIntent = this.getIntent();
		if ("android.intent.action.SEND".equals(fbIntent.getAction()))
		{
			Crouton.makeText(MainActivity.this,
					MainActivity.this.getString(R.string.btn_text_confirm),
					Style.ALERT).show();
			mStat = true;
			mButton.setText(this.getString(R.string.btn_text_save));
			mButton.setTextColor(Color.RED);
			//iv.setImageDrawable(this.getResources().getDrawable(R.drawable.b2));
			
			/*tv.setText(this.getString(R.string.open_photo));
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openFolder();
				}
				
			});*/
			
			Bundle bundle = fbIntent.getExtras();
		    boolean flag = bundle.containsKey("android.intent.extra.STREAM");
		    if(flag)
		    {
		    	Uri photoPathUri = (Uri)bundle.getParcelable("android.intent.extra.STREAM");
		    	/*String photoPath = photoPathUri.getPath();
		    	File phtotFile = null;
		    	 if (photoPath != null && !photoPath.equals("")){
		    		 phtotFile = new File(photoPath);
		    		 if(phtotFile!=null && !phtotFile.exists())
		    	 }*/
		    	
		    	photoURI = photoPathUri;
		    }
		}else{
			
			Crouton.makeText(MainActivity.this,
					MainActivity.this.getString(R.string.open_fb),
					Style.INFO).show();
			
			mStat = false;
			mButton.setText(this.getString(R.string.btn_text_openfb));
			mButton.setTextColor(Color.BLACK);
			tv.setText(this.getString(R.string.tv_text_desc));
			
		}
	}
	
	private void saveImage()
	{
		if (photoURI != null) {
			try {
				String fileName = photoURI.toString().substring(
						2 + photoURI.toString().lastIndexOf("/"),
						photoURI.toString().length());

				String sdPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				String finalFilePath = sdPath + File.separator
						+ "Download Facebook photos";
				File sdFolder = new File(finalFilePath);
				if (!sdFolder.exists())
					sdFolder.mkdir();

				copy(new File(photoURI.getPath()), new File(finalFilePath,
						fileName));

				String[] pathAry = { finalFilePath + File.separator + fileName };

				MediaScannerConnection.scanFile(this, pathAry, null, null);

				Toast.makeText(this, this.getString(R.string.save_suc),
						Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
}
