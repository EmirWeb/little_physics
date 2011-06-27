package com.maker.world;

import org.json.JSONObject;

import com.maker.R;
import com.maker.geometry.Line;
import com.maker.utilities.Algebra;
import com.maker.utilities.linkedlist.JSONizable;
import com.maker.world.mobile.FallingObject;

public class Generic implements WorldObject, Terrain, Mobile, JSONizable {
	private float[] position = new float[] { 0, 0 };
	private Line[] lines;
	private float[] directionVector = new float[] { 0, 0 };
	private int id;
	private final float frame = 500;
	private float lastInterval = -1;
	private float gravity;
	private int imageId = 0;
	private String waterMark;
	
	public void setImageId(int imageId){
		this.imageId = imageId;
	}

	public Generic(float gravity, float[] position, float[] fs) {
		if (gravity > 0)
			gravity = -gravity;
		this.gravity = gravity;
		this.position = position;
		directionVector = fs;
		lines = getLines();
	}

	@Override
	public float[] getCenter() {
		return new float[] { position[0] + .5f, position[1] + .5f };
	}

	@Override
	public float getRadius() {
		return 0.5f;
	}

	@Override
	public float[] getPosition() {
		return position;
	}

	@Override
	public int getTexture() {
		return imageId;
	}

	@Override
	public int getIcon() {
		return R.drawable.penguin;
	}

	@Override
	public CharSequence getDescription() {
		return "Gravity Object";
	}

	@Override
	public WorldObject New(float x, float y) {
		FallingObject fallingObject = new FallingObject(gravity, new float[] { x, y }, directionVector.clone());
		return fallingObject;
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
		float x = position[0];
		float y = position[1];
		return new Line[] { new Line(new float[] { x, y + 1 }, new float[] { x + 1, y + 1 }), new Line(new float[] { x + 1, y + 1 }, new float[] { x + 1, y }),
				new Line(new float[] { x + 1, y }, new float[] { x, y }), new Line(new float[] { x, y }, new float[] { x, y + 1 }) };
	}

	@Override
	public float[] getBoundingBox() {
		return getBoundingBox(new float[] { 0, 0, 0 });
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
	public JSONObject toJSON() {
		return null;
	}

	@Override
	public void recreateFromJSON(JSONObject object) {

	}

	public float[] getCollision(Collision collision) {
		float lambda = collision.getLambda();
		float[] directionVector = collision.getDirectionVector();

		if (lambda < 0 || lambda > 1)
			return null;
		lambda -= lambda / 100f;

		float fullX = directionVector[0];
		float fullY = directionVector[1];

		float[] move = new float[] { fullX * lambda, fullY * lambda };
		float[] remainder = new float[] { fullX - move[0], fullY - move[1] };
		float[] reflectionVector = Algebra.getReflection(remainder, collision.getLine().getNormal());

		return reflectionVector;
	}

	@Override
	public float[] setCollision(Collision collision) {
		float lambda = collision.getLambda();
		float[] directionVector = collision.getDirectionVector();

		if (lambda < 0 || lambda > 1)
			return null;
		lambda -= lambda / 100f;

		float fullX = directionVector[0];
		float fullY = directionVector[1];

		float[] move = new float[] { fullX * lambda, fullY * lambda };
		float[] remainder = new float[] { fullX - move[0], fullY - move[1] };
		float[] reflectionVector = Algebra.getReflection(remainder, collision.getLine().getNormal());

		move(move);

		float magnitude = Algebra.getMagnitude(reflectionVector);
		float[] normalized = Algebra.divideVectorByConstant(reflectionVector, magnitude);
		float reflectedMagnitude = (1f - lambda) * Algebra.getMagnitude(directionVector);
		this.directionVector = Algebra.multiplyVectorByConstant(normalized, reflectedMagnitude);

		return reflectionVector;
	}

	@Override
	public void move(float[] vector) {
		float x = vector[0];
		float y = vector[1];

		position[0] = position[0] + x;
		position[1] = position[1] + y;

		lines = getLines(position[0], position[1]);
	}

	private static Line[] getLines(float x, float y) {
		return new Line[] { new Line(new float[] { x, y + 1 }, new float[] { x + 1, y + 1 }), new Line(new float[] { x + 1, y + 1 }, new float[] { x + 1, y }),
				new Line(new float[] { x + 1, y }, new float[] { x, y }), new Line(new float[] { x, y }, new float[] { x, y + 1 }) };
	}

	@Override
	public float[] getDirectionVector(long timeChange) {
		lastInterval = timeChange;
		float ratio = getRatio(timeChange);
		directionVector[1] += gravity * ratio;
				
		return Algebra.multiplyVectorByConstant(directionVector, ratio);
	}

	@Override
	public float[] getBoundingBox(float[] directionVector) {
		float[] results = new float[] { Float.MAX_VALUE, -Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE };
		for (int i = 0; i < lines.length; i++) {
			float[][] points = lines[i].getPoints();
			for (int j = 0; j < points.length; j++) {
				float[] p1 = points[j];
				// left
				results[0] = Math.min(p1[0], results[0]);
				results[0] = Math.min(results[0], p1[0] + directionVector[0]);
				// right
				results[1] = Math.max(p1[0], results[1]);
				results[1] = Math.max(results[1], p1[0] + directionVector[0]);
				// top
				results[2] = Math.min(p1[1], results[2]);
				results[2] = Math.min(results[2], p1[1] + directionVector[1]);
				// bottom
				results[3] = Math.max(p1[1], results[3]);
				results[3] = Math.max(results[3], p1[1] + directionVector[1]);
			}
		}
		return results;
	}

	private float getRatio(float timeChange) {
		float ratio = ((float) timeChange) / ((float) frame);
		return ratio;
	}

	@Override
	public boolean isWinning() {
		return true;
	}

	@Override
	public boolean isBreakable() {
		return false;
	}

	@Override
	public int[] getCoords() {
		return new int[] { (int) position[0], (int) position[1] };
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
