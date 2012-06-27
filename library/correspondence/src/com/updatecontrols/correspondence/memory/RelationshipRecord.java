package com.updatecontrols.correspondence.memory;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.RoleMemento;

public class RelationshipRecord {
	
	private FactID factID;
	private RoleMemento role;
	private FactID predecessorID;
	private boolean isPivot;
	
	public RelationshipRecord(FactID factID, RoleMemento role, FactID predecessorID, boolean isPivot) {
		this.factID = factID;
		this.role = role;
		this.predecessorID = predecessorID;
		this.isPivot = isPivot;
	}

	public FactID getObjectID() {
		return factID;
	}
	
	public RoleMemento getRole() {
		return role;
	}
	
	public FactID getPredecessorID() {
		return predecessorID;
	}

	public boolean isPivot() {
		return isPivot;
	}
	
}
