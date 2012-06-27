package com.updatecontrols.correspondence;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterable<T> implements Iterable<T> {
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			public boolean hasNext() {
				return false;
			}
			public T next() {
				throw new NoSuchElementException();
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}