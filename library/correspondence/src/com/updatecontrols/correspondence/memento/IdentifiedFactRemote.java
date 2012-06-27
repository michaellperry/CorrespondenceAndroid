package com.updatecontrols.correspondence.memento;

public class IdentifiedFactRemote extends IdentifiedFactBase {
	
	private FactID remoteId;

	public IdentifiedFactRemote(FactID id, FactID remoteId) {
		super(id);
		this.remoteId = remoteId;
	}

	public FactID getRemoteId() {
		return remoteId;
	}

}
