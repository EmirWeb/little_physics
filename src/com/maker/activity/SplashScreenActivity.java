package com.maker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.maker.R;

public class SplashScreenActivity extends Activity {

	private long timeStarted;
	private boolean finished;
	private final long splashTimeout = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash_screen_activity);
		super.onCreate(savedInstanceState);
		timeStarted = System.currentTimeMillis();
		
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {
						if (!finished){
							startActivity(new Intent(SplashScreenActivity.this, GravityLevel1Activity.class));
							finish();
						}
					}
				}, splashTimeout);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finished = true;
	}
}
