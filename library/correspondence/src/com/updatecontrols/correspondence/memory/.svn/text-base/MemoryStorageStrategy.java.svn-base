package com.updatecontrols.correspondence.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mallardsoft.query.Predicate;
import com.mallardsoft.query.QuerySpec;
import com.mallardsoft.query.Selector;
import com.updatecontrols.correspondence.memento.CorrespondenceFactType;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.IdentifiedFactMemento;
import com.updatecontrols.correspondence.memento.MessageMemento;
import com.updatecontrols.correspondence.memento.PredecessorMemento;
import com.updatecontrols.correspondence.memento.RoleMemento;
import com.updatecontrols.correspondence.memento.TimestampID;
import com.updatecontrols.correspondence.query.Condition;
import com.updatecontrols.correspondence.query.Join;
import com.updatecontrols.correspondence.query.QueryDefinition;
import com.updatecontrols.correspondence.strategy.StorageStrategy;

public class MemoryStorageStrategy implements StorageStrategy {

	private long nextObjectId = 1L;
	/* internal */ ArrayList<FactRecord> factTable = new ArrayList<FactRecord>();
	/* internal */ ArrayList<RelationshipRecord> relationshipTable = new ArrayList<RelationshipRecord>();
	/* internal */ ArrayList<MessageRecord> messageTable = new ArrayList<MessageRecord>();
	/* internal */ ArrayList<IncomingTimestampRecord> incomingTimestampTable = new ArrayList<IncomingTimestampRecord>();
	/* internal */ ArrayList<OutgoingTimestampRecord> outgoingTimestampTable = new ArrayList<OutgoingTimestampRecord>();
	/* internal */ ArrayList<PeerRecord> peerTable = new ArrayList<PeerRecord>();
	/* internal */ UUID clientGuid;
	
	// Type specific "tables".
	/* internal */ ArrayList<DateIndexMemento> dateIndices = new ArrayList<DateIndexMemento>();
	
	// Hit counter
	private int hits = 0;

	@Override
	public FactMemento load(FactID id) {
		FactRecord found = getFactRecordById(id);
		if (found == null)
			return null;

		return loadMemento(found);
	}

	@Override
	public Iterable<IdentifiedFactMemento> queryForFacts(
			QueryDefinition queryDefinition, FactID id) {
		ArrayList<FactID> ids = getMatchingIds(queryDefinition, id);
		return QuerySpec
			.from(ids)
			.select(new Selector<FactID, IdentifiedFactMemento>() {
				public IdentifiedFactMemento select(FactID row) {
					return new IdentifiedFactMemento(row, load(row));
				}
			});
	}

	@Override
	public Iterable<FactID> queryForIds(QueryDefinition queryDefinition, FactID id) {
		return getMatchingIds(queryDefinition, id);
	}

	/* internal */ ArrayList<FactID> getMatchingIds(QueryDefinition queryDefinition, FactID id) {
		ArrayList<FactID> ids = new ArrayList<FactID>();
		ids.add(id);
		
		// Execute each join, from the object up to the results.
		List<Join> joins = queryDefinition.getJoins();
		for (Join join : joins) {
			ArrayList<FactID> results = new ArrayList<FactID>();
			for (FactID start: ids) {
				for (RelationshipRecord r: relationshipTable) {
					if (join.isSuccessor()) {
						if (
								r.getPredecessorID().equals(start) &&
								r.getRole().equals(join.getRole()) &&
								matchesCondition(join.getConditions(), r.getObjectID()))
							results.add(r.getObjectID());
					}
					else {
						if (
								r.getObjectID().equals(start) &&
								r.getRole().equals(join.getRole()) &&
								matchesCondition(join.getConditions(), r.getObjectID()))
							results.add(r.getPredecessorID());
					}
				}
			}
			ids = results;
		}
		return ids;
	}

	private boolean matchesCondition(
			List<Condition> conditions,
			FactID factID) {
		
		if (conditions != null) {
			for (Condition condition : conditions) {
				ArrayList<FactID> ids = getMatchingIds(condition.getSubQuery(), factID);
				if (condition.isEmpty() && !ids.isEmpty())
					return false;
				else if (!condition.isEmpty() && ids.isEmpty())
					return false;
			}
		}
		return true;
	}

	@Override
	public FactID save(FactMemento factMemento) {
		// First check to see if the object is already in storage.
		int hashCode = factMemento.hashCode();
		for (FactRecord fact: factTable) {
			if (fact.getHashCode() == hashCode && fact.getType().equals(factMemento.getType().getName())) {
				FactMemento candidate = loadMemento(fact);
				if (candidate.equals(factMemento))
					// It's already here, so return its id.
					return fact.getId();
			}
		}
		
		// Generate a new id.
		FactID factId = new FactID(nextObjectId++);
		
		// Store the object memento.
		factTable.add(0, new FactRecord(factId, factMemento.getType().getName(), factMemento.getType().getVersion(), factMemento.getData(), factMemento.hashCode(), 0));
		
		// Store the predecessor relationships.
		int predecessorIndex = 0;
		for (PredecessorMemento predecessor: factMemento.getPredecessors()) {
			relationshipTable.add(predecessorIndex, new RelationshipRecord(factId, predecessor.getRole(), predecessor.getId(), predecessor.isPivot()));
			predecessorIndex++;
		}
		
		// Store a message for each pivot.
		for (PredecessorMemento predecessor: factMemento.getPredecessors()) {
			if (predecessor.isPivot()) {
				messageTable.add(new MessageRecord(new MessageMemento(predecessor.getId(), factId), factId, predecessor.getRole()));
			}
		}
		
		// Store messages for each non-pivot. This fact belongs to all predecessor pivots.
		ArrayList<FactID> nonPivots = new ArrayList<FactID>();
		for (PredecessorMemento predecessor: factMemento.getPredecessors()) {
			if (!predecessor.isPivot()) {
				nonPivots.add(predecessor.getId());
			}
		}
		ArrayList<MessageRecord> predecessorPivots = new ArrayList<MessageRecord>();
		for (MessageRecord message : messageTable) {
			if (!predecessorPivots.contains(message) && nonPivots.contains(message.getMessage().getFactId())) {
				predecessorPivots.add(message);
			}
		}
		for (MessageRecord predecessorPivot : predecessorPivots) {
			messageTable.add(new MessageRecord(
				new MessageMemento(predecessorPivot.getMessage().getFactId(), factId),
				predecessorPivot.getAncestorFact(),
				predecessorPivot.getAncestorRole()));
		}
		
		return factId;
	}

	@Override
	public FactID findExistingFact(FactMemento factMemento) {
		int hashCode = factMemento.hashCode();
		for (FactRecord fact: factTable) {
			if (fact.getHashCode() == hashCode && fact.getType().equals(factMemento.getType().getName())) {
				FactMemento candidate = loadMemento(fact);
				if (candidate.equals(factMemento))
					// It's already here, so return its id.
					return fact.getId();
			}
		}
		
		return null;
	}

	@Override
	public FactID getID(String objectName) {
		return null;
	}

	@Override
	public void setID(String objectName, FactID id) {
	}

	public FactMemento loadMemento(FactRecord record) {
		// Load the object type and data.
		FactMemento factMemento = new FactMemento(new CorrespondenceFactType(record.getType(), record.getVersion()));
		factMemento.setData(record.getData());
		
		// Load the predecessors.
		for (RelationshipRecord relationship: relationshipTable) {
			if (relationship.getObjectID().equals(record.getId())) {
				factMemento.addPredecessor(relationship.getRole(), relationship.getPredecessorID(), relationship.isPivot());
			}
		}
		
		return factMemento;
	}

	/* internal */ void countHit() {
		++hits;
	}
	
	public int getDatabaseHits() {
		return hits;
	}

	@Override
	public UUID getClientGuid() {
		if (clientGuid == null)
			clientGuid = UUID.randomUUID();
		return clientGuid;
	}

	@Override
	public FactID getFactIDFromShare(int peerId, FactID remoteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FactID getRemoteId(FactID factId, int peerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimestampID loadIncomingTimestamp(int peerId, FactID pivotId) {
		IncomingTimestampRecord record = getIncomingTimestampByPeerAndPivot(peerId, pivotId);
		return record == null ? new TimestampID(0, 0) : record.timestampId;
	}

	@Override
	public TimestampID loadOutgoingTimestamp(int peerId) {
		OutgoingTimestampRecord record = getOutgoingTimestampByPeer(peerId);
		return record == null ? new TimestampID(0, 0) : record.timestamp;
	}

	@Override
	public Iterable<MessageMemento> loadRecentMessagesForServer(int peerId, TimestampID timestamp) {
		ArrayList<MessageMemento> messages = new ArrayList<MessageMemento>();
		for (MessageRecord message : messageTable) {
			FactRecord factRecord = getFactRecordById(message.getMessage().getFactId());
			if ((timestamp == null || message.getMessage().getFactId().getKey() > timestamp.getKey()) &&
				factRecord.getPeerId() != peerId &&
				!messages.contains(message.getMessage())) {
				messages.add(message.getMessage());
			}
		}
		return messages;
	}

	@Override
	public Iterable<FactID> loadRecentMessagesForClient(FactID localPivotId, TimestampID timestamp) {
		ArrayList<FactID> messages = new ArrayList<FactID>();
		for (MessageRecord message : messageTable) {
			if (message.getMessage().getPivotId().getKey() == localPivotId.getKey() &&
				message.getMessage().getFactId().getKey() > timestamp.getKey() &&
				!messages.contains(message.getMessage().getFactId())) {
				messages.add(message.getMessage().getFactId());
			}
		}
		return messages;
	}

	@Override
	public void saveIncomingTimestamp(int peerId, FactID pivotId, TimestampID timestamp) {
		IncomingTimestampRecord record = getIncomingTimestampByPeerAndPivot(peerId, pivotId);
		if (record != null)
			record.timestampId = timestamp;
		else
			incomingTimestampTable.add(new IncomingTimestampRecord(peerId, pivotId, timestamp));
	}

	@Override
	public void saveOutgoingTimestamp(int peerId, TimestampID timestamp) {
		OutgoingTimestampRecord record = getOutgoingTimestampByPeer(peerId);
		if (record != null)
			record.timestamp = timestamp;
		else
			outgoingTimestampTable.add(new OutgoingTimestampRecord(peerId, timestamp));
	}

	@Override
	public int savePeer(final String protocolName, final String peerName) {
		PeerRecord record = QuerySpec.from(peerTable)
			.where(new Predicate<PeerRecord>() {
				@Override
				public boolean where(PeerRecord row) {
					return row.protocolName.equals(protocolName) && row.peerName.equals(peerName);
				}
			})
			.selectFirstOrNull();
		if (record == null) {
			record = new PeerRecord(protocolName, peerName, peerTable.size() + 1);
			peerTable.add(record);
		}
		return record.peerId;
	}

	@Override
	public void saveShare(int peerId, FactID remoteFactId, FactID localFactId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unpublish(FactID localMessageId, RoleMemento role) {
		// TODO Auto-generated method stub
		
	}

	private FactRecord getFactRecordById(FactID factId) {
		for (FactRecord fact : factTable) {
			if (fact.getId().equals(factId)) {
				return fact;
			}
		}
		return null;
	}
	
	private OutgoingTimestampRecord getOutgoingTimestampByPeer(final int peerId) {
		return QuerySpec.from(outgoingTimestampTable)
			.where(new Predicate<OutgoingTimestampRecord>() {
				@Override
				public boolean where(OutgoingTimestampRecord row) {
					return row.peerId == peerId;
				}
			})
			.selectFirstOrNull();
	}

	private IncomingTimestampRecord getIncomingTimestampByPeerAndPivot(final int peerId, final FactID pivotId) {
		IncomingTimestampRecord record = QuerySpec.from(incomingTimestampTable)
			.where(new Predicate<IncomingTimestampRecord>() {
				@Override
				public boolean where(IncomingTimestampRecord row) {
					return row.peerId == peerId && row.pivotId.equals(pivotId);
				}
			})
			.selectFirstOrNull();
		return record;
	}
}
