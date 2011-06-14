package com.maker.world;

import java.io.Serializable;

import com.maker.Logger;
import com.maker.utilities.linkedlist.LinkedList;

public class GridHash implements Serializable {

	private static final int gridSize = 100000;
	private static final int bucketSize = 100;

	private final LinkedList<WorldObject>[] gridSet = new LinkedList[gridSize];
	private final BucketSet[] bucketSet = new BucketSet[bucketSize];

	private int bucketCounter;

	private static int hash(int x, int y) {
		int hash = x * 100 + y;
		if (hash < 0)
			hash += 10000;
		hash = hash % gridSize;

		return Math.abs(hash);
	}

	public void add(WorldObject worldObject, float[] boundingBox) {
		int id = worldObject.getId();

		int left = (int) boundingBox[0];
		int right = (int) Math.ceil(boundingBox[1]);
		int top = (int) boundingBox[2];
		int bottom = (int) Math.ceil(boundingBox[3]);

		AugmentedNode augmentedNode = new AugmentedNode(worldObject);

		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				int hashValue = hash(x, y);
				augmentedNode.addHashValue(hashValue);
				addToGrid(hashValue, worldObject);
			}
		}

		if (id == -1) {
			id = getBucket();
			worldObject.setId(id);
			addToBucket(id, augmentedNode);
		} else {
			AugmentedNode removedNode = updateBucket(id, augmentedNode);
			if (removedNode != null) {
				LinkedList<Integer> hashValuesOut = removedNode.getHashValues();
				LinkedList<Integer> hashValuesIn = augmentedNode.getHashValues();

				for (Integer hashValue : hashValuesIn)
					hashValuesOut.remove(hashValue);

				for (Integer hashValue : hashValuesOut)
					removeFromGrid(hashValue, worldObject);
			}
		}
	}

	public void remove(WorldObject worldObject) {
		int id = worldObject.getId();

		AugmentedNode augmentedNode = removeFromBucket(id, worldObject);
		if (augmentedNode == null)
			return;
		LinkedList<Integer> hashValues = augmentedNode.getHashValues();
		for (Integer hashValue : hashValues)
			removeFromGrid(hashValue, worldObject);

	}

	private int getBucket() {
		return (++bucketCounter) % bucketSize;
	}

	public LinkedList<WorldObject> get(int x, int y) {
		return gridSet[hash(x, y)];
	}

	private void removeFromGrid(int hashValue, WorldObject worldObject) {
		if (gridSet[hashValue] == null)
			return;
		gridSet[hashValue].remove(worldObject);
	}

	private void addToBucket(int id, AugmentedNode augmentedNode) {
		if (bucketSet[id] == null)
			bucketSet[id] = new BucketSet();
		bucketSet[id].add(augmentedNode);
	}

	private AugmentedNode updateBucket(int id, AugmentedNode augmentedNode) {
		if (bucketSet[id] == null)
			bucketSet[id] = new BucketSet();

		return bucketSet[id].update(augmentedNode);
	}

	private void debug(String message) {
		Logger.debug(getClass(), message);
	}

	private void addToGrid(int hashValue, WorldObject worldObject) {
		if (gridSet[hashValue] == null)
			gridSet[hashValue] = new LinkedList<WorldObject>();
		gridSet[hashValue].add(worldObject);
	}

	private AugmentedNode removeFromBucket(int id, WorldObject worldObject) {
		if (bucketSet[id] == null)
			return null;

		return bucketSet[id].remove(worldObject);
	}

}
