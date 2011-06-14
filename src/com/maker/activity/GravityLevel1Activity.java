package com.maker.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.maker.R;
import com.maker.Listeners.WinningListener;
import com.maker.world.World;
import com.maker.world.mobile.FallingObject;
import com.maker.world.terrain.Crate;
import com.maker.world.terrain.WinningTrigger;

public class GravityLevel1Activity extends GameActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		World world = getWorld();
		setWorld(world);
		
		
		super.onCreate(savedInstanceState, true);

		final View view = getLayoutInflater().inflate(R.layout.gravity_level_1_activity, null, false);

		addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	private World getWorld() {
		World world = new World();
		for (int i = -5; i < 5; i++){
			WinningTrigger crate = new WinningTrigger (i,-8);
			world.add(crate);
		}
		
		FallingObject fallingObject = new FallingObject(new float[] { 0,0});
		world.add(fallingObject);
		world.setWinningListener(new WinningListener() {
			
			@Override
			public void win() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "WINNING!", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
		return world;
	}

}
