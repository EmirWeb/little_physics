package com.maker.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.maker.Global;
import com.maker.R;
import com.maker.world.WorldObject;

public class CustomObjectActivity extends Activity {

	private int chosenImage = R.drawable.blue_monster;
	private boolean isBreakable;
	private boolean isWinning;
	private boolean isMovable;
	private boolean isMobile;
	private boolean isTerrain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_object_activity);

		LinearLayout layout = (LinearLayout) findViewById(R.id.custom_layout);

		TextView title = new TextView(getApplicationContext());
		title.setText("Mobile");
		isMobile = true;
		CheckBox breakable = new CheckBox(getApplicationContext());
		CheckBox winning = new CheckBox(getApplicationContext());
		breakable.setText("Breakable");
		winning.setText("Winning");
		layout.addView(title);
		layout.addView(breakable);
		layout.addView(winning);
		
		OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				RadioButton terrain = (RadioButton) findViewById(R.id.terrain);

				LinearLayout layout = (LinearLayout) findViewById(R.id.custom_layout);

				TextView title = new TextView(getApplicationContext());
				if (buttonView == terrain && isChecked) {
					isMobile = false;
					isTerrain = true;
					isMovable = false;
					isBreakable = false;
					isWinning = false;
					
					layout.removeAllViews();
					title.setText("Terrain");
					CheckBox movable = new CheckBox(getApplicationContext());
					movable.setText("Movable");
					layout.addView(title);
					layout.addView(movable);
				} else if (isChecked) {
					isMobile = true;
					isTerrain = false;
					isMovable = false;
					isBreakable = false;
					isWinning = false;

					
					layout.removeAllViews();
					title.setText("Mobile");
					CheckBox breakable = new CheckBox(getApplicationContext());
					CheckBox winning = new CheckBox(getApplicationContext());
					breakable.setText("Breakable");
					winning.setText("Winning");
					layout.addView(title);
					layout.addView(breakable);
					layout.addView(winning);
				}

			}
		};

		RadioButton terrain = (RadioButton) findViewById(R.id.terrain);
		terrain.setOnCheckedChangeListener(listener);

		RadioButton mobile = (RadioButton) findViewById(R.id.mobile);
		mobile.setOnCheckedChangeListener(listener);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WorldObject worldObject = null;
				Global.setCustomObject(worldObject);
			}
		});

		ArrayList<Integer> images = new ArrayList<Integer>();
		for (int i = 0; i < Global.ids.length; i++)
			images.add(Global.ids[i]);

		ImageView image = (ImageView) findViewById(R.id.preview);

		Drawable icon = getResources().getDrawable(chosenImage);
		image.setBackgroundDrawable(icon);

		ListView imageList = (ListView) findViewById(R.id.image_list);

		imageList.setAdapter(new ArrayAdapter<Integer>(getApplicationContext(), 0, images) {
			@Override
			public View getView(final int position, View v, ViewGroup parent) {
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.image_list_item, null);
				}

				ImageView image = (ImageView) v.findViewById(R.id.icon);

				Drawable icon = getResources().getDrawable(getItem(position));
				image.setBackgroundDrawable(icon);

				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageView image = (ImageView) findViewById(R.id.preview);
						chosenImage = getItem(position);
						Drawable icon = getResources().getDrawable(chosenImage);
						image.setBackgroundDrawable(icon);
					}
				});

				return v;
			}
		});

	}
}
