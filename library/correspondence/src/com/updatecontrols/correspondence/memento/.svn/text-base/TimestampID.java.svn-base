package com.updatecontrols.correspondence.memento;

public class TimestampID {
	public static final FactID Null = new FactID();
	
	private long databaseId;
	private long key;
	
	// Create an empty ID, which represents a null.
	public TimestampID() {
		this.key = 0L;
	}
	
	// Create an ID that represents an object.
	public TimestampID(long databaseId, long key) {
		this.databaseId = databaseId;
		this.key = key;
	}

	public long getDatabaseId() {
		return databaseId;
	}

	public long getKey() {
		return key;
	}

	// Compare two IDs. The result is negative if this is less,
	// positive if this is greater, and zero if they are equal.
	public int compareTo(TimestampID o) {
		if (databaseId < o.databaseId)
			return -1;
		else if (databaseId > o.databaseId)
			return 1;
		else if (key < o.key)
			return -1;
		else if (key > o.key)
			return 1;
		else
			return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (databaseId ^ (databaseId >>> 32));
		result = prime * result + (int) (key ^ (key >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimestampID other = (TimestampID) obj;
		if (databaseId != other.databaseId || key != other.key)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Long.toHexString(databaseId) + ":" + Long.toHexString(key);
	}
}
