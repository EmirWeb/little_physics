package com.maker.world.terrain;

import org.json.JSONException;
import org.json.JSONObject;

import com.maker.R;
import com.maker.geometry.Line;
import com.maker.utilities.JSONUtilities;
import com.maker.utilities.linkedlist.JSONizable;
import com.maker.world.Terrain;
import com.maker.world.WorldObject;

public class Crate implements WorldObject, Terrain, JSONizable {

	private int[] coords;

	private Line[] lines;

	private int id = -1;

	public Crate(int x, int y) {
		coords = new int[] { x, y };

		lines = getLines(x, y);
	}

	public Crate() {
		this(0, 0);
	}

	private Line[] getLines(float x, float y) {
		return new Line[] { new Line(new float[] { x, y + 1 }, new float[] { x + 1, y + 1 }), new Line(new float[] { x + 1, y + 1 }, new float[] { x + 1, y }),
				new Line(new float[] { x + 1, y }, new float[] { x, y }), new Line(new float[] { x, y }, new float[] { x, y + 1 }) };
	}

	@Override
	public int[] getCoords() {
		return coords;
	}

	@Override
	public Line[] getLines() {
		return lines;
	}

	@Override
	public int getTexture() {
		return 1;
	}

	@Override
	public float[] getPosition() {
		return new float[] { coords[0], coords[1] };
	}

	@Override
	public float[] getBoundingBox() {
		float x = coords[0];
		float y = coords[1];
		return new float[] { x, x + 1, y, y + 1 };
	}

	@Override
	public CharSequence getDescription() {
		return "Crate";
	}

	@Override
	public int getIcon() {
		return R.drawable.crate;
	}

	@Override
	public WorldObject New(float x, float y) {
		return new Crate((int) x, (int) y);
	}

	@Override
	public Class getEditActivity() {
		return null;
	}

	@Override
	public boolean isBreakable() {
		return false;
	}

	@Override
	public boolean isWinning() {
		return false;
	}

	@Override
	public boolean isMovable() {
		// TODO Auto-generated method stub
		return false;
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
			json.put("coords", JSONUtilities.IntegersToJSON(coords));
			json.put("lines", JSONUtilities.LinesToJSON(lines));
		} catch (JSONException e) {
		}

		return json;
	}

	@Override
	public void recreateFromJSON(JSONObject object) {
		try {
			coords = JSONUtilities.JSONToIntegers(object.getJSONArray("coords"));
			lines = JSONUtilities.JSONToLines(object.getJSONArray("lines"));
		} catch (JSONException e) {
		}
	}

	@Override
	public float[] getCenter() {
		float x = coords[0];
		float y = coords[1];
		return new float[] {x + .5f, y + .5f};
	}

	@Override
	public float getRadius() {
		return 1.5f;
	}
}
