package com.updatecontrols.correspondence.memento;

import java.util.*;
import com.mallardsoft.query.*;

public class FactTreeMemento {

	private long databaseId;
	private List<IdentifiedFactBase> facts = new ArrayList<IdentifiedFactBase>();

	public FactTreeMemento(long databaseId) {
		this.databaseId = databaseId;
	}

	public long getDatabaseId() {
		return databaseId;
	}
	
	public int getFactCount() {
		return facts.size();
	}

	public Iterable<IdentifiedFactBase> getFacts() {
		return facts;
	}
	
	public boolean contains(final FactID factId) {
		return QuerySpec.from(facts)
			.where(new Predicate<IdentifiedFactBase>() {
				public boolean where(IdentifiedFactBase row) {
					return row.getId().equals(factId);
				}
			})
			.any();
	}
	
	public void add(IdentifiedFactBase fact) {
		facts.add(fact);
	}

	public IdentifiedFactBase getFactById(final FactID factId) {
		return QuerySpec.from(facts)
			.where(new Predicate<IdentifiedFactBase>() {
				public boolean where(IdentifiedFactBase row) {
					return row.getId().equals(factId);
				}
			})
			.selectOne();
	}
}
