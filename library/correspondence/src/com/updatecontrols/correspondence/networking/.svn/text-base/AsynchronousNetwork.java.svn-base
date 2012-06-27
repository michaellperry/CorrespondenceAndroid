package com.updatecontrols.correspondence.networking;

import java.util.ArrayList;

import com.updatecontrols.Dependent;
import com.updatecontrols.Independent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.RecycleBin;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.correspondence.CorrespondenceFact;
import com.updatecontrols.correspondence.Inspector;
import com.updatecontrols.correspondence.Model;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactTreeMemento;
import com.updatecontrols.correspondence.memento.PivotMemento;
import com.updatecontrols.correspondence.memento.TimestampID;
import com.updatecontrols.correspondence.memento.UnpublishMemento;
import com.updatecontrols.correspondence.strategy.AsynchronousCommunicationStrategy;
import com.updatecontrols.correspondence.strategy.ErrorCallback;
import com.updatecontrols.correspondence.strategy.GetManyCallback;
import com.updatecontrols.correspondence.strategy.MessageReceivedListener;
import com.updatecontrols.correspondence.strategy.PostCallback;
import com.updatecontrols.correspondence.strategy.StorageStrategy;
import com.updatecontrols.correspondence.strategy.TaskDispatcher;

public class AsynchronousNetwork {

	private static long ClientDatabaseId = 0;

	private SubscriptionProvider subscriptionProvider;
	private Model model;
	private StorageStrategy storageStrategy;
	private TaskDispatcher dispatcher;

    private ArrayList<AsynchronousServerProxy> serverProxies = new ArrayList<AsynchronousServerProxy>();
    private boolean sending;
    private boolean keepSending;
    private boolean receiving;
    private boolean keepReceiving;
    private Exception lastException;
    
    private Independent indSending = new Independent();
    private Independent indReceiving = new Independent();
    private Independent indLastException = new Independent();

	private ArrayList<PushSubscriptionProxy> pushSubscriptions = new ArrayList<PushSubscriptionProxy>();
	private Dependent depPushSubscriptions;

	public AsynchronousNetwork(SubscriptionProvider subscriptionProvider, Model model, StorageStrategy storageStrategy, TaskDispatcher dispatcher) {
		this.subscriptionProvider = subscriptionProvider;
		this.model = model;
		this.storageStrategy = storageStrategy;
		this.dispatcher = dispatcher;
		
		depPushSubscriptions = new Dependent(new UpdateMethod() {
			@Override
			public void update() {
				updatePushSubscriptions();
			}
		});
		depPushSubscriptions.addInalidatedListener(new InvalidatedListener() {
			@Override
			public void invalidated() {
				triggerSubscriptionUpdate();
			}
		});
	}

	public void addAsynchronousCommunicationStrategy(
			AsynchronousCommunicationStrategy asynchronousCommunicationStrategy) {
        final int peerId = storageStrategy.savePeer(
                asynchronousCommunicationStrategy.getProtocolName(),
                asynchronousCommunicationStrategy.getPeerName());
            asynchronousCommunicationStrategy.addMessageReceivedListener(new MessageReceivedListener() {
				@Override
				public void messageReceived(FactTreeMemento messageBody) {
	                model.receiveMessage(messageBody, peerId);
	                // Trigger a receive on normal channels. This updates the
	                // timestamp and pulls down any messages that were too long
	                // for the push buffer.
	                beginReceiving();
				}
			});
            serverProxies.add(new AsynchronousServerProxy(asynchronousCommunicationStrategy, peerId));
	}
	
	public synchronized boolean isSynchronizing() {
		indReceiving.onGet();
		indSending.onGet();
		return receiving || sending;
	}

	public synchronized Exception getLastException() {
		indLastException.onGet();
		return lastException;
	}

	public void beginSending() {
		synchronized (this) {
			if (sending) {
				keepSending = true;
				return;
			}
		}
		send();
	}

	private void send() {
        boolean sending = false;
		for (final AsynchronousServerProxy serverProxy : serverProxies) {
			final TimestampID timestamp = storageStrategy.loadOutgoingTimestamp(serverProxy.getPeerId());
            FactTreeMemento messageBodies = model.getMessageBodies(timestamp, serverProxy.getPeerId(), new ArrayList<UnpublishMemento>());
            if (messageBodies != null && messageBodies.getFactCount() > 0) {
                sending = true;
                serverProxy.getCommunicationStrategy().beginPost(
            		messageBodies,
            		model.getClientDatabaseGuid(),
            		new ArrayList<UnpublishMemento>(),
            		new PostCallback() {
						@Override
						public void onSuccess() {
	                        storageStrategy.saveOutgoingTimestamp(serverProxy.getPeerId(), timestamp);
	                        send();
						}
					}, new ErrorCallback() {
						@Override
						public void onError(Exception e) {
							onSendError(e);
						}
					});
            }
		}

        synchronized (this) {
        	indSending.onSet();
            this.sending = sending;
        }
	}

	public void beginReceiving() {
		synchronized (this) {
			if (receiving) {
				keepReceiving = true;
				return;
			}
		}
		
		receive();
	}

	private void receive() {
        boolean receiving = false;
        for (final AsynchronousServerProxy serverProxy : serverProxies) {
            FactTreeMemento pivotTree = new FactTreeMemento(ClientDatabaseId);
            ArrayList<FactID> pivotIds = new ArrayList<FactID>();
            synchronized (this) {
                depPushSubscriptions.onGet();
                getPivots(pivotTree, pivotIds, serverProxy.getPeerId());
            }

            boolean anyPivots = !pivotIds.isEmpty();
            if (anyPivots) {
                receiving = true;
                ArrayList<PivotMemento> pivots = new ArrayList<PivotMemento>();
                for (FactID pivotId : pivotIds) {
                    TimestampID timestamp = storageStrategy.loadIncomingTimestamp(serverProxy.getPeerId(), pivotId);
                    pivots.add(new PivotMemento(pivotId, timestamp));
                }
                serverProxy.getCommunicationStrategy().beginGetMany(
                	pivotTree,
                	pivots,
                	model.getClientDatabaseGuid(),
                	new GetManyCallback() {
						@Override
						public void onSuccess(FactTreeMemento messageBody, Iterable<PivotMemento> newTimestamps) {
		                    boolean receivedFacts = messageBody.getFactCount() > 0;
		                    if (receivedFacts) {
		                        model.receiveMessage(messageBody, serverProxy.getPeerId());
		                        for (PivotMemento pivot : newTimestamps) {
		                            storageStrategy.saveIncomingTimestamp(serverProxy.getPeerId(), pivot.getPivotId(), pivot.getTimestamp());
		                        }
		                    }
		                    if (receivedFacts || serverProxy.getCommunicationStrategy().isLongPolling())
		                        receive();
		                    else {
		                        setReceiving(false);
		                    }
						}
					},
					new ErrorCallback() {
						@Override
						public void onError(Exception e) {
							onReceiveError(e);
						}
					});
            }
        }

        synchronized (this) {
        	indReceiving.onSet();
            this.receiving = receiving;
        }
	}

	private void updatePushSubscriptions() {
		RecycleBin<PushSubscriptionProxy> bin = new RecycleBin<PushSubscriptionProxy>(pushSubscriptions);
		try {
			pushSubscriptions.clear();
			for (Subscription subscription : subscriptionProvider.getSubscriptions()) {
				for (CorrespondenceFact pivot : subscription.getPivots()) {
					if (pivot != null) {
						for (AsynchronousServerProxy serverProxy : serverProxies) {
							PushSubscriptionProxy pushSubscription = bin.extract(new PushSubscriptionProxy(model, serverProxy, pivot));
							pushSubscriptions.add(pushSubscription);
							pushSubscription.subscribe();
						}
					}
				}
			}
		}
		finally {
			bin.dispose();
		}

        for (AsynchronousServerProxy serverProxy : serverProxies) {
            if (serverProxy.getCommunicationStrategy().isLongPolling())
                serverProxy.getCommunicationStrategy().interrupt(model.getClientDatabaseGuid());
        }

        beginReceiving();
	}

	private void getPivots(FactTreeMemento pivotTree, ArrayList<FactID> pivotIds, int peerId) {
        for (Subscription subscription : subscriptionProvider.getSubscriptions()) {
            for (CorrespondenceFact pivot : subscription.getPivots()) {
                if (pivot == null)
                    continue;

                FactID pivotId = Inspector.idOf(pivot);
                model.addToFactTree(pivotTree, pivotId, peerId);
                pivotIds.add(pivotId);
            }
        }
	}

	private synchronized void onSendError(Exception e) {
		indSending.onSet();
		sending = false;
		indLastException.onSet();
		lastException = e;
	}

	private synchronized void onReceiveError(Exception e) {
		indReceiving.onSet();
		receiving = false;
		indLastException.onSet();
		lastException = e;
	}

	private void triggerSubscriptionUpdate() {
		dispatcher.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				updatePushSubscriptions();
			}});
	}

	private synchronized void setReceiving(boolean value) {
		indReceiving.onSet();
	    receiving = value;
	}

}
