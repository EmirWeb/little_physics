package com.maker.world.terrain;

import com.maker.R;
import com.maker.world.WorldObject;

public class Breakable extends Crate {

	public Breakable(float x, float y) {
		super((int) x, (int) y);
	}

	public Breakable() {
		this(0, 0);
	}

	@Override
	public int getTexture() {
		return 2;
	}

	@Override
	public WorldObject New(float x, float y) {
		return new Breakable(x, y);
	}

	@Override
	public boolean isBreakable() {
		return true;
	}

	@Override
	public int getIcon() {
		return R.drawable.brick;
	}

}
