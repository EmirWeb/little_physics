package com.maker.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maker.Logger;
import com.maker.R;
import com.maker.Listeners.WinningListener;
import com.maker.dialog.FailureDialog;
import com.maker.dialog.SuccessDialog;
import com.maker.world.Generic;
import com.maker.world.World;
import com.maker.world.WorldObject;
import com.maker.world.terrain.WinningTrigger;

public class GravityLevel2Activity extends GameActivity {

	private boolean animating;
	private Button startStop;
	private EditText angle;

	// In degrees
	public final float INITIAL_ANGLE = 45;
	public final float MAX_ANGLE = 90;

	public final float EARTH_GRAVITY = -9.8f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		World world = getWorld(-9.8f);
		setWorld(world);

		super.onCreate(savedInstanceState, false);

		setBackgroundTexture(22);

		View view = getLayoutInflater().inflate(R.layout.gravity_level_1_activity, null, false);

		TextView description = (TextView) view.findViewById(R.id.description);
		description.setText("The coyote has disstracted the road runner with some bird seed, now is his chance. He is shooting a cannonball at the road runner from 16 meters away, the ball starts moving with a speed of 15 m/s, what angle should the coyote shoot the cannonball to hit ihs target?");

		TextView title = (TextView) view.findViewById(R.id.input_title);
		title.setText("Angle");

		angle = (EditText) view.findViewById(R.id.user_input);
		angle.setText(String.valueOf(INITIAL_ANGLE));

		startStop = (Button) view.findViewById(R.id.start_stop);

		startStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animating = !animating;

				if (animating) {
					String aString = angle.getText().toString();
					if (aString.equals(""))
						aString = "0";
					float angleFloat = Float.parseFloat(aString);

					World world = getWorld(angleFloat);
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
		String aString = angle.getText().toString();
		if (aString.equals(""))
			aString = "0";
		float angleFloat = Float.parseFloat(aString);

		World world = getWorld(angleFloat);
		setWorld(world);

	}

	private World getWorld(float angle) {
		if (angle > MAX_ANGLE)
			angle = MAX_ANGLE;

		World world = new World();
		for (int i = -15; i < 150; i++) {
			WinningTrigger crate = new WinningTrigger(i, -8);
			crate.setWaterMark("GROUND");
			world.add(crate);

			if (i < 20) {
				crate = new WinningTrigger(i, -9);
				crate.setWaterMark("GROUND");
				world.add(crate);
			}
		}
		
		final float startx = -10;
		final float starty = -7;
		final float power = 15f;

		float xPower = (float) (power * Math.cos(Math.toRadians(angle)));
		float yPower = (float) (power * Math.sin(Math.toRadians(angle)));

		Generic cannonBall = new Generic(new float[] { 0, EARTH_GRAVITY }, new float[] { startx, starty }, new float[] { xPower, yPower });
		cannonBall.setImageId(18);
		cannonBall.setWaterMark("CANNONBAL");
		world.add(cannonBall);

		Generic coyote = new Generic(new float[] { 0, 0 }, new float[] { startx - 2.5f, starty }, new float[] { 0, 0 });
		coyote.setIsWinning(false);
		coyote.setImageId(14);
		coyote.setWaterMark("COYOTE");
		world.add(coyote);

		Generic cannon = new Generic(new float[] { 0, 0 }, new float[] { startx - 1.5f, starty }, new float[] { 0, 0 });
		cannon.setIsWinning(false);
		cannon.setImageId(23);
		cannon.setWaterMark("CANNON");
		world.add(cannon);

		Generic roadRunner = new Generic(new float[] { 0, 0 }, new float[] { 6, -7 }, new float[] { 0, 0 });
		roadRunner.setImageId(16);
		roadRunner.setIsWinning(true);
		roadRunner.setWaterMark("ROAD RUNNER");
		world.add(roadRunner);

		Generic birdSeed = new Generic(new float[] { 0, 0 }, new float[] { 7, -7 }, new float[] { 0, 0 });
		birdSeed.setImageId(24);
		birdSeed.setIsWinning(true);
		birdSeed.setWaterMark("BIRD SEED");
		world.add(birdSeed);

		world.setWinningListener(new WinningListener() {

			@Override
			public boolean win(final WorldObject w1, final WorldObject w2) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						String waterMark1 = w1.getWaterMark();
						String waterMark2 = w2.getWaterMark();
						debug(waterMark1 + " " + waterMark2);
						if (waterMark2.equals("ROAD RUNNER") && waterMark1.equals("CANNONBAL") || waterMark1.equals("ROAD RUNNER") && waterMark2.equals("CANNONBAL")) {
							Dialog d = new SuccessDialog(GravityLevel2Activity.this, new OnClickListener() {

								@Override
								public void onClick(View v) {
									startActivity(new Intent(GravityLevel2Activity.this, GravityLevel3Activity.class));
								}
							});
							d.show();
						} else {
							Dialog d = new FailureDialog(GravityLevel2Activity.this);
							d.show();
						}
						stop();
					}
				});
				return true;
			}
		});

		return world;
	}

	private void debug(String message) {
		Logger.debug(getClass(), message);
	}
}
