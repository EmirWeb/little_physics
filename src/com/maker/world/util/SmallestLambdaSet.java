package com.maker.world.util;

import com.maker.utilities.linkedlist.LinkedList;

public class SmallestLambdaSet<T> {
	private LinkedList<T> generics = new LinkedList<T>();
	private float lambda = Float.MAX_VALUE;	

	public void add(float lambda, T c) {
		if (c == null || lambda > this.lambda || Float.MAX_VALUE == lambda)
			return;
		else if(Math.abs(lambda - this.lambda) < 0.0001 ){
			generics.add(c);
			return;
		}
		
		this.lambda = lambda;
		
		generics = new LinkedList<T>();
		generics.add(c);
	}
	
	public void add(float lambda, LinkedList<T> generics) {
		if (generics == null || lambda > this.lambda)
			return;
		else if(Math.abs(lambda - this.lambda) < 0.0001){
			for(T generic: generics)
				this.generics.add(generic);
			return;
		}
		
		this.lambda = lambda;
		
		//TODO: copy objects inside
		this.generics = generics;
	}
	
	public LinkedList<T> getSet(){
		return generics;
	}
	
	public float getLambda(){
		return lambda;
	}

	public boolean isEmpty() {
		return generics.isEmpty();
	}
}
