package com.maker.world;

import java.io.Serializable;

import com.maker.R;
import com.maker.geometry.Line;

public class Eraser implements WorldObject, Serializable {
	private float[] position;
	private int id = -1;
	private String waterMark;

	public Eraser(){
		this(new float[2]);
	}
	
	public Eraser(float[] position) {
		this.position = position;
	}

	@Override
	public float[] getPosition() {
		return position;
	}

	@Override
	public int getTexture() {
		return -1;
	}

	@Override
	public int getIcon() {
		return R.drawable.highlight;
	}

	@Override
	public CharSequence getDescription() {
		return "Eraser";
	}

	@Override
	public WorldObject New(float x, float y) {
		return new Eraser(new float[] { x, y });
	}

	@Override
	public Class getEditActivity() {
		return null;
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public Line[] getLines() {
		return null;
	}

	@Override
	public float[] getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(int bucket) {
		id = bucket;
		
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public float[] getCenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getWaterMark() {
		return waterMark;
	}

	@Override
	public void setWaterMark(String waterMark) {
		this.waterMark = waterMark;
	}
}
