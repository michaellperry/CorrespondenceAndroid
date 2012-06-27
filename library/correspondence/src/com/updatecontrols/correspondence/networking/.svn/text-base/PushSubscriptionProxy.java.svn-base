package com.updatecontrols.correspondence.networking;

import com.updatecontrols.Disposable;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.Inspector;
import com.updatecontrols.correspondence.Model;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactTreeMemento;
import com.updatecontrols.correspondence.strategy.PushSubscription;

public class PushSubscriptionProxy implements Disposable {

	private static long ClientDatabasId = 0;

	private Model model;
    private AsynchronousServerProxy serverProxy;
    private CorrespondenceFact pivot;
	private PushSubscription pushSubscription = null;
	
	public PushSubscriptionProxy(Model model,
			AsynchronousServerProxy serverProxy, CorrespondenceFact pivot) {
		super();
		this.model = model;
		this.serverProxy = serverProxy;
		this.pivot = pivot;
	}

	public void subscribe() {
		if (pushSubscription == null) {
			FactTreeMemento pivotTree = new FactTreeMemento(ClientDatabasId);
			FactID pivotId = Inspector.idOf(pivot);
			model.addToFactTree(pivotTree, pivotId, serverProxy.getPeerId());
			pushSubscription = serverProxy.getCommunicationStrategy().subscribeForPush(pivotTree, pivotId, model.getClientDatabaseGuid());
		}
	}

	public void dispose() {
		if (pushSubscription != null)
			pushSubscription.unsubscribe();
		pushSubscription = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pivot == null) ? 0 : pivot.hashCode());
		result = prime * result
				+ ((serverProxy == null) ? 0 : serverProxy.hashCode());
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
		PushSubscriptionProxy other = (PushSubscriptionProxy) obj;
		if (pivot == null) {
			if (other.pivot != null)
				return false;
		} else if (!pivot.equals(other.pivot))
			return false;
		if (serverProxy == null) {
			if (other.serverProxy != null)
				return false;
		} else if (!serverProxy.equals(other.serverProxy))
			return false;
		return true;
	}

}
