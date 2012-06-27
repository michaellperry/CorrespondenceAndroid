package com.updatecontrols.correspondence.strategy;

import java.util.List;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactTreeMemento;
import com.updatecontrols.correspondence.memento.GetResultMemento;
import com.updatecontrols.correspondence.memento.TimestampID;
import com.updatecontrols.correspondence.memento.UnpublishMemento;

public interface SynchronousCommunicationStrategy {

    String getProtocolName();
    String getPeerName();
    GetResultMemento get(FactTreeMemento pivotTree, FactID pivotId, TimestampID timestamp);
    void Post(FactTreeMemento messageBody, List<UnpublishMemento> unpublishedMessages);
}
