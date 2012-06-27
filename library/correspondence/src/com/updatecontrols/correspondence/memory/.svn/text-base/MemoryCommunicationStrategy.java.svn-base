package com.updatecontrols.correspondence.memory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.FactTreeMemento;
import com.updatecontrols.correspondence.memento.IdentifiedFactBase;
import com.updatecontrols.correspondence.memento.IdentifiedFactMemento;
import com.updatecontrols.correspondence.memento.IdentifiedFactRemote;
import com.updatecontrols.correspondence.memento.PivotMemento;
import com.updatecontrols.correspondence.memento.PredecessorMemento;
import com.updatecontrols.correspondence.memento.TimestampID;
import com.updatecontrols.correspondence.memento.UnpublishMemento;
import com.updatecontrols.correspondence.strategy.AsynchronousCommunicationStrategy;
import com.updatecontrols.correspondence.strategy.ErrorCallback;
import com.updatecontrols.correspondence.strategy.GetManyCallback;
import com.updatecontrols.correspondence.strategy.MessageReceivedListener;
import com.updatecontrols.correspondence.strategy.PostCallback;
import com.updatecontrols.correspondence.strategy.PushSubscription;
import com.updatecontrols.correspondence.strategy.StorageStrategy;

public class MemoryCommunicationStrategy implements AsynchronousCommunicationStrategy {
	
	private StorageStrategy _repository = new MemoryStorageStrategy();
	private static long _databaseId = 0;

	@Override
	public void addMessageReceivedListener(MessageReceivedListener messageReceivedListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getProtocolName() {
		return "Memory";
	}

	@Override
	public String getPeerName() {
		return "Local";
	}

	@Override
	public void beginGetMany(FactTreeMemento pivotTree,
			List<PivotMemento> pivots, UUID clientGuid,
			GetManyCallback success, ErrorCallback error) {
		
        FactTreeMemento messageBody = new FactTreeMemento(_databaseId);
        ArrayList<PivotMemento> newPivots = new ArrayList<PivotMemento>();
        
		for (PivotMemento pivot : pivots) {
			FactID localPivotId = findExistingFact(pivot.getPivotId(), pivotTree);
	        long nextTimestamp = pivot.getTimestamp().getKey();
	        if (localPivotId != null) {
		        Iterable<FactID> recentMessages = _repository.loadRecentMessagesForClient(localPivotId, pivot.getTimestamp());
		        for (FactID recentMessage : recentMessages) {
		        	if (recentMessage.getKey() > nextTimestamp)
		        		nextTimestamp = recentMessage.getKey();
		        }
		        for (FactID recentMessage : recentMessages)
		            addToFactTree(messageBody, recentMessage);
	        }
	        newPivots.add(new PivotMemento(pivot.getPivotId(), new TimestampID(_databaseId, nextTimestamp)));
			success.onSuccess(messageBody, newPivots);
		}
	}

	@Override
	public void beginPost(FactTreeMemento messageBody, UUID clientGuid,
			List<UnpublishMemento> unpublishedMessages, PostCallback success,
			ErrorCallback error) {

        Hashtable<FactID, FactID> localIdByRemoteId = new Hashtable<FactID, FactID>();
        for (IdentifiedFactBase identifiedFact : messageBody.getFacts()) {
            FactID localId;
            if (identifiedFact instanceof IdentifiedFactMemento) {
                FactMemento memento = ((IdentifiedFactMemento)identifiedFact).getMemento();
                FactMemento translatedMemento = new FactMemento(memento.getType());
                translatedMemento.setData(memento.getData());
                for (PredecessorMemento predecessor : memento.getPredecessors()) {
                	translatedMemento.addPredecessor(predecessor.getRole(), localIdByRemoteId.get(predecessor.getId()), predecessor.isPivot());
                }
                localId = _repository.save(translatedMemento);
            }
            else {
                // I am remote to the sender, so my local ID is his remote ID.
                IdentifiedFactRemote identifiedFactRemote = (IdentifiedFactRemote)identifiedFact;
                localId = identifiedFactRemote.getRemoteId();
            }
            FactID remoteId = identifiedFact.getId();
            localIdByRemoteId.put(remoteId, localId);
        }

        for (UnpublishMemento unpublishedMessage : unpublishedMessages) {
            FactID localMessageId = localIdByRemoteId.get(unpublishedMessage.getMessageId());
            if (localMessageId != null)
                _repository.unpublish(localMessageId, unpublishedMessage.getRole());
        }
	}

	@Override
	public void interrupt(UUID clientDatabaseGuid) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLongPolling() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PushSubscription subscribeForPush(FactTreeMemento pivotTree,
			FactID pivotID, UUID clientGuid) {
		// TODO Auto-generated method stub
		return null;
	}

	private FactID findExistingFact(FactID remotePivotId, FactTreeMemento messageBody) {
        Hashtable<FactID, FactID> localIdByRemoteId = new Hashtable<FactID, FactID>();
        for (IdentifiedFactBase identifiedFact : messageBody.getFacts()) {
            FactID localId;
            if (identifiedFact instanceof IdentifiedFactMemento) {
                FactMemento memento = ((IdentifiedFactMemento)identifiedFact).getMemento();
                FactMemento translatedMemento = new FactMemento(memento.getType());
                translatedMemento.setData(memento.getData());
                for (PredecessorMemento predecessor : memento.getPredecessors()) {
                	translatedMemento.addPredecessor(predecessor.getRole(), localIdByRemoteId.get(predecessor.getId()), predecessor.isPivot());
                }
                localId = _repository.findExistingFact(translatedMemento);
                if (localId == null)
                    return null;
            }
            else {
                // I am remote to the sender, so my local ID is his remote ID.
                IdentifiedFactRemote identifiedFactRemote = (IdentifiedFactRemote)identifiedFact;
                localId = identifiedFactRemote.getRemoteId();
            }
            FactID remoteId = identifiedFact.getId();
            localIdByRemoteId.put(remoteId, localId);
        }
        return localIdByRemoteId.get(remotePivotId);
	}

	private void addToFactTree(FactTreeMemento messageBody, FactID factId) {
        if (!messageBody.contains(factId)) {
            FactMemento fact = _repository.load(factId);
            for (PredecessorMemento predecessor : fact.getPredecessors())
                addToFactTree(messageBody, predecessor.getId());
            messageBody.add(new IdentifiedFactMemento(factId, fact));
        }
	}

}
