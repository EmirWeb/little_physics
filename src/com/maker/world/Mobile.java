package com.maker.world;

import com.maker.geometry.Line;

public interface Mobile {
	void move(float[] vector);
	
	public float[] getDirectionVector(long timeChange);
	float[] getBoundingBox(float[] directionVector);
	float[] setCollision(Collision collision);
	Line[] getLines();

	float[] getCollision(Collision collision);
}
