package com.maker;

import com.maker.world.World;
import com.maker.world.WorldObject;

public class Global {
	public static final int[] ids = { R.drawable.penguin, R.drawable.crate, R.drawable.brick, R.drawable.highlight, R.drawable.red_highlight, R.drawable.paddle, R.drawable.blue_monster,
			R.drawable.cactus, R.drawable.earth, R.drawable.fire, R.drawable.heart, R.drawable.megaman, R.drawable.question_mark, R.drawable.red_circle, R.drawable.coyote, R.drawable.anvil,
			R.drawable.road_runner, R.drawable.dirt, R.drawable.cannon_ball, R.drawable.apple, R.drawable.arrow, R.drawable.bow };

	private static WorldObject editingObject;
	private static World savingWorld;
	private static World loadingWorld;
	private static WorldObject customObject;

	public static void setEditingObject(WorldObject object) {
		editingObject = object;
	}

	public static WorldObject getEditingObject() {
		return editingObject;
	}

	public static void setSavingWorld(World world) {
		savingWorld = world;
	}

	public static World getSavingWorld() {
		return savingWorld;
	}

	public static World getLoadingGame() {
		return loadingWorld;
	}

	public static void setLoadingGame(World world) {
		loadingWorld = world;
	}

	public static void setCustomObject(WorldObject worldObject) {
		customObject = worldObject;
	}

	public static WorldObject getCustomObject() {
		return customObject;
	}
}
