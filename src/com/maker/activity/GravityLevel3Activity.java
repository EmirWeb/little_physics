package com.maker.activity;

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
import com.maker.world.mobile.FallingObject;
import com.maker.world.terrain.Crate;
import com.maker.world.terrain.WinningTrigger;

public class GravityLevel3Activity extends GameActivity {

	private boolean animating;
	private float distance;
	private TextView timer;
	private Button startStop;
	private Button next;
	private EditText angle;

	// In degrees
	public static final float INITIAL_ANGLE = 45;
	public static final float MAX_ANGLE = 90;
	
	public static final float EARTH_GRAVITY = -9.8f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		World world = getWorld(-9.8f);
		setWorld(world);

		super.onCreate(savedInstanceState, false);

		View view = getLayoutInflater().inflate(R.layout.gravity_level_1_activity, null, false);
		
		TextView description = (TextView) view.findViewById(R.id.description);
		description.setText("An apple 22 units away from you is falling from a height of 15 units. If you shoot the arrow at an initial speed of 20 units per second, at what angle do you need to fire the arrow? (How relevant is the initial speed of the arrow?). angle: [0, 90].");
		
		TextView title = (TextView) view.findViewById(R.id.input_title);
		title.setText("Angle");
		
		timer = (TextView) view.findViewById(R.id.timer);
		angle = (EditText) view.findViewById(R.id.user_input);
		angle.setText(String.valueOf(INITIAL_ANGLE));

		startStop = (Button) view.findViewById(R.id.start_stop);
		next = (Button) view.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

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
		}
		
		final float startx = -10;
		final float starty = -7;
		
		final float fallingStartx = 12;
		final float fallingStarty = 8;
		
		final float velocity = 20f;

		float xVel = (float) (velocity * Math.cos(Math.toRadians(angle)));
		float yVel = (float) (velocity * Math.sin(Math.toRadians(angle)));
		
		Generic arrow = new Generic(EARTH_GRAVITY, new float[] { startx, starty }, new float[] { xVel, yVel });
		arrow.setImageId(20);
		arrow.setWaterMark("ARROW");
		world.add(arrow);
		
		Generic bow = new Generic(0, new float[] { startx - 1, starty }, new float[] { 0, 0 });
		bow.setImageId(21);
		world.add(bow);
		
		Generic apple = new Generic(EARTH_GRAVITY, new float[] { fallingStartx, fallingStarty }, new float[] { 0, 0 });
		apple.setImageId(19);
		apple.setWaterMark("APPLE");
		world.add(apple);
		
		world.setWinningListener(new WinningListener() {

			@Override
			public void win(final WorldObject w) {
				runOnUiThread(new Runnable() {
					

					@Override
					public void run() {
						float[] pos = w.getPosition().clone();
						pos[0] -= startx;
						pos[1] -= starty;
						
						distance = pos[0];
						timer.setText("distance: " + distance);

						
						String waterMark = w.getWaterMark();							
						if (waterMark.equals("APPLE"))
							Toast.makeText(getApplicationContext(), "GOT IT!", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getApplicationContext(), "FAil", Toast.LENGTH_SHORT).show();

						
						
						stop();
					}
				});
			}
		});

		return world;
	}

}
