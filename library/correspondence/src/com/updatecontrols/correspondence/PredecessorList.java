package com.updatecontrols.correspondence;

import java.util.ArrayList;
import java.util.List;

import com.mallardsoft.query.QuerySpec;
import com.mallardsoft.query.Selector;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.FactMemento;

public class PredecessorList<F extends CorrespondenceFact> extends PredecessorBase {

	private ArrayList<FactID> ids = new ArrayList<FactID>();
	private ArrayList<F> facts;
	
	public PredecessorList(CorrespondenceFact subject, Role role, List<F> simpleList ) {
	    
        // When creating a predecessor with a known object, it is immediately cached.
        super(subject, true);

        ArrayList<F> buildFactList = new ArrayList<F>();
        if( simpleList != null ) {
            for( F next : simpleList ) {
                buildFactList.add(next);
            }
        }
        
        this.facts = buildFactList;
        for (CorrespondenceFact object: facts) {
            this.ids.add(object.getId());
        }
        
        subject.attachPredecessorSet(role.getMemento(), this);
	}
	
	public PredecessorList(CorrespondenceFact subject, Role role, FactMemento factMemento) throws CorrespondenceException {
		// When creating a predecessor with a memento, it is not cached.
		super(subject, false);
		
		// Get the predecessors from the model by the IDs in the memento.
		for (FactID id: factMemento.getPredecessorIdsByRole(role.getMemento())) {
			this.ids.add(id);
		}

		subject.attachPredecessorSet(role.getMemento(), this);
	}
	
	public synchronized Iterable<F> getFacts() {
		onGet();
		return facts;
	}

	public synchronized List<F> getFactList() {
		onGet();
		return facts;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void populateCache(Community community) {
		facts = new ArrayList<F>();
		for (FactID id: ids) {
			// Resolve each id to an object.
			F fact = (F)community.getFactById(id);
			if (fact != null) {
				facts.add(fact);
			}
		}
	}

	@Override
	protected void emptyCache() {
		facts = null;
	}

	@Override
	protected Iterable<FactID> getFactIDs() {
		return ids;
	}

	@Override
	protected Iterable<CorrespondenceFact> internalGetFacts() {
		// Cast each object to the base class type.
		return QuerySpec
			.from(getFacts())
			.select(new Selector<F, CorrespondenceFact>() {
				public CorrespondenceFact select(F row) {
					return row;
				}
			});
	}

}
