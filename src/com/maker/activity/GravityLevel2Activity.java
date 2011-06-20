package com.maker.activity;

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

public class GravityLevel2Activity extends GameActivity {

	private boolean animating;
	private float distance;
	private TextView timer;
	private Button startStop;
	private Button next;
	private EditText gravity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		World world = getWorld(-9.8f);
		setWorld(world);

		super.onCreate(savedInstanceState, false);

		View view = getLayoutInflater().inflate(R.layout.gravity_level_1_activity, null, false);
		timer = (TextView) view.findViewById(R.id.timer);
		gravity = (EditText) view.findViewById(R.id.gravity);

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
					String gString = gravity.getText().toString();
					if (gString.equals(""))
						gString = "0";
					float gravityFloat = Float.parseFloat(gString);

					World world = getWorld(gravityFloat);
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
		String gString = gravity.getText().toString();
		if (gString.equals(""))
			gString = "0";
		float gravityFloat = Float.parseFloat(gString);
		
		World world = getWorld(gravityFloat);
		setWorld(world);

	}

	private World getWorld(float gravity) {
		World world = new World();
		for (int i = -15; i < 150; i++) {
			Crate crate = new Crate(i, -8);
			world.add(crate);
		}
		final float startx = -10;
		final float starty = -7;

		FallingObject fallingObject = new FallingObject(gravity, new float[] { startx, starty }, new float[] { 0.05f, 0.05f });
		world.add(fallingObject);
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
