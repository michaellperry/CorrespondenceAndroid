package com.updatecontrols.correspondence.memento;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * FactMemento
 * @author mperry
 * Record all information about a correspondence object in a serializable form.
 */
public class FactMemento {

	private CorrespondenceFactType type;
	private ArrayList<PredecessorMemento> predecessors = new ArrayList<PredecessorMemento>();
	private byte[] data;
	
	public FactMemento(CorrespondenceFactType type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public CorrespondenceFactType getType() {
		return type;
	}

	public void setType(CorrespondenceFactType type) {
		this.type = type;
	}
	
	public int getPredecessorCount() {
		return predecessors.size();
	}

	public Iterable<PredecessorMemento> getPredecessors() {
		return predecessors;
	}
	
	public void addPredecessor(RoleMemento role, FactID id, boolean isPivot) {
		predecessors.add(new PredecessorMemento(role, id, isPivot));
	}
	
	public Iterable<FactID> getPredecessorIdsByRole(final RoleMemento role) {
		// Create an iterator that returns only the matching object IDs.
		return com.mallardsoft.query.QuerySpec
			.from(predecessors)
			.where(new com.mallardsoft.query.Predicate<PredecessorMemento>() {
				public boolean where(PredecessorMemento row) {
					return row.getRole().equals(role);
				}
			})
			.select(new com.mallardsoft.query.Selector<PredecessorMemento, FactID>() {
				public FactID select(PredecessorMemento row) {
					return row.getId();
				}				
			});
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result;
		for (PredecessorMemento predecessor: predecessors) {
			result += predecessor.hashCode();
		}
		result = prime * result
				+ ((type == null) ? 0 : type.hashCode());
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
		final FactMemento other = (FactMemento) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (predecessors == null) {
			if (other.predecessors != null)
				return false;
		} else {
			if (predecessors.size() != other.predecessors.size())
				return false;
			for (PredecessorMemento first: predecessors) {
				if (!other.predecessors.contains(first))
					return false;
			}
		}
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append(type.getName()).append(", ")
			.append(type.getVersion()).append(", ")
			.append(predecessors).append(", ")
			.append(data.length).toString();
	}
	
}
