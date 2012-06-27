package com.updatecontrols.correspondence;

import com.updatecontrols.correspondence.query.QueryDefinition;

public class InvalidatedQuery {

	private CorrespondenceFact targetFact;
	private QueryDefinition invalidatedQuery;
	
	public InvalidatedQuery(CorrespondenceFact targetFact, QueryDefinition invalidatedQuery) {
		super();
		this.targetFact = targetFact;
		this.invalidatedQuery = invalidatedQuery;
	}

	public void invalidate() {
		targetFact.invalidateQuery(invalidatedQuery);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((invalidatedQuery == null) ? 0 : invalidatedQuery.hashCode());
		result = prime * result
				+ ((targetFact == null) ? 0 : targetFact.hashCode());
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
		InvalidatedQuery other = (InvalidatedQuery) obj;
		if (invalidatedQuery == null) {
			if (other.invalidatedQuery != null)
				return false;
		} else if (!invalidatedQuery.equals(other.invalidatedQuery))
			return false;
		if (targetFact == null) {
			if (other.targetFact != null)
				return false;
		} else if (!targetFact.equals(other.targetFact))
			return false;
		return true;
	}
}
