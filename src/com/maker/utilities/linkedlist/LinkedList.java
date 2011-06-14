package com.maker.utilities.linkedlist;

import java.util.Iterator;

import com.maker.Logger;

public class LinkedList<T> implements Iterable<T> {
	private Node<T> root;
	private int size;

	public void add(T generic) {
		for(T t: this)
			if (equals(t, generic))
				return;

		add(new Node<T>(generic));
	}

	private void add(Node<T> node) {
		node.setNext(root);
		if (root != null)
			root.setPrevious(node);
		root = node;
		size++;
	}

	public void remove(T generic) {
		Node<T> current = root;
		while (current != null) {
			T genericObject = current.get();
			if (equals(genericObject, generic)) {
				remove(current);
				return;
			}
			current = current.getNext();
		}
	}

	public void remove(Node<T> node) {
		if (node == null)
			return;

		Node<T> previous = node.getPrevious();
		Node<T> next = node.getNext();

		if (previous != null)
			previous.setNext(next);

		if (next != null)
			next.setPrevious(previous);

		if (node == root)
			root = next;

		size--;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public Node<T> getRoot() {
		return root;
	}

	public boolean equals(T t1, T t2) {
		return t1 == t2 || t1 != null && t1.equals(t2);
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			Node<T> current = root;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public T next() {
				if (current == null)
					return null;

				T next = current.get();
				current = current.getNext();
				return next;
			}

			@Override
			public void remove() {
				LinkedList.this.remove(current);
				current = current.getNext();
			}
		};
	}
	
	public int size(){
		return size;
	}
	
	private void debug(String string) {
		Logger.debug(getClass(), string);
	}
}
