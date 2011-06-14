package com.maker.world;

import java.io.Serializable;

import com.maker.utilities.linkedlist.LinkedList;

public class AugmentedNode implements Serializable{

	private WorldObject worldObject;
	private LinkedList<Integer> hashValues = new LinkedList<Integer>();
	
	public AugmentedNode(WorldObject worldObject) {
		this.worldObject = worldObject;
	}

	public void addHashValue(int hashValue) {
		hashValues.add(hashValue);
	}
	
	public LinkedList<Integer> getHashValues() {
		return hashValues;
	}

	public WorldObject getWorldObject() {
		return worldObject;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AugmentedNode))
			return false;

		if (this == o)
			return true;

		AugmentedNode augmentedNode = (AugmentedNode) o;
		
		return worldObject == augmentedNode.getWorldObject();
	}

}
