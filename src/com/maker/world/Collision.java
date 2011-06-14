package com.maker.world;

import java.io.Serializable;

import com.maker.geometry.Line;

public class Collision implements Serializable{
	private float lambda;
	private Line line;
	private WorldObject worldObject;
	private float[] directionVector;
	private float[] intersectionPoint;
	
	public Collision (float lambda, Line line, WorldObject terrain, float [] directionVector, float[] intersectionPoint){
		this.lambda = lambda;
		this.line = line;
		this.worldObject = terrain;
		this.directionVector = directionVector;
		this.intersectionPoint = intersectionPoint;
	}

	public float getLambda() {
		return lambda;
	}

	public void setLambda(float lambda) {
		this.lambda = lambda;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public WorldObject getObject() {
		return worldObject;
	}

	public void setObject(WorldObject worldObject) {
		this.worldObject = worldObject;
	}

	public void setDirectionVector(float[] directionVector) {
		this.directionVector = directionVector;
	}

	public float[] getDirectionVector() {
		return directionVector;
	}

	public void setIntersectionPoint(float[] intersectionPoint) {
		this.intersectionPoint = intersectionPoint;
	}

	public float[] getIntersectionPoint() {
		return intersectionPoint;
	}

}
