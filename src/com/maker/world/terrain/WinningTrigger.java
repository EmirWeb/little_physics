package com.maker.world.terrain;

import com.maker.R;
import com.maker.world.WorldObject;

public class WinningTrigger extends Crate {

	public WinningTrigger(float x, float y) {
		super((int) x, (int) y);
	}

	public WinningTrigger() {
		this(0, 0);
	}
	
	@Override
	public int getTexture() {
		return 4;
	}

	@Override
	public WorldObject New(float x, float y) {
		return new WinningTrigger(x, y);
	}

	@Override
	public boolean isBreakable() {
		return false;
	}

	@Override
	public int getIcon() {
		return R.drawable.red_highlight;
	}
	
	@Override
	public boolean isWinning() {
		return true;
	}

}
