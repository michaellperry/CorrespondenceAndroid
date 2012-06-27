package com.updatecontrols.correspondence.query;

public class Condition {

	private boolean isEmpty;
	private QueryDefinition subQuery;
	
	public Condition(boolean isEmpty, QueryDefinition subQuery) {
		super();
		this.isEmpty = isEmpty;
		this.subQuery = subQuery;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public QueryDefinition getSubQuery() {
		return subQuery;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isEmpty ? 1231 : 1237);
		result = prime * result
				+ ((subQuery == null) ? 0 : subQuery.hashCode());
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
		Condition other = (Condition) obj;
		if (isEmpty != other.isEmpty)
			return false;
		if (subQuery == null) {
			if (other.subQuery != null)
				return false;
		} else if (!subQuery.equals(other.subQuery))
			return false;
		return true;
	}
}
