package com.maker.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maker.Global;
import com.maker.R;
import com.maker.utilities.PersistanceManager;
import com.maker.world.World;

public class LoadWorldActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_world_activity);

		ListView worlds = (ListView) findViewById(R.id.worlds);

		ArrayList<String> files = new ArrayList<String>(PersistanceManager.getFiles(getApplicationContext()));
		worlds.setAdapter(new ArrayAdapter<String>(getApplicationContext(), 0, files) {
			@Override
			public View getView(final int position, View v, ViewGroup parent) {
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.world_list_item, null);
				}

				TextView worldText = (TextView) v.findViewById(R.id.world);
				worldText.setText(getItem(position));

				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						String string = (String) PersistanceManager.load(getItem(position), getApplicationContext());

						World world = new World();
						try {
							JSONObject json = new JSONObject(string);
							world.recreateFromJSON(json);
						} catch (JSONException e) {
							world = null;
						}

						if (world != null) {
							Global.setLoadingGame(world);
							startActivity(new Intent(LoadWorldActivity.this, PlayGameActivity.class));
						} else
							Toast.makeText(getApplicationContext(), "Unable to load World", Toast.LENGTH_LONG).show();

					}
				});
				return v;
			}
		});
	}
}
