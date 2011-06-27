package com.maker.world;

import java.io.Serializable;

import com.maker.R;
import com.maker.geometry.Line;

public class Custom implements WorldObject, Serializable{

	private int id = -1;
	private String waterMark;
	
	@Override
	public float[] getPosition() {
		return null;
	}

	@Override
	public int getTexture() {
		return 0;
	}

	@Override
	public int getIcon() {
		return R.drawable.custom;
	}

	@Override
	public CharSequence getDescription() {
		return "Custom";
	}

	@Override
	public WorldObject New(float x, float y) {
		return null;
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
		return new float[] {0,0,0};
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
