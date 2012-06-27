package com.updatecontrols.correspondence.query;

import java.util.ArrayList;
import java.util.List;

import com.updatecontrols.correspondence.Role;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;

public class QueryDefinition {

	private ArrayList<Join> joins = new ArrayList<Join>();

	public QueryDefinition copy() {
		QueryDefinition copy = new QueryDefinition();
		copy.joins = new ArrayList<Join>();
		for (Join join : joins) {
			copy.joins.add(join);
		}
		return copy;
	}
	
	public QueryDefinition joinSuccessors(Role role) {
		joins.add(new Join(true, role.getMemento()));
		return this;
	}
	
	public QueryDefinition joinSuccessors(Role role, ConditionCollection conditions) {
		joins.add(new Join(true, role.getMemento(), conditions.getConditions()));
		return this;
	}
	
	public QueryDefinition joinPredecessors(Role role) {
		joins.add(new Join(false, role.getMemento()));
		return this;
	}
	
	public QueryDefinition joinPredecessors(Role role, ConditionCollection conditions) {
		joins.add(new Join(false, role.getMemento(), conditions.getConditions()));
		return this;
	}

	public void prependInverse(Join join, List<Condition> conditions) {
		joins.add(0, new Join(!join.isSuccessor(), join.getRole(), conditions));
	}

	public List<Join> getJoins() {
		return joins;
	}

	public boolean canExecuteFromMemento() {
		// You can execute a single unconditional predecessor
		// join without going to the database.
		return
			joins.size() == 1 &&
			!joins.get(0).isSuccessor() &&
			(joins.get(0).getConditions() == null || joins.get(0).getConditions().isEmpty());
	}

	public Iterable<FactID> executeFromMemento(FactMemento memento) {
		return memento.getPredecessorIdsByRole(joins.get(0).getRole());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (Join join : joins)
			result = prime * result + join.hashCode();
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
		QueryDefinition other = (QueryDefinition) obj;
		if (joins.size() != other.joins.size())
			return false;
		for (int joinIndex = 0; joinIndex < joins.size(); ++joinIndex)
			if (!joins.get(joinIndex).equals(other.joins.get(joinIndex)))
				return false;
		return true;
	}
}
