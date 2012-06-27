package com.updatecontrols.correspondence.memento;

public class FactID implements Comparable<FactID> {
	public static final FactID Null = new FactID();
	
	private long key;
	
	// Create an empty ID, which represents a null.
	public FactID() {
		this.key = 0L;
	}
	
	// Create an ID that represents an object.
	public FactID(long key) {
		this.key = key;
	}

	public long getKey() {
		return key;
	}

	// Compare two IDs. The result is negative if this is less,
	// positive if this is greater, and zero if they are equal.
	public int compareTo(FactID o) {
		if (key < o.key)
			return -1;
		else if (key > o.key)
			return 1;
		else
			return 0;
	}

	@Override
	public int hashCode() {
		return (int) (key ^ (key >>> 32));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FactID other = (FactID) obj;
		if (key != other.key)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Long.toHexString(key);
	}
}
