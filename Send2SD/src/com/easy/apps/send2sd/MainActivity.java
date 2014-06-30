package com.easy.apps.send2sd;

import info.hoang8f.widget.FButton;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{
	
	private FButton twitterBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		twitterBtn = (FButton) findViewById(R.id.f_twitter_button);
		twitterBtn.setButtonColor(this.getResources().getColor(R.color.color_sun_flower));
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
		 		
			 break;
		 }
	}

}
