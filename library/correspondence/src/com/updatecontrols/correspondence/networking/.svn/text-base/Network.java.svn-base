package com.updatecontrols.correspondence.networking;

import java.util.ArrayList;
import java.util.List;

import com.updatecontrols.correspondence.Model;
import com.updatecontrols.correspondence.strategy.AsynchronousCommunicationStrategy;
import com.updatecontrols.correspondence.strategy.CommunicationStrategy;
import com.updatecontrols.correspondence.strategy.StorageStrategy;
import com.updatecontrols.correspondence.strategy.TaskDispatcher;

public class Network implements SubscriptionProvider {

    private List<Subscription> subscriptions = new ArrayList<Subscription>();

	private SynchronousNetwork synchronousNetwork;
	private AsynchronousNetwork asynchronousNetwork;

	public Network(Model model, StorageStrategy storageStrategy, TaskDispatcher dispatcher) {
        this.synchronousNetwork = new SynchronousNetwork(this, model, storageStrategy);
		this.asynchronousNetwork = new AsynchronousNetwork(this, model, storageStrategy, dispatcher);
	}

    public void subscribe(Subscription subscription) {
        subscriptions.add(subscription);
    }

	public Iterable<Subscription> getSubscriptions() {
		return subscriptions;
	}

    public void addCommunicationStrategy(CommunicationStrategy communicationStrategy) {
		synchronousNetwork.addCommunicationStrategy(communicationStrategy);
	}

    public void addAsynchronousCommunicationStrategy(AsynchronousCommunicationStrategy asynchronousCommunicationStrategy) {
		asynchronousNetwork.addAsynchronousCommunicationStrategy(asynchronousCommunicationStrategy);
	}

	public void beginReceiving() {
		asynchronousNetwork.beginReceiving();
	}

    public void beginSending() {
        asynchronousNetwork.beginSending();
    }

    public boolean synchronize() {
		return synchronousNetwork.synchronize();
	}

    public boolean isSynchronizing() {
        return asynchronousNetwork.isSynchronizing() || synchronousNetwork.isSynchronizing();
    }

    public Exception getLastException() {
        return asynchronousNetwork.getLastException();
    }
}
