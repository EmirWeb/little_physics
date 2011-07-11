package com.maker;

import com.maker.world.World;
import com.maker.world.WorldObject;

public class Global {
	public static final int[] ids = { 
			R.drawable.penguin, 		//0
			R.drawable.crate, 			//1
			R.drawable.brick, 			//2
			R.drawable.highlight, 		//3
			R.drawable.red_highlight, 	//4
			R.drawable.paddle, 			//5
			R.drawable.blue_monster,	//6
			R.drawable.cactus, 			//7
			R.drawable.earth, 			//8
			R.drawable.fire, 			//9
			R.drawable.heart, 			//10
			R.drawable.megaman, 		//11
			R.drawable.question_mark, 	//12
			R.drawable.red_circle, 		//13
			R.drawable.coyote, 			//14
			R.drawable.anvil,			//15
			R.drawable.road_runner, 	//16
			R.drawable.dirt, 			//17
			R.drawable.cannon_ball, 	//18
			R.drawable.apple, 			//19
			R.drawable.arrow, 			//20
			R.drawable.bow, 			//21
			R.drawable.canyon, 			//22
			R.drawable.cannon,			//23
			R.drawable.bird_seed,		//24
			R.drawable.hot_air_baloon	//25
		};

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
