package com.updatecontrols.correspondence.query;

public class QueryInvalidator {

	private QueryDefinition targetFacts;
	private QueryDefinition invalidQuery;
	
	public QueryInvalidator(QueryDefinition targetFacts, QueryDefinition invalidQuery) {
		super();
		this.targetFacts = targetFacts;
		this.invalidQuery = invalidQuery;
	}

	public QueryDefinition getTargetFacts() {
		return targetFacts;
	}

	public QueryDefinition getInvalidQuery() {
		return invalidQuery;
	}
}
