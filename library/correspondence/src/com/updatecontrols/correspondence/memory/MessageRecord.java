package com.updatecontrols.correspondence.memory;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.MessageMemento;
import com.updatecontrols.correspondence.memento.RoleMemento;

public class MessageRecord {

	private MessageMemento message;
	private FactID ancestorFact;
	private RoleMemento ancestorRole;
	
	public MessageRecord(MessageMemento message, FactID ancestorFact, RoleMemento ancestorRole) {
		super();
		this.message = message;
		this.ancestorFact = ancestorFact;
		this.ancestorRole = ancestorRole;
	}

	public MessageMemento getMessage() {
		return message;
	}

	public FactID getAncestorFact() {
		return ancestorFact;
	}

	public RoleMemento getAncestorRole() {
		return ancestorRole;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ancestorFact == null) ? 0 : ancestorFact.hashCode());
		result = prime * result
				+ ((ancestorRole == null) ? 0 : ancestorRole.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		MessageRecord other = (MessageRecord) obj;
		if (ancestorFact == null) {
			if (other.ancestorFact != null)
				return false;
		} else if (!ancestorFact.equals(other.ancestorFact))
			return false;
		if (ancestorRole == null) {
			if (other.ancestorRole != null)
				return false;
		} else if (!ancestorRole.equals(other.ancestorRole))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}
}
