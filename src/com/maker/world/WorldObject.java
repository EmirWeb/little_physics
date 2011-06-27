package com.maker.world;

import com.maker.geometry.Line;

public interface WorldObject {

	String getWaterMark();
	
	void setWaterMark(String waterMark);
	
	float[] getCenter();
	
	float getRadius();
	
	float[] getPosition();

	int getTexture();

	int getIcon();

	CharSequence getDescription();

	WorldObject New(float x, float y);

	Class getEditActivity();

	boolean isMovable();

	Line[] getLines();

	float[] getBoundingBox();

	void setId(int bucket);
	
	int getId();
	

}
