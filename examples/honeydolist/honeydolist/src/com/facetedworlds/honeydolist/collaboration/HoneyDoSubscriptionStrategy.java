package com.facetedworlds.honeydolist.collaboration;

import java.util.ArrayList;

import android.app.Activity;

import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.strategy.SubscriptionStrategy;

import facetedworlds.honeydo.model.Identity;
import facetedworlds.honeydo.model.List;
import facetedworlds.honeydo.model.ListContents;

public class HoneyDoSubscriptionStrategy implements SubscriptionStrategy {

	private Identity identity;
	
	private Dependent depListContents;
	
	public HoneyDoSubscriptionStrategy(Identity identity, final Activity context) {
		this.identity = identity;
		
		depListContents = new Dependent(new UpdateMethod() {
			@Override
			public void update() {
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateListContents();
					}
				});
			}
		});
		depListContents.addInalidatedListener(new InvalidatedListener() {
			@Override
			public void invalidated() {
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						depListContents.onGet();
					}
				});
			}
		});
		depListContents.onGet();
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
