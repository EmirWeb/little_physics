package com.maker.geometry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.maker.Logger;
import com.maker.utilities.JSONUtilities;
import com.maker.utilities.linkedlist.JSONizable;

public class Line implements JSONizable {
	private float[][] points;
	private float[] vector;
	private String COULD_NOT_JSONIZE = "Could not JSONize";

	public Line(JSONObject line){
		recreateFromJSON(line);
	}
	
	public Line(float[] p1, float[] p2) {
		points = new float[][] { p1, p2 };
		vector = new float[] { p2[0] - p1[0], p2[1] - p1[1] };
	}

	public float[][] getPoints() {
		return points;
	}

	public float[] getVector() {
		return vector;
	}

	public float[] getPoint(float lambda) {
		return new float[] { points[0][0] + lambda * points[1][0], points[0][1] + lambda * points[1][1] };
	}

	public float[] getNormal() {
		return new float[] { -vector[1], vector[0] };
	}

	public static Line[] moveLines(Line[] lines, float[] vector) {
		Line[] result = Line.clone(lines);
		for (int i = 0; i < lines.length; i++)
			result[i].move(vector);
		return result;
	}

	private void move(float[] vector) {
		for (int i = 0; i < points.length; i++) {
			float[] point = points[i];
			point[0] += vector[0];
			point[1] += vector[1];
		}
	}

	public static Line[] clone(Line[] lines) {
		Line[] result = new Line[lines.length];
		for (int i = 0; i < result.length; i++)
			result[i] = lines[i].clone();
		return result;
	}

	public Line clone() {
		return new Line(points[0].clone(), points[1].clone());
	}

	public boolean containsPoint(float[] point) {

		if (vector[0] == 0 && vector[1] == 0)
			return point[0] == points[0][0] && point[1] == points[0][1];

		if (vector[1] == 0) {
			float lambda = (point[0] - points[0][0]) / vector[0];
			return lambda >= 0 && lambda <= 1;
		}

		if (vector[0] == 0) {
			float lambda = (point[1] - points[0][1]) / vector[1];
			return lambda >= 0 && lambda <= 1;
		}

		float lambda1 = 0;
		float lambda2 = 0;

		lambda1 = (point[0] - points[0][0]) / vector[0];
		lambda2 = (point[1] - points[0][1]) / vector[1];

		return lambda1 >= 0 && lambda1 <= 1 && lambda2 >= 0 && lambda2 <= 1 && Math.abs(lambda1 - lambda2) <= 0.01;
	}

	@Override
	public JSONObject toJSON() {

		JSONObject json = new JSONObject();
		String POINTS = "Points";
		String VECTOR = "Vector";

		try {
			json.put(POINTS, JSONUtilities.Floats2DToJSON(points));
			json.put(VECTOR, JSONUtilities.FloatsToJSON(vector));
		} catch (JSONException e) {
			Logger.debug(getClass(), COULD_NOT_JSONIZE);
		}
		return json;
	}

	@Override
	public void recreateFromJSON(JSONObject object) {
		String POINTS = "Points";
		String VECTOR = "Vector";

		try {
			points = JSONUtilities.JSONToFloats2D(object.getJSONArray(POINTS));
			vector = JSONUtilities.JSONToFloats(object.getJSONArray(VECTOR));
		} catch (JSONException e) {
			Logger.debug(getClass(), COULD_NOT_JSONIZE);
		}
	}

}
