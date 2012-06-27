package com.updatecontrols.correspondence;

import com.updatecontrols.correspondence.memento.FactID;

public abstract class PredecessorBase {

	private CorrespondenceFact subject;
	private boolean cached;
	
	protected PredecessorBase(CorrespondenceFact subject, boolean cached) {
		this.subject = subject;
		this.cached = cached;
	}

	protected abstract void populateCache(Community community);
	protected abstract void emptyCache();
	protected abstract Iterable<FactID> getFactIDs();
	protected abstract Iterable<CorrespondenceFact> internalGetFacts();
	
	protected void onGet() {
		Community community = subject.getCommunity();
		if (!cached) {
			// Cache the predecessor.
			cached = true;
			populateCache(community);
		}
	}
	
	public void unload() {
		synchronized (this) {
			if (cached) {
				cached = false;
				emptyCache();
			}
		}
	}

}
