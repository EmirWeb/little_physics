package com.maker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maker.R;
import com.maker.Listeners.WinningListener;
import com.maker.world.World;
import com.maker.world.WorldObject;
import com.maker.world.mobile.FallingObject;
import com.maker.world.terrain.Crate;
import com.maker.world.terrain.WinningTrigger;

public class GravityLevel2Activity extends GameActivity {

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
		description.setText("Try to get the ball in the basket. The angle interval is [0, 90].");
		
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
				startActivity(new Intent(GravityLevel2Activity.this, GravityLevel3Activity.class));
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
			Crate crate = new Crate(i, -8);
			world.add(crate);
		}
		final float startx = -10;
		final float starty = -7;
		final float power = 0.6f;

		float xPower = (float) (power * Math.cos(Math.toRadians(angle)));
		float yPower = (float) (power * Math.sin(Math.toRadians(angle)));
		
		FallingObject fallingObject = new FallingObject(EARTH_GRAVITY, new float[] { startx, starty }, new float[] { xPower, yPower });		
		world.add(fallingObject);
		
		WinningTrigger target = new WinningTrigger(6,-7);
		world.add(target);
		
		world.setWinningListener(new WinningListener() {

			@Override
			public void win(final WorldObject worldObject) {
				runOnUiThread(new Runnable() {
					

					@Override
					public void run() {
						
						float[] pos = worldObject.getPosition().clone();
						pos[0] -= startx;
						pos[1] -= starty;
						
						distance = pos[0];
						timer.setText("distance: " + distance);
						stop();
					}
				});
			}
		});

		return world;
	}

}
