package com.maker.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.maker.Global;
import com.maker.Listeners.WinningListener;
import com.maker.world.World;
import com.maker.world.WorldObject;

public class PlayGameActivity extends GameActivity {
	World world = Global.getLoadingGame();

	protected void onCreate(Bundle savedInstanceState) {
		world.setWinningListener(new WinningListener() {
			@Override
			public void win(WorldObject w) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "You A WINNA!", Toast.LENGTH_LONG).show();
						finish();
					}
				});
			}
		});

		setWorld(world);
		super.onCreate(savedInstanceState, true);

	}

	@Override
	protected void onDestroy() {
		world.setWinningListener(null);
		super.onDestroy();
	}
}
