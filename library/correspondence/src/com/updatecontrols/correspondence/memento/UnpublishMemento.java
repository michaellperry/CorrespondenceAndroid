package com.updatecontrols.correspondence.memento;

public class UnpublishMemento {

	private FactID messageId;
	private RoleMemento role;
	
	public UnpublishMemento(FactID messageId, RoleMemento role) {
		super();
		this.messageId = messageId;
		this.role = role;
	}

	public FactID getMessageId() {
		return messageId;
	}

	public RoleMemento getRole() {
		return role;
	}
}
