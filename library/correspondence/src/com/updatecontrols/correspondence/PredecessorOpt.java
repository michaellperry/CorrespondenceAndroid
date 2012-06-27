package com.updatecontrols.correspondence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.FactID;

public class PredecessorOpt<F extends CorrespondenceFact> extends PredecessorBase {

	private FactID id;
	private F fact;
	
	public PredecessorOpt(CorrespondenceFact subject, Role role, F fact) {
		// When creating a predecessor with a known fact, it is immediately cached.
		super(subject, true);

		this.fact = fact;
		if (fact == null)
			this.id = null;
		else {
			this.id = fact.getId();
		}
		
		subject.attachPredecessorSet(role.getMemento(), this);
	}
	
	public PredecessorOpt(CorrespondenceFact subject, Role role, FactMemento factMemento) throws CorrespondenceException {
		// When creating a predecessor with a memento, it is not cached.
		super(subject, false);
		
		// Get the predecessor from the model by the ID in the memento.
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
		// If you found none, then it's null.
		if (!found) {
			this.fact = null;
		}

		subject.attachPredecessorSet(role.getMemento(), this);
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
		if (id != null) {
			// Resolve the id to an fact.
			fact = (F)community.getFactById(id);
		}
	}

	@Override
	protected void emptyCache() {
		if (fact != null) {
			fact = null;
		}
	}

	@Override
	protected Iterable<FactID> getFactIDs() {
		// Create an iterator over the one fact.
		return new Iterable<FactID>() {
			public Iterator<FactID> iterator() {
				return new Iterator<FactID>() {
					// The iterator is initially finished if there is no fact.
					private boolean finished = id == null;
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
		// Return a collection containing the one fact, if it exists.
		ArrayList<CorrespondenceFact> list = new ArrayList<CorrespondenceFact>();
		F object = getFact();
		if (object != null) {
			list.add(object);
		}
		return list;
	}

}
