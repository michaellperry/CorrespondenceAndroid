package com.facetedworlds.honeydolist.collaboration;

import java.util.ArrayList;

import com.updatecontrols.Dependent;
import com.updatecontrols.android.Update;
import com.updatecontrols.android.UpdateAdapter;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.strategy.SubscriptionStrategy;

import facetedworlds.honeydo.model.Identity;
import facetedworlds.honeydo.model.List;
import facetedworlds.honeydo.model.ListContents;

public class HoneyDoSubscriptionStrategy implements SubscriptionStrategy {

	private Identity identity;
	
	private Dependent depListContents;
	
	public HoneyDoSubscriptionStrategy(Identity identity) {
		this.identity = identity;
		
		depListContents = Update.whenNecessary(new UpdateAdapter() {
			@Override
			public void update() {
				updateListContents();
			}
		});
	}

	@Override
	public Iterable<CorrespondenceFact> getSubscriptions() {
		ArrayList<CorrespondenceFact> subscriptions = new ArrayList<CorrespondenceFact>();
		subscriptions.add(identity);
		subscriptions.addAll(identity.sharedLists());
		subscriptions.addAll(identity.sharedListContents());
		subscriptions.addAll(identity.openTasks());
		return subscriptions;
	}

	private void updateListContents() {
		for(List list : identity.listsNeedingContents()) {
			list.getCommunity().addFact(new ListContents(list));
		}
	}
}
