package com.updatecontrols.correspondence.memory;

import java.util.Comparator;

public class ForwardComparator<T extends Comparable<? super T>> implements Comparator<T> {
	public int compare(T o1, T o2) {
		return o1.compareTo(o2);
	}

}
