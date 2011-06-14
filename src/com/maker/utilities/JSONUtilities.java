package com.maker.utilities;

import org.json.JSONArray;
import org.json.JSONException;

import com.maker.geometry.Line;

public class JSONUtilities {
	public static JSONArray LinesToJSON(Line[] lines) {
		JSONArray json = new JSONArray();
		if (lines == null)
			return json;

		for (Line line : lines)
			json.put(line.toJSON());
		return json;
	}

	public static JSONArray FloatsToJSON(float[] floats) {
		JSONArray json = new JSONArray();
		if (floats == null)
			return json;

		try {
			for (float f : floats)
				json.put(f);
		} catch (JSONException e) {
		}
		return json;
	}

	public static JSONArray IntegersToJSON(int[] ints) {
		JSONArray coords = new JSONArray();
		if (ints == null)
			return coords;

		for (int i : ints)
			coords.put(i);
		return coords;
	}

	public static JSONArray Floats2DToJSON(float[][] floats) {
		JSONArray json = new JSONArray();
		if (floats == null)
			return json;

		for (float[] f : floats)
			json.put(FloatsToJSON(f));
		return json;
	}

	public static float[] JSONToFloats(JSONArray jsonArray) {
		float[] floats = new float[jsonArray.length()];
		try {
			for (int i = 0; i < jsonArray.length(); i++)
				floats[i] = (float) jsonArray.getDouble(i);
		} catch (JSONException e) {
		}

		return floats;
	}

	public static int[] JSONToIntegers(JSONArray jsonArray) {
		int[] ints = new int[jsonArray.length()];
		try {
			for (int i = 0; i < jsonArray.length(); i++)
				ints[i] = jsonArray.getInt(i);
		} catch (JSONException e) {
		}

		return ints;
	}

	public static Line[] JSONToLines(JSONArray jsonArray) {
		Line[] lines = new Line[jsonArray.length()];
		try {
			for (int i = 0; i < jsonArray.length(); i++){
				Line line = new Line(jsonArray.getJSONObject(i));
				lines[i] = line;
			}
				
		} catch (JSONException e) {
		}

		return lines;
	}

	public static float[][] JSONToFloats2D(JSONArray jsonArray) {
		float[][] floats = new float[jsonArray.length()][];
		try {
			for (int i = 0; i < jsonArray.length(); i++)
				floats[i] = JSONToFloats(jsonArray.getJSONArray(i));
		} catch (JSONException e) {
		}

		return floats;
	}

}
