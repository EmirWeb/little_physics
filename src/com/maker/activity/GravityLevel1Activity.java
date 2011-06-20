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
import com.maker.world.terrain.WinningTrigger;

public class GravityLevel1Activity extends GameActivity {

	private boolean animating;
	private long startTime;
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
				startActivity(new Intent(GravityLevel1Activity.this, GravityLevel2Activity.class));
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
					startTime = System.currentTimeMillis();
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
		for (int i = -5; i < 5; i++) {
			WinningTrigger crate = new WinningTrigger(i, -8);
			world.add(crate);
		}

		FallingObject fallingObject = new FallingObject(gravity, new float[] { 0, 0 }, new float[]{0,0});
		world.add(fallingObject);
		world.setWinningListener(new WinningListener() {

			@Override
			public void win(WorldObject w) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						long time = (System.currentTimeMillis() - startTime);
						long milliseconds = time % 1000 / 10;
						long seconds = time / 1000;
						timer.setText(seconds + ":" + milliseconds);

						stop();
					}
				});
			}
		});

		return world;
	}
}
