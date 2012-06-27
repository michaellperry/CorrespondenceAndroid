package com.updatecontrols.correspondence.strategy;

import com.updatecontrols.correspondence.CorrespondenceFact;

public interface SubscriptionStrategy {

	Iterable<CorrespondenceFact> getSubscriptions();
}
