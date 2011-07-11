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

public class FrictionLevel1Activity extends GameActivity {

	private boolean animating;
	private float distance;
	private Button startStop;
	private EditText angle;

	// In degrees
	public final float INITIAL_ANGLE = 45;
	public final float MAX_ANGLE = 90;
	public final float EARTH_GRAVITY = -9.8f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		World world = getWorld(EARTH_GRAVITY);
		setWorld(world);

		super.onCreate(savedInstanceState, false);
		setBackgroundTexture(22);
		View view = getLayoutInflater().inflate(R.layout.gravity_level_1_activity, null, false);

		TextView description = (TextView) view.findViewById(R.id.description);
		description.setText("The roadrunner is in an air baloon ");

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
			crate.setWaterMark("crate");
			world.add(crate);
			if (i < 30) {
				crate = new WinningTrigger(i, -9);
				crate.setWaterMark("crate");
				world.add(crate);
			}
		}

		final float startx = -10;
		final float starty = -7;

		final float fallingStartx = 12;
		final float fallingStarty = 8;

		final float velocity = 200f;

		float xVel = (float) (velocity * Math.cos(Math.toRadians(angle)));
		float yVel = (float) (velocity * Math.sin(Math.toRadians(angle)));

		Generic arrow = new Generic(0f, new float[] { startx, starty }, new float[] { xVel, yVel });
		arrow.setImageId(20);
		arrow.setWaterMark("ARROW");
		world.add(arrow);

		Generic bow = new Generic(0, new float[] { startx - 1, starty }, new float[] { 0, 0 });
		bow.setImageId(21);
		bow.setWaterMark("BOW");
		world.add(bow);

		Generic coyote = new Generic(0, new float[] { startx - 2, starty }, new float[] { 0, 0 });
		coyote.setImageId(14);
		coyote.setWaterMark("COYOTE");
		world.add(coyote);

		Generic apple = new Generic(-2f, new float[] { fallingStartx, fallingStarty }, new float[] { 0, 0 });
		apple.setImageId(25);
		apple.setIsWinning(true);
		apple.setWaterMark("HOT AIR BALLOON");
		world.add(apple);

		world.setWinningListener(new WinningListener() {
			@Override
			public boolean win(final WorldObject w1, final WorldObject w2) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String waterMark1 = w1.getWaterMark();
						String waterMark2 = w2.getWaterMark();
						debug(waterMark1 + " " + waterMark2);
						if (waterMark2.equals("ARROW") && waterMark1.equals("HOT AIR BALLOON") || waterMark1.equals("ARROW") && waterMark2.equals("HOT AIR BALLOON")) {
							Dialog d = new SuccessDialog(FrictionLevel1Activity.this, new OnClickListener() {

								@Override
								public void onClick(View v) {
									startActivity(new Intent(FrictionLevel1Activity.this, GravityLevel3Activity.class));
								}
							});
							d.show();
						} else {
							Dialog d = new FailureDialog(FrictionLevel1Activity.this);
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
