package com.updatecontrols.correspondence.memento;



public class PredecessorMemento {

	private RoleMemento role;
	private FactID id;
	private boolean pivot;
	
	public PredecessorMemento(RoleMemento role, FactID id, boolean pivot) {
		this.role = role;
		this.id = id;
		this.pivot = pivot;
	}
	
	public RoleMemento getRole() {
		return role;
	}
	
	public FactID getId() {
		return id;
	}

	public boolean isPivot() {
		return pivot;
	}

	@Override
	public int hashCode() {
		return ((id == null) ? 1 : id.hashCode()) * ((role == null) ? 1 : role.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PredecessorMemento other = (PredecessorMemento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append(role).append(", ")
			.append(id).toString();
	}
}
