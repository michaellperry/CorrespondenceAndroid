package com.updatecontrols.correspondence.query;

import com.updatecontrols.correspondence.memento.RoleMemento;

public class Unpublisher {

    private QueryDefinition messageFacts;
    private Condition publishCondition;
    private RoleMemento role;
    
	public Unpublisher(QueryDefinition messageFacts,
			Condition publishCondition, RoleMemento role) {
		super();
		this.messageFacts = messageFacts;
		this.publishCondition = publishCondition;
		this.role = role;
	}

	public QueryDefinition getMessageFacts() {
		return messageFacts;
	}

	public Condition getPublishCondition() {
		return publishCondition;
	}

	public RoleMemento getRole() {
		return role;
	}
}
