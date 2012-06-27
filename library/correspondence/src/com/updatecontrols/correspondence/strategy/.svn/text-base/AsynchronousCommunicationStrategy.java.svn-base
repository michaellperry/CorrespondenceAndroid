package com.updatecontrols.correspondence.strategy;

import java.util.List;
import java.util.UUID;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactTreeMemento;
import com.updatecontrols.correspondence.memento.PivotMemento;
import com.updatecontrols.correspondence.memento.UnpublishMemento;

public interface AsynchronousCommunicationStrategy {

    String getProtocolName();
    String getPeerName();
    void beginGetMany(FactTreeMemento pivotTree, List<PivotMemento> pivots, UUID clientGuid, GetManyCallback success, ErrorCallback error);
    void beginPost(FactTreeMemento messageBody, UUID clientGuid, List<UnpublishMemento> unpublishedMessages, PostCallback success, ErrorCallback error);
	void interrupt(UUID clientDatabaseGuid);
    boolean isLongPolling();
    
    void addMessageReceivedListener(MessageReceivedListener messageReceivedListener);
    PushSubscription subscribeForPush(FactTreeMemento pivotTree, FactID pivotID, UUID clientGuid);
    
}
