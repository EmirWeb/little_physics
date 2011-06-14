package com.maker.world.mobile;

import org.json.JSONException;
import org.json.JSONObject;

import com.maker.Logger;
import com.maker.R;
import com.maker.activity.EditPenguinActivity;
import com.maker.geometry.Line;
import com.maker.utilities.Algebra;
import com.maker.utilities.JSONUtilities;
import com.maker.utilities.linkedlist.JSONizable;
import com.maker.world.Collision;
import com.maker.world.Mobile;
import com.maker.world.WorldObject;

public class SimpleReflection implements WorldObject, Mobile, JSONizable {

	private float miliseconds = 1000;

	private Line[] lines;
	private float[] directionVector;
	private float[] position;

	private int id = -1;
	private float interval;
	private float magnitude;

	public SimpleReflection(float[] currentPosition, float[] velocity) {
		this.position = currentPosition;
		this.directionVector = new float[] { velocity[0], velocity[1] };

		if (velocity[2] == 0) {
			interval = miliseconds;
			magnitude = 0;
		} else {
			interval = velocity[2] * miliseconds;
			magnitude = Algebra.getMagnitude(directionVector);
		}

		lines = getLines(currentPosition[0], currentPosition[1]);
	}

	public SimpleReflection() {
		this(new float[2], new float[3]);
	}

	public float[] getDirectionVector(long timeChange) {
		float t = timeChange;
		return Algebra.multiplyVectorByConstant(directionVector, magnitude * t / interval);
	}

	@Override
	public void move(float[] vector) {
		float x = vector[0];
		float y = vector[1];

		position[0] = position[0] + x;
		position[1] = position[1] + y;

		lines = getLines(position[0], position[1]);

	}

	private Line[] getLines(float x, float y) {
		return new Line[] { new Line(new float[] { x, y + 1 }, new float[] { x + 1, y + 1 }), new Line(new float[] { x + 1, y + 1 }, new float[] { x + 1, y }),
				new Line(new float[] { x + 1, y }, new float[] { x, y }), new Line(new float[] { x, y }, new float[] { x, y + 1 }) };
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

	@Override
	public float[] getCollision(Collision collision) {
		float lambda = collision.getLambda();
		float[] directionVector = collision.getDirectionVector();

		Line line = collision.getLine();

		Logger.debug(getClass(), "getCollision");
		Logger.debug(getClass(), "lambda: " + lambda);
		Logger.debug(getClass(), "directionVector x: " + directionVector[0] + " y: " + directionVector[1]);
		Logger.debug(getClass(), "LINE");
		Logger.debug(getClass(), "1 x: " + line.getPoints()[0][0] + " y: " + line.getPoints()[0][1]);
		Logger.debug(getClass(), "2 x: " + line.getPoints()[1][0] + " y: " + line.getPoints()[1][1]);
		Logger.debug(getClass(), "POSITION");
		Logger.debug(getClass(), "1 x: " + position[0] + " y: " + position[1]);

		if (lambda < 0 || lambda > 1)
			return null;
		lambda -= lambda / 100f;

		float fullX = directionVector[0];
		float fullY = directionVector[1];

		float[] move = new float[] { fullX * lambda, fullY * lambda };
		float[] remainder = new float[] { fullX - move[0], fullY - move[1] };
		float[] reflectionVector = Algebra.getReflection(remainder, collision.getLine().getNormal());

		float magnitude = Algebra.getMagnitude(reflectionVector);
		float[] normalized = Algebra.divideVectorByConstant(reflectionVector, magnitude);
		this.directionVector = Algebra.multiplyVectorByConstant(normalized, this.magnitude);

		return reflectionVector;
	}

	@Override
	public float[] setCollision(Collision collision) {
		float lambda = collision.getLambda();
		float[] directionVector = collision.getDirectionVector();

		Line line = collision.getLine();

		Logger.debug(getClass(), "setCollision");
		Logger.debug(getClass(), "lambda: " + lambda);
		Logger.debug(getClass(), "directionVector x: " + directionVector[0] + " y: " + directionVector[1]);
		Logger.debug(getClass(), "LINE");
		Logger.debug(getClass(), "1 x: " + line.getPoints()[0][0] + " y: " + line.getPoints()[0][1]);
		Logger.debug(getClass(), "2 x: " + line.getPoints()[1][0] + " y: " + line.getPoints()[1][1]);
		Logger.debug(getClass(), "POSITION");
		Logger.debug(getClass(), "1 x: " + position[0] + " y: " + position[1]);

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
		this.directionVector = Algebra.multiplyVectorByConstant(normalized, this.magnitude);

		return reflectionVector;
	}

	@Override
	public Line[] getLines() {
		return lines;
	}

	@Override
	public float[] getPosition() {
		return position;
	}

	@Override
	public int getTexture() {
		return 0;
	}

	@Override
	public int getIcon() {
		return R.drawable.penguin;
	}

	@Override
	public CharSequence getDescription() {
		return "Simple Moving Object";
	}

	@Override
	public WorldObject New(float x, float y) {
		return new SimpleReflection(new float[] { x, y }, new float[3]);
	}

	@Override
	public Class getEditActivity() {
		return EditPenguinActivity.class;
	}

	public void setVelocity(float[] fs) {
		if (fs.length < 3)
			return;

		directionVector = new float[] { fs[0], fs[1] };
		magnitude = fs[2];
	}

	@Override
	public boolean isMovable() {
		return false;
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
		JSONObject json = new JSONObject();

		try {
			json.put("lines", JSONUtilities.LinesToJSON(lines));
			json.put("directionVector", JSONUtilities.FloatsToJSON(directionVector));
			json.put("position", JSONUtilities.FloatsToJSON(position));
			json.put("interval", interval);
			json.put("magnitude", magnitude);
		} catch (JSONException e) {
		}

		return json;
	}

	@Override
	public void recreateFromJSON(JSONObject object) {
		try {
			directionVector = JSONUtilities.JSONToFloats(object.getJSONArray("directionVector"));
			position = JSONUtilities.JSONToFloats(object.getJSONArray("position"));
			interval = (float) object.getDouble("interval");
			magnitude = (float) object.getDouble("magnitude");
		} catch (JSONException e) {

		}
	}

	@Override
	public float[] getCenter() {
		float x = position[0] + .5f;
		float y = position[1] + .5f;

		return new float[] { x, y };
	}

	@Override
	public float getRadius() {
		return 1.5f;
	}

}
