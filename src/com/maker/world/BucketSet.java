package com.maker.world;

import java.io.Serializable;

import com.maker.utilities.linkedlist.LinkedList;
import com.maker.utilities.linkedlist.Node;

public class BucketSet implements Serializable{

	private LinkedList<AugmentedNode> bucket = new LinkedList<AugmentedNode>();

	public void add(AugmentedNode augmentedNode) {
		bucket.add(augmentedNode);
	}

	public AugmentedNode remove(WorldObject worldObject) {

		Node<AugmentedNode> current = bucket.getRoot();

		while (current != null) {
			AugmentedNode node = current.get();
			if (node.getWorldObject() == worldObject) {
				bucket.remove(current);
				return node;
			}
			current = current.getNext();
		}
		return null;

	}

	public AugmentedNode update(AugmentedNode augmentedNode) {
		Node<AugmentedNode> current = bucket.getRoot();

		while (current != null) {
			AugmentedNode node = current.get();
			if (node.equals(augmentedNode)) {
				current.set(augmentedNode);
				return node;
			}
			current = current.getNext();
		}
		return null;

	}

}
