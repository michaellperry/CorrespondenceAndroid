package com.updatecontrols.correspondence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.PredecessorMemento;

public class PredecessorObj<F extends CorrespondenceFact> extends PredecessorBase {
	
	private FactID id;
	private F fact;
	
	public PredecessorObj(CorrespondenceFact subject, Role role, F fact) {
		// When creating a predecessor with a known fact, it is immediately cached.
		super(subject, true);
		
		this.id = fact.getId();
		this.fact = fact;		
		subject.attachPredecessorSet(role.getMemento(), this);
	}
	
	public PredecessorObj(CorrespondenceFact subject, Role role, FactMemento factMemento) throws CorrespondenceException {
		// When creating a predecessor with a memento, it is not cached.
		super(subject, false);
		
		// Get the predecessor ID from the memento.
		boolean found = false;
		for (FactID id: factMemento.getPredecessorIdsByRole(role.getMemento())) {
			// If you found one, take it.
			if (!found) {
				this.id = id;
				found = true;
			}
			// If you found two, error.
			else {
				throw new CorrespondenceException("Many predecessors found where there should be one.");
			}
		}
		// If you found none, error.
		if (!found) {
			throw new CorrespondenceException(String.format("No predecessor found in role %1s. %2s", role.getMemento(), commaSeparated(factMemento.getPredecessors())));
		}

		subject.attachPredecessorSet(role.getMemento(), this);
	}

	private String commaSeparated(Iterable<PredecessorMemento> predecessors) {
		StringBuilder builder = new StringBuilder();
		for (PredecessorMemento item : predecessors) {
			if (builder.length() > 0)
				builder.append(", ");
			builder.append(item.getRole());
		}
		return builder.toString();
	}

	public F getFact() {
		synchronized (this) {
			onGet();
			return fact;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void populateCache(Community community) {
		// Resolve the id to an fact.
		fact = (F)community.getFactById(id);
	}

	@Override
	protected void emptyCache() {
		fact = null;
	}

	@Override
	protected Iterable<FactID> getFactIDs() {
		// Create an iterator over the one fact.
		return new Iterable<FactID>() {
			public Iterator<FactID> iterator() {
				return new Iterator<FactID>() {
					private boolean finished = false;
					public boolean hasNext() {
						return !finished;
					}
					public FactID next() {
						if (!finished) {
							finished = true;
							return id;
						}
						else
							throw new NoSuchElementException();
					}
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	@Override
	protected Iterable<CorrespondenceFact> internalGetFacts() {
		// Return a collection containing the one fact.
		ArrayList<CorrespondenceFact> list = new ArrayList<CorrespondenceFact>();
		list.add(getFact());
		return list;
	}

}
