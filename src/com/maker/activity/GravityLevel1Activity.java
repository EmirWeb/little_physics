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

public class GravityLevel1Activity extends GameActivity {
	private boolean animating;
	private Button startStop;
	private EditText height;

	public final float INITIAL_HEIGHT = 0;
	public final float MAX_HEIGHT = 50;

	public final float EARTH_GRAVITY = -9.8f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		World world = getWorld(INITIAL_HEIGHT);
		setWorld(world);

		super.onCreate(savedInstanceState, false);
		
		setBackgroundTexture(22);
		
		View view = getLayoutInflater().inflate(R.layout.gravity_level_1_activity, null, false);

		TextView description = (TextView) view.findViewById(R.id.description);
		description.setText("The coyote is 13 meters away from the roadrunner. If the roadrunner is running at 5 meters per second towards the coyote and the coyote's 1 meter wide anvil is next to him, from what height should the coyote drop the anvil on the 1 meter road runner?.");

		TextView title = (TextView) view.findViewById(R.id.input_title);
		title.setText("Height");

		height = (EditText) view.findViewById(R.id.user_input);
		height.setText(String.valueOf(INITIAL_HEIGHT));

		startStop = (Button) view.findViewById(R.id.start_stop);

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
	}

	private World getWorld(float height) {
		height = userToWorldHeight(height);

		World world = new World();
		for (int i = -15; i < 20; i++) {
			WinningTrigger crate = new WinningTrigger(i, -9);
			crate.setWaterMark("crate");
			world.add(crate);
			
			crate = new WinningTrigger(i, -8);
			crate.setWaterMark("crate");
			world.add(crate);
		}

		Generic anvil = new Generic(new float[] {0,EARTH_GRAVITY}, new float[] { 3, height }, new float[] { 0, 0 });
		anvil.setImageId(15);
		anvil.setWaterMark("ANVIL");
		world.add(anvil);
		
		Generic roadRunner = new Generic(new float[] {0,0}, new float[] { -10, userToWorldHeight(0.1f) }, new float[] { 5f, 0 });
		roadRunner.setImageId(16);
		roadRunner.setIsWinning(true);
		roadRunner.setWaterMark("ROAD RUNNER");
		world.add(roadRunner);
		
		Generic coyote = new Generic(new float[] {0,0}, new float[] { 4.5f, height }, new float[] { 0, 0 });
		coyote.setIsWinning(false);
		coyote.setImageId(14);
		coyote.setWaterMark("COYOTE");
		world.add(coyote);

		world.setWinningListener(new WinningListener() {

			@Override
			public boolean win(final WorldObject w1, final WorldObject w2) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String waterMark1 = w1.getWaterMark();
						String waterMark2 = w2.getWaterMark();
						debug(waterMark1 + " " + waterMark2);
						if (waterMark2.equals("ROAD RUNNER") && waterMark1.equals("ANVIL") || waterMark1.equals("ROAD RUNNER") && waterMark2.equals("ANVIL")){
							Dialog d = new SuccessDialog(GravityLevel1Activity.this, new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									startActivity(new Intent(GravityLevel1Activity.this, GravityLevel2Activity.class));
								}
							});
							d.show();
						}else{
							Dialog d = new FailureDialog(GravityLevel1Activity.this);
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

	private float userToWorldHeight(float height) {
		return Math.min(height, MAX_HEIGHT) - 7;
	}
	
	private void debug(String message){
		Logger.debug(getClass(), message);
	}
	
	@Override
	protected void onDestroy() {
		if (startStop != null)
			startStop.setOnClickListener(null);
		startStop = null;
		height = null;
		super.onDestroy();
	}
}
