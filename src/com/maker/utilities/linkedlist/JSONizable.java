package com.maker.utilities.linkedlist;

import org.json.JSONObject;

public interface JSONizable {
	public JSONObject toJSON();
	public void recreateFromJSON(JSONObject object);
}
