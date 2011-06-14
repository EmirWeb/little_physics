package com.maker.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maker.Global;
import com.maker.R;
import com.maker.utilities.PersistanceManager;
import com.maker.world.World;

public class SaveWorldActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_world_activity);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText nameText = (EditText) findViewById(R.id.name);
				String filename = nameText.getText().toString();
				try{
					World world = Global.getSavingWorld();
					JSONObject json = world.toJSON();
					String string = json.toString();
					PersistanceManager.save(string, filename, getApplicationContext());
				}catch (Exception e){
					Toast.makeText(getApplicationContext(), "File already exists", Toast.LENGTH_LONG).show();
					return;
				}
				
				Global.setSavingWorld(null);
				finish();
			}
		});
	}
}
