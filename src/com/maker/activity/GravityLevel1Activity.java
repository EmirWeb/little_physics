package com.maker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maker.R;
import com.maker.Listeners.WinningListener;
import com.maker.world.Generic;
import com.maker.world.World;
import com.maker.world.WorldObject;
import com.maker.world.terrain.WinningTrigger;

public class GravityLevel1Activity extends GameActivity {

	private boolean animating;
	private Button startStop;
	private Button next;
	private EditText height;

	public static final float INITIAL_HEIGHT = 0;
	public static final float MAX_HEIGHT = 20;

	public static final float EARTH_GRAVITY = -9.8f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		World world = getWorld(INITIAL_HEIGHT);
		setWorld(world);

		super.onCreate(savedInstanceState, false);

		View view = getLayoutInflater().inflate(R.layout.gravity_level_1_activity, null, false);

		TextView description = (TextView) view.findViewById(R.id.description);
		description.setText("The coyote is 13 units away from the roadrunner. If the roadrunner is running at 8 units per second, from what height should the coyote drop the anvil from? height: [0, 20].");

		TextView title = (TextView) view.findViewById(R.id.input_title);
		title.setText("Height");

		height = (EditText) view.findViewById(R.id.user_input);
		height.setText(String.valueOf(INITIAL_HEIGHT));

		startStop = (Button) view.findViewById(R.id.start_stop);
		next = (Button) view.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GravityLevel1Activity.this, GravityLevel2Activity.class));
			}
		});

		startStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animating = !animating;

				if (animating) {
					String hString = height.getText().toString();
					if (hString.equals(""))
						hString = "0";
					float heightFloat = Float.parseFloat(hString);

					World world = getWorld(heightFloat);
					setWorld(world);
					startStop.setText("Stop");
					setAnimate(animating);
				} else
					stop();
			}

		});

		addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	protected void stop() {
		animating = false;
		setAnimate(animating);

		startStop.setText("Start");
		String hString = height.getText().toString();
		if (hString.equals(""))
			hString = "0";
		float heightFloat = Float.parseFloat(hString);

		World world = getWorld(heightFloat);
		setWorld(world);

	}

	private World getWorld(float height) {
		height = userToWorldHeight(height);

		World world = new World();
		for (int i = -15; i < 150; i++) {
			WinningTrigger crate = new WinningTrigger(i, -8);
			crate.setWaterMark("crate");
			world.add(crate);
		}

		Generic anvil = new Generic(EARTH_GRAVITY, new float[] { 3, height }, new float[] { 0, 0 });
		anvil.setImageId(15);
		anvil.setWaterMark("ANVIL");
		
		Generic roadRunner = new Generic(0, new float[] { -10, userToWorldHeight(0.1f) }, new float[] { 8f, 0 });
		roadRunner.setImageId(16);
		world.add(anvil);
		
		Generic coyote = new Generic(0, new float[] { 4, height }, new float[] { 0, 0 });
		coyote.setImageId(14);
		coyote.setWaterMark("COYOTE");
		world.add(coyote);
		
		roadRunner.setWaterMark("ROAD RUNNER");
		world.add(roadRunner);

		world.setWinningListener(new WinningListener() {

			@Override
			public void win(final WorldObject w) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String waterMark = w.getWaterMark();
						if (waterMark.equals("ROAD RUNNER"))
							Toast.makeText(getApplicationContext(), "dead", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getApplicationContext(), "FAil", Toast.LENGTH_SHORT).show();

						stop();
					}
				});
			}
		});

		return world;
	}

	// The user will assume the crates at the bottom is height 0 for the user,
	// this accounts for that
	private float userToWorldHeight(float height) {
		float newHeight = height;
		if (newHeight > MAX_HEIGHT)
			newHeight = MAX_HEIGHT;

		newHeight = newHeight - 7;

		return newHeight;
	}
}
