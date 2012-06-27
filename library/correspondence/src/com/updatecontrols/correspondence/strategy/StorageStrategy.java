package com.updatecontrols.correspondence.strategy;

import java.util.UUID;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.IdentifiedFactMemento;
import com.updatecontrols.correspondence.memento.MessageMemento;
import com.updatecontrols.correspondence.memento.RoleMemento;
import com.updatecontrols.correspondence.memento.TimestampID;
import com.updatecontrols.correspondence.query.QueryDefinition;

public interface StorageStrategy {

	UUID getClientGuid();
	
	// Facts
	FactID getID(String factName);
	void setID(String factName, FactID id);
	FactMemento load(FactID id);
	FactID save(FactMemento factMemento);
	FactID findExistingFact(FactMemento factMemento);
	Iterable<IdentifiedFactMemento> queryForFacts(QueryDefinition queryDefinition, FactID id);
	Iterable<FactID> queryForIds(QueryDefinition targetFacts, FactID id);

	// Messages
	TimestampID loadOutgoingTimestamp(int peerId);
	void saveOutgoingTimestamp(int peerId, TimestampID timestamp);
	TimestampID loadIncomingTimestamp(int peerId, FactID pivotId);
	void saveIncomingTimestamp(int peerId, FactID pivotId, TimestampID timestamp);
	Iterable<MessageMemento> loadRecentMessagesForServer(int peerId, TimestampID timestamp);
	Iterable<FactID> loadRecentMessagesForClient(FactID localPivotId, TimestampID timestamp);
	void unpublish(FactID localMessageId, RoleMemento role);
	
	// Networking
	int savePeer(String protocolName, String peerName);
	FactID getFactIDFromShare(int peerId, FactID remoteId);
	FactID getRemoteId(FactID factId, int peerId);
	void saveShare(int peerId, FactID remoteFactId, FactID localFactId);
}
