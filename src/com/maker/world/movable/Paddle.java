package com.maker.world.movable;

import org.json.JSONException;
import org.json.JSONObject;

import com.maker.Logger;
import com.maker.R;
import com.maker.geometry.Line;
import com.maker.utilities.Algebra;
import com.maker.utilities.JSONUtilities;
import com.maker.utilities.linkedlist.JSONizable;
import com.maker.world.Collision;
import com.maker.world.Mobile;
import com.maker.world.Movable;
import com.maker.world.WorldObject;

public class Paddle implements Movable, WorldObject, Mobile, JSONizable {

	private int id = -1;
	private float[] position;
	private float[] touch;
	private Line[] lines;

	public Paddle(float x, float y) {
		position = new float[] { x, y };
		lines = getLines(x, y);
	}

	public Paddle() {
		this(0, 0);
	}

	private Line[] getLines(float x, float y) {
		return new Line[] { new Line(new float[] { x, y + 1 }, new float[] { x + 1, y + 1 }), new Line(new float[] { x + 1, y + 1 }, new float[] { x + 1, y }),
				new Line(new float[] { x + 1, y }, new float[] { x, y }), new Line(new float[] { x, y }, new float[] { x, y + 1 }) };
	}

	@Override
	public void setTouch(float[] touch) {
		this.touch = touch;
	}

	@Override
	public boolean hasTouch() {
		return touch != null && touch.length >= 2;
	}

	@Override
	public float[] getPosition() {
		return position;
	}

	@Override
	public int getTexture() {
		return 5;
	}

	@Override
	public int getIcon() {
		return R.drawable.paddle;
	}

	@Override
	public CharSequence getDescription() {
		return "Paddle";
	}

	@Override
	public WorldObject New(float x, float y) {
		return new Paddle(x, y);
	}

	@Override
	public Class getEditActivity() {
		return null;
	}

	@Override
	public boolean isMovable() {
		return true;
	}

	@Override
	public void move(float[] vector) {
		position = Algebra.add(vector, position);
		lines = getLines(position[0], position[1]);
	}

	@Override
	public float[] getDirectionVector(long timeChange) {
		if (hasTouch())
			return Algebra.subtract(touch, position);
		return new float[] { 0, 0 };
	}

	@Override
	public float[] getBoundingBox(float[] directionVector) {
		float[] results = new float[] { Float.MAX_VALUE, -Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE };
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < 2; j++) {
				float[] p1 = lines[i].getPoints()[j];
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
		if(Math.abs(results[0] - results[1]) > 9 ){
			Logger.debug(getClass(), "YO");
		}
		
		return results;
	}

	@Override
	public float[] setCollision(Collision collision) {
		float lambda = collision.getLambda();
		float[] directionVector = collision.getDirectionVector();

		if (lambda <= 0 || lambda > 1)
			return null;
		lambda -= lambda / 5f;

		float fullX = directionVector[0];
		float fullY = directionVector[1];

		float[] move = new float[] { fullX * lambda, fullY * lambda };

		move(move);
		return null;
	}

	@Override
	public Line[] getLines() {
		return lines;
	}

	@Override
	public float[] getCollision(Collision collision) {
		return null;
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
			json.put("position", JSONUtilities.FloatsToJSON(position));
			json.put("touch", JSONUtilities.FloatsToJSON(touch));
			json.put("lines", JSONUtilities.LinesToJSON(lines));
		} catch (JSONException e) {
		}
		
		return json;
	}

	@Override
	public void recreateFromJSON(JSONObject object) {
		try {
			position = JSONUtilities.JSONToFloats(object.getJSONArray("position"));
			touch = JSONUtilities.JSONToFloats(object.getJSONArray("touch"));
			lines = JSONUtilities.JSONToLines(object.getJSONArray("lines"));
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
