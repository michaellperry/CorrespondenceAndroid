package com.updatecontrols.correspondence.query;

import java.util.List;

import com.updatecontrols.correspondence.memento.RoleMemento;

public class Join {

	private boolean successor;
	private RoleMemento role;
	private List<Condition> conditionCollection;
	
	public Join(boolean successor, RoleMemento roleName) {
		this(successor, roleName, null);
	}

	public Join(boolean successor, RoleMemento role, List<Condition> conditionCollection) {
		super();
		this.successor = successor;
		this.role = role;
		this.conditionCollection = conditionCollection;
	}

	public boolean isSuccessor() {
		return successor;
	}

	public RoleMemento getRole() {
		return role;
	}

	public List<Condition> getConditions() {
		return conditionCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (conditionCollection != null) {
			for (Condition condition : conditionCollection) {
				result = prime * result + condition.hashCode();
			}
		}
		result = prime * result + role.hashCode();
		result = prime * result + (successor ? 1231 : 1237);
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
		Join other = (Join) obj;
		if (conditionCollection == null) {
			if (other.conditionCollection != null)
				return false;
		}
		else {
			for (int conditionIndex = 0; conditionIndex < conditionCollection.size(); ++conditionIndex) {
				if (!conditionCollection.get(conditionIndex).equals(other.conditionCollection.get(conditionIndex))) {
					return false;
				}
			}
		}
		if (!role.equals(other.role))
			return false;
		if (successor != other.successor)
			return false;
		return true;
	}
}
