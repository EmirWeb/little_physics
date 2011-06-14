package com.maker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maker.Logger;
import com.maker.R;

public class LandingActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.setDebuggable(true);
		setContentView(R.layout.landing_activity);

		Button createButton = (Button) findViewById(R.id.create_button);
		createButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create();
			}
		});
		
		Button loadButton = (Button) findViewById(R.id.load_button);
		loadButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				load();
			}
		});
	}

	private void create() {
		startActivity(new Intent(this, CreateActivity.class));
	}
	
	private void load() {
		startActivity(new Intent(this, LoadWorldActivity.class));
	}
}