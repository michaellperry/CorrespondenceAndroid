package com.updatecontrols.correspondence.networking;

import java.util.ArrayList;
import java.util.List;

import com.updatecontrols.Dependent;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.strategy.SubscriptionStrategy;

public class Subscription {

    private SubscriptionStrategy subscriptionStrategy;
    private List<CorrespondenceFact> pivots;
    private Dependent depPivots;

    public Subscription(SubscriptionStrategy subscriptionStrategy) {
        this.subscriptionStrategy = subscriptionStrategy;

        depPivots = new Dependent(new UpdateMethod() {
			
			@Override
			public void update() {
				updatePivots();
			}
		});
    }

    public Iterable<CorrespondenceFact> getPivots() {
        depPivots.onGet();
        return pivots;
    }

    private void updatePivots() {
    	Iterable<CorrespondenceFact> subscriptions = subscriptionStrategy.getSubscriptions();
		pivots = new ArrayList<CorrespondenceFact>();
    	if (subscriptions != null) {
    		for (CorrespondenceFact pivot : subscriptions) {
    			pivots.add(pivot);
    		}
    	}
    }
}
