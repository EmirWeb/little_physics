package com.maker.utilities;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.PriorityQueue;

public class OrderedSet<T> {
	private Hashtable<Float, HashSet<T>> collisions = new Hashtable<Float, HashSet<T>>();
	private PriorityQueue<Float> queue = new PriorityQueue<Float>(16, new Comparator<Float>() {
		@Override
		public int compare(Float object1, Float object2) {
			return Float.compare(object1, object2);
		}
	});

	public void add(float lambda, T c) {
		if (c == null)
			return;
		if (collisions.containsKey(lambda)) 
			collisions.get(lambda).add(c);
		else {
			HashSet<T> collision = new HashSet<T>();
			collision.add(c);
			collisions.put(lambda, collision);
			queue.add(lambda);
		}
	}
	
	public HashSet<T> getSmallestValue(){
		if (queue.isEmpty())
			return new HashSet<T>();
		
		return collisions.get(queue.peek());
	}
	
	public HashSet<T> getValue(float lambda){
		if (collisions.containsKey(lambda))
			return collisions.get(lambda);
		return new HashSet<T>();
	}

	public float getSmallestKey() {
		if (queue.isEmpty())
			return -1;
		
		return queue.peek();
	}

	public boolean isEmpty() {
		return collisions.isEmpty();
	}
}
