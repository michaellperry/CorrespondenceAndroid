package com.updatecontrols.correspondence.query;

import java.util.ArrayList;
import java.util.List;

public class ConditionCollection {

	private ArrayList<Condition> conditions = new ArrayList<Condition>();

	public List<Condition> getConditions() {
		return conditions;
	}
	
	public ConditionCollection isEmpty(QueryDefinition subQuery) {
		conditions.add(new Condition(true, subQuery));
		return this;
	}
	
	public ConditionCollection isNotEmpty(QueryDefinition subQuery) {
		conditions.add(new Condition(false, subQuery));
		return this;
	}
	
	public ConditionCollection addAll( Condition[] c ) {
		for( Condition nextCondition : c ) {
			conditions.add( nextCondition);
		}
		
		return this;
	}
}
