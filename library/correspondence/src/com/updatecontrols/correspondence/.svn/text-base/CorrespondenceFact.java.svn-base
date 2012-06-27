package com.updatecontrols.correspondence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.updatecontrols.correspondence.memento.CorrespondenceFactType;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.RoleMemento;
import com.updatecontrols.correspondence.query.QueryDefinition;

public abstract class CorrespondenceFact {
	
	private FactID id;
	private HashMap<RoleMemento, PredecessorBase> predecessorByRole = new HashMap<RoleMemento, PredecessorBase>();
	private HashMap<QueryDefinition, List<Result<?>>> resultsByQueryDefinition = new HashMap<QueryDefinition, List<Result<?>>>();
	
	private Community community;
	
	public CorrespondenceFact() {
	}

	protected Community getCommunity() {
		return community;
	}
	
	/* internal */ void setCommunity(Community community) {
		this.community = community;
	}

	/* internal */ FactID getId() {
		return id;
	}

	/* internal */ void setId(FactID id) {
		this.id = id;
	}

	/* internal */ void attachPredecessorSet(RoleMemento role, PredecessorBase predecessorSet) {
		predecessorByRole.put(role, predecessorSet);
	}
	
	/* internal */ Iterable<RoleMemento> getPredecessorRoles() {
		return predecessorByRole.keySet();
	}
	
	/* internal */ PredecessorBase getPredecessorSetByRole(RoleMemento role) {
		return predecessorByRole.get(role);
	}
	
	/* internal */ void addPredecessorsToMemento(FactMemento factMemento) {
		// Record the predecessor IDs.
		for (RoleMemento role: predecessorByRole.keySet()) {
			PredecessorBase predecessorSet = predecessorByRole.get(role);
			for (FactID predecessorId : predecessorSet.getFactIDs()) {
				factMemento.addPredecessor(role, predecessorId, role.isPivot());
			}
		}
	}

	/* internal */ void invalidateQuery(QueryDefinition invalidatedQuery) {
		List<Result<?>> results = resultsByQueryDefinition.get(invalidatedQuery);
		if (results != null) {
			for (Result<?> result : results) {
				result.invalidate();
			}
		}
	}
	
	/* internal */ void addQueryResult(QueryDefinition queryDefinition, Result<?> result) {
		List<Result<?>> results = resultsByQueryDefinition.get(queryDefinition);
		if (results == null) {
			results = new ArrayList<Result<?>>();
			resultsByQueryDefinition.put(queryDefinition, results);
		}
		results.add(result);
	}

	protected abstract CorrespondenceFactType getCorrespondenceFactType();
}
