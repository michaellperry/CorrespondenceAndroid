package com.facetedworlds.honeydolist.collaboration;

import java.util.ArrayList;

import com.facetedworlds.honeydolist.model.Identity;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.strategy.SubscriptionStrategy;

public class HoneyDoSubscriptionStrategy implements SubscriptionStrategy {

	private Identity identity;
	
	public HoneyDoSubscriptionStrategy(Identity identity) {
		this.identity = identity;
	}

	@Override
	public Iterable<CorrespondenceFact> getSubscriptions() {
		ArrayList<CorrespondenceFact> subscriptions = new ArrayList<CorrespondenceFact>();
		subscriptions.add(identity);
		subscriptions.addAll(identity.sharedLists());
		return subscriptions;
	}

}
