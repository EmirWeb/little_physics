package com.maker.world;

import com.maker.geometry.Line;

public interface Terrain {
	boolean isWinning();

	boolean isBreakable();

	int[] getCoords();

	Line[] getLines();


}
