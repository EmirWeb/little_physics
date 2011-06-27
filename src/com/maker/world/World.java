package com.maker.world;

import java.lang.reflect.Constructor;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.maker.Logger;
import com.maker.Listeners.WinningListener;
import com.maker.geometry.Line;
import com.maker.utilities.Algebra;
import com.maker.utilities.linkedlist.JSONizable;
import com.maker.utilities.linkedlist.LinkedList;
import com.maker.world.util.SmallestLambdaSet;

public class World implements JSONizable {
	private LinkedList<Mobile> mobileObjects = new LinkedList<Mobile>();
	private LinkedList<Terrain> terrainObjects = new LinkedList<Terrain>();
	private LinkedList<WorldObject> objects = new LinkedList<WorldObject>();
	private GridHash grid = new GridHash();
	private static int stackCounter = 0;
	private WinningListener listener;

	private long time = -1;
	private float[] gravity = new float[] { 0f, -9.8f };
	private int[] highlight = new int[] { 0, 0 };
	private boolean hasHighLight;
	private Movable[] touched = new Movable[2];
	private boolean debuggable = false;
	private long DEBUGGABL_TIME_CHANGE = 10;

	public World(WinningListener listener) {
		this.listener = listener;
	}

	public World() {
	}

	public void add(WorldObject object) {
		removeObject(object.getPosition());

		if (object instanceof Eraser)
			return;

		if (object instanceof Mobile) {
			Mobile mobile = (Mobile) object;
			mobileObjects.add(mobile);
			addToGrid(mobile);
		} else if (object instanceof Terrain) {
			Terrain terrain = (Terrain) object;
			addToGrid(terrain);
		}

		objects.add(object);
	}

	public void removeObject(float[] position) {
		WorldObject removable = getObjectInt(position);

		if (removable != null)
			removeObject(removable);
	}

	private void removeObject(WorldObject removable) {
		objects.remove(removable);

		if (removable instanceof Mobile)
			mobileObjects.remove((Mobile) removable);
		else if (removable instanceof Terrain)
			removeFromGrid((Terrain) removable);
	}

	public WorldObject getObjectInt(float[] newPosition) {
		for (WorldObject o : objects) {
			float[] existingPosition = o.getPosition();
			if (Math.abs(existingPosition[0] - newPosition[0]) < 0.01f && Math.abs(existingPosition[1] - newPosition[1]) < 0.01f)
				return o;
		}
		return null;

	}

	public WorldObject getObject(float[] newPosition) {
		for (WorldObject o : objects) {
			float[] existingPosition = o.getPosition();
			if (Math.abs(existingPosition[0] - newPosition[0]) < 1.01f && Math.abs(existingPosition[1] - newPosition[1]) < 1.01f)
				return o;
		}
		return null;
	}

	private void addToGrid(Terrain terrain) {

		Terrain overlappingTerrain = getOverlappingterrain(terrain);
		if (overlappingTerrain != null)
			removeFromGrid(overlappingTerrain);

		addToGrid((WorldObject) terrain);

		terrainObjects.add(terrain);
	}

	private void addToGrid(WorldObject worldObject, float[] boundingBox) {
		grid.add(worldObject, boundingBox);
	}

	private void addToGrid(WorldObject worldObject) {
		float[] boundingbox = worldObject.getBoundingBox();
		addToGrid(worldObject, boundingbox);
	}

	private Terrain getOverlappingterrain(Terrain terrain) {
		for (Terrain t : terrainObjects) {
			int[] coordinates = t.getCoords();
			int x = coordinates[0];
			int y = coordinates[1];

			int[] terrainCoordinates = terrain.getCoords();
			int terrainX = terrainCoordinates[0];
			int terrainY = terrainCoordinates[1];

			if (x == terrainX && y == terrainY)
				return t;
		}
		return null;
	}

	private void removeFromGrid(Terrain terrain) {
		removeFromGrid((WorldObject) terrain);
		terrainObjects.remove(terrain);
	}

	private void removeFromGrid(WorldObject worldObject) {
		grid.remove(worldObject);
	}

	public LinkedList<WorldObject> getCurrentFrame(boolean animate) {
		if (!animate) {
			time = -1;
			return objects;
		}

		if (time == -1)
			time = System.currentTimeMillis();
		
		long currentTime = System.currentTimeMillis();
		long timeChange = (currentTime - time) / 10;

		if (debuggable)
			timeChange = DEBUGGABL_TIME_CHANGE;

		time = currentTime;

		for (Mobile mobile : mobileObjects) {
			float[] directionVector = mobile.getDirectionVector(timeChange);			
			checkCollision(mobile, directionVector, gravity);
			addToGrid(mobile);
		}

		return objects;
	}

	private void addToGrid(Mobile mobile) {
		WorldObject worldObject = (WorldObject) mobile;
		float[] boundingBox = worldObject.getBoundingBox();
		addToGrid((WorldObject) mobile, boundingBox);
	}

	private void checkCollision(Mobile object, float[] directionVector, float[] gravity) {
		stackCounter++;
		if (stackCounter > 2 && directionVector[0] < 0.01 && directionVector[1] < 0.01) {
			debug("Too Much");
			stackCounter--;
			return;
		} else if (stackCounter > 2) {
			debug("Too Much");
			stackCounter--;
			return;
		}

		float[] boundingBox = object.getBoundingBox(directionVector);
		int left = (int) boundingBox[0];
		int right = (int) Math.ceil(boundingBox[1]);
		int top = (int) boundingBox[2];
		int bottom = (int) Math.ceil(boundingBox[3]);

		if (Math.abs(left - right) > 9) {
			debug("yo");
		}
		boundingBox = object.getBoundingBox(directionVector);
		SmallestLambdaSet<LinkedList<Collision>> collisions = new SmallestLambdaSet<LinkedList<Collision>>();

		int comparisons = 0;
		int radiusComparisons = 0;

		for (int x = left - 1; x <= right; x++) {
			for (int y = top - 1; y <= bottom; y++) {

				LinkedList<WorldObject> objectList = grid.get(x, y);
				if (objectList != null) {
					for (WorldObject worldObject : objectList) {
						if (worldObject != object) {
							radiusComparisons++;
							if (withinRadius(worldObject, (WorldObject) object, directionVector)) {
								SmallestLambdaSet<Collision> c = getCollisionLambda(object, worldObject, directionVector);
								float l = c.getLambda();
								if (l != -1)
									collisions.add(l, c.getSet());
								comparisons++;
							}
						}
					}
				}
			}
		}
		debug("Comparisons: " + comparisons + " radiusComparisons: " + radiusComparisons + " left-Right: " + (left - right) + " top-bottom: " + (top - bottom) + " area: "
				+ ((left - right) * (top - bottom)));

		if (!collisions.isEmpty()) {
			HashSet<Collision> accountedCollisions = new HashSet<Collision>();

			for (LinkedList<Collision> h : collisions.getSet()) {
				if (h.size() == 1)
					for (Collision c : h) {
						accountedCollisions.add(c);
						manageCollision(c, (WorldObject) object);
					}
				else {
					HashSet<float[]> points = new HashSet<float[]>();
					for (Collision c : h) {
						points.add(c.getIntersectionPoint());
						manageCollision(c, (WorldObject) object);
					}
					Collision considerCollision = null;
					int pointCount = 0;
					for (Collision c : h) {
						Line line = c.getLine();
						int p = 0;
						for (float[] point : points)
							if (line.containsPoint(point))
								p++;
						/**
						 * This is oversimplified assumes only one collision
						 * line per object, when in fact there could be more
						 * than one
						 */
						if (p > pointCount) {
							pointCount = p;
							considerCollision = c;
						}
					}

					if (considerCollision != null) {
						boolean collisionFound = false;
						for (Collision c : accountedCollisions) {
							float[] v1 = Algebra.normalize(c.getLine().getVector());
							float[] v2 = Algebra.normalize(considerCollision.getLine().getVector());

							if (Math.abs(v1[0] - v2[0]) <= 0.01 && Math.abs(v1[1] - v2[1]) <= 0.01) {
								collisionFound = true;
								break;
							}
						}
						if (!collisionFound)
							accountedCollisions.add(considerCollision);
					}
				}
			}

			HashSet<float[]> reflectionVectors = new HashSet<float[]>();
			int i = 0;
			for (Collision c : accountedCollisions) {
				float[] reflectionVector = null;

				if (++i == accountedCollisions.size()) {
					reflectionVector = object.setCollision(c);
				} else {
					reflectionVector = object.getCollision(c);
				}

				if (reflectionVector != null) {
					reflectionVectors.add(reflectionVector);
					debug("x: " + reflectionVector[0] + " y: " + reflectionVector[1]);
				}
			}

			float[] reflectionVector = new float[] { 0, 0 };
			float magnitude = 0;
			for (float[] vector : reflectionVectors) {
				if (magnitude == 0)
					magnitude = Algebra.getMagnitude(vector);
				reflectionVector = Algebra.add(reflectionVector, vector);
			}

			reflectionVector = Algebra.multiplyVectorByConstant(reflectionVector, magnitude);
			checkCollision(object, reflectionVector, gravity);
		} else
			object.move(directionVector);
		stackCounter--;
	}

	private boolean withinRadius(WorldObject o1, WorldObject mobile, float[] directionVector) {
		float[] center1 = o1.getCenter();
		float[] center2 = mobile.getCenter();
		float[] center3 = Algebra.add(center2, directionVector);

		float radius1 = o1.getRadius();
		float radius2 = mobile.getRadius();

		float square1 = center1[0] - center2[0];
		float square2 = center1[1] - center2[1];

		float square3 = center1[0] - center3[0];
		float square4 = center1[1] - center3[1];

		float distanceWithoutVector = (float) Math.sqrt(square1 * square1 + square2 * square2);
		float distanceWithVector = (float) Math.sqrt(square3 * square3 + square4 * square4);
		float radius = radius1 + radius2;

		return radius > distanceWithoutVector && radius > distanceWithVector;
	}

	private void manageCollision(Collision c, WorldObject w) {
		WorldObject t = c.getObject();
		if (t instanceof Terrain && ((Terrain) t).isBreakable())
			removeObject((WorldObject) t);
		if (t instanceof Terrain && ((Terrain) t).isWinning())
			win(w);
	}

	private void win(WorldObject w) {
		if (listener != null) {
			listener.win(w);
			listener = null;
		}
	}

	private static SmallestLambdaSet<Collision> getCollisionLambda(Mobile object, WorldObject worldObject, float[] directionVector) {
		Line[] currentLines = object.getLines();
		Line[] terrainLines = worldObject.getLines();

		SmallestLambdaSet<Collision> collisions = new SmallestLambdaSet<Collision>();

		for (int i = 0; i < currentLines.length; i++) {
			for (int j = 0; j < terrainLines.length; j++) {
				Line terrainLine = terrainLines[j];
				{
					Line l1 = new Line(currentLines[i].getPoints()[0], Algebra.add(currentLines[i].getPoints()[0], directionVector));
					float currentLambda = getLambda(l1, terrainLine);
					float[] points = l1.getPoints()[0];
					float[] intersectionPoint = new float[] { points[0] + currentLambda * l1.getVector()[0], points[1] + currentLambda * l1.getVector()[1] };

					collisions.add(currentLambda, getCollision(currentLambda, terrainLine, worldObject, directionVector, intersectionPoint));
				}
				{
					Line l2 = new Line(currentLines[i].getPoints()[1], Algebra.add(currentLines[i].getPoints()[1], directionVector));
					float currentLambda = getLambda(l2, terrainLine);
					float[] points = l2.getPoints()[0];
					float[] intersectionPoint = new float[] { points[0] + currentLambda * l2.getVector()[0], points[1] + currentLambda * l2.getVector()[1] };

					collisions.add(currentLambda, getCollision(currentLambda, terrainLine, worldObject, directionVector, intersectionPoint));
				}
				{
					Line l3 = new Line(terrainLine.getPoints()[0], Algebra.subtract(terrainLine.getPoints()[0], directionVector));
					float currentLambda = getLambda(l3, currentLines[i]);
					float[] points = l3.getPoints()[0];
					float[] intersectionPoint = new float[] { points[0] + currentLambda * l3.getVector()[0], points[1] + currentLambda * l3.getVector()[1] };

					collisions.add(currentLambda, getCollision(currentLambda, terrainLine, worldObject, directionVector, intersectionPoint));
				}
				{
					Line l4 = new Line(terrainLine.getPoints()[1], Algebra.subtract(terrainLine.getPoints()[1], directionVector));
					float currentLambda = getLambda(l4, currentLines[i]);
					float[] points = l4.getPoints()[0];
					float[] intersectionPoint = new float[] { points[0] + currentLambda * l4.getVector()[0], points[1] + currentLambda * l4.getVector()[1] };

					collisions.add(currentLambda, getCollision(currentLambda, terrainLine, worldObject, directionVector, intersectionPoint));
				}
			}
		}
		return collisions;
	}

	private static Collision getCollision(float lambda, Line line, WorldObject worldObject, float[] directionVector, float[] intersectionPoint) {
		if (lambda != -1 && lambda > 0 && lambda <= 1)
			return new Collision(lambda, line, worldObject, directionVector, intersectionPoint);

		return null;
	}

	private static float getLambda(Line l1, Line l2) {
		float[] p1 = l1.getPoints()[0];
		float[] p2 = l2.getPoints()[0];

		float x1 = l1.getVector()[0];
		float y1 = l1.getVector()[1];

		float x2 = l2.getVector()[0];
		float y2 = l2.getVector()[1];

		float c = x2 * y1 - x1 * y2;

		if (c == 0) // parallel
			return -1;

		float a = p2[0] - p1[0];
		float b = p2[1] - p1[1];

		if (x1 == 0) {
			if (y1 == 0) // not moving
				return -1;

			/**
			 * case already covered if (x2 == 0) // parallel return -1;
			 */
			float lambda = -a / x2;

			if (lambda < 0 || lambda > 1)
				return -1;

			return (b - a * y2) / y1;
		}

		float lambda = (b * x1 - a * y1) / c;

		if (lambda < 0 || lambda > 1)
			return -1;

		return (a + x2 * lambda) / x1;
	}

	public float[] getGravity() {
		return gravity;
	}

	public void sethighlight(float x, float y) {
		highlight[0] = (int) x;
		highlight[1] = (int) y;
	}

	public int[] getHighlight() {
		return highlight;
	}

	public boolean hasHighlight() {
		return hasHighLight;
	}

	public void hasHighlight(boolean highlight2) {
		hasHighLight = highlight2;
	}

	public void reset() {
		objects = new LinkedList<WorldObject>();
		mobileObjects = new LinkedList<Mobile>();
		grid = new GridHash();
	}

	public HashSet<Movable> getTouched() {
		return null;
	}

	public void setTouch(int i, float[] worldTouch) {
		if (worldTouch == null) {
			touched[i] = null;
			return;
		}

		if (touched[i] != null) {
			Movable movable = touched[i];
			movable.setTouch(worldTouch);
		} else {
			WorldObject object = getObject(new float[] { worldTouch[0], worldTouch[1] });

			if (object == null || !(object instanceof Movable))
				return;

			Movable movable = (Movable) object;

			if (touched[0] == movable || touched[1] == movable)
				return;

			movable.setTouch(worldTouch);
			touched[i] = movable;
		}
	}

	public void setWinningListener(WinningListener winningListener) {
		listener = winningListener;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		try {
			JSONArray worldObjects = new JSONArray();
			for (WorldObject object : objects) {
				JSONizable jsonizeable = (JSONizable) object;
				JSONObject j = jsonizeable.toJSON();
				j.put("Class", object.getClass().getName());
				worldObjects.put(j);
			}

			json.put("worldObjects", worldObjects);
		} catch (JSONException e) {
		}
		return json;
	}

	@Override
	public void recreateFromJSON(JSONObject object) {
		try {
			JSONArray worldObjects = object.getJSONArray("worldObjects");
			for (int i = 0; i < worldObjects.length(); i++) {
				JSONObject json = (JSONObject) worldObjects.get(i);
				String c = json.getString("Class");

				WorldObject worldObject = recreateWorlObject(c, json);
				if (worldObject != null)
					add(worldObject);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private WorldObject recreateWorlObject(String name, JSONObject json) {
		WorldObject worldObject = null;
		try {
			Class cl = Class.forName(name);
			Constructor co = cl.getConstructor();
			Object o = co.newInstance();
			JSONizable jsonObject = (JSONizable) o;
			jsonObject.recreateFromJSON(json);
			worldObject = (WorldObject) jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return worldObject;
	}

	private void debug(String message) {
		if (debuggable)
			Logger.debug(getClass(), message);
	}

	private float getRatio(float timeChange) {
		float ratio = ((float) timeChange) / ((float) 10000);
		return ratio;
	}
}
