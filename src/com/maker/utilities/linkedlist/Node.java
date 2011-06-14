package com.maker.utilities.linkedlist;

public class Node<T> {
	private T generic;
	private Node<T> next;
	private Node<T> previous;

	public Node(T generic) {
		this.generic = generic;
	}

	public boolean hasNext() {
		return next != null;
	}

	public Node<T> getNext() {
		return next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public T get() {
		return generic;
	}

	public void set(T generic) {
		this.generic = generic;
	}

	public void setPrevious(Node<T> previous) {
		this.previous = previous;
	}

	public Node<T> getPrevious() {
		return previous;
	}
}
