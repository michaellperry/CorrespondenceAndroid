package com.updatecontrols.correspondence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.updatecontrols.Independent;
import com.updatecontrols.correspondence.query.QueryDefinition;

public class Result<RESULT extends CorrespondenceFact> implements Iterable<RESULT> {

	private CorrespondenceFact fact;
	private QueryDefinition queryDefinition;
	
	private ArrayList<RESULT> results;
	private Independent indResults = new Independent();
	
	public Result(CorrespondenceFact fact, QueryDefinition queryDefinition) {
		this.fact = fact;
		this.queryDefinition = queryDefinition;
		
		fact.addQueryResult(queryDefinition, this);
	}
	
	public synchronized List<RESULT> getFactList() {
		// Someone has shown interest.
		indResults.onGet();
		loadResults();
		
		// Return a copy of the results.
		ArrayList<RESULT> returnList = new ArrayList<RESULT>();
		for (RESULT row: results) {
			returnList.add(row);
		}
		return returnList;
	}
	
	public Iterator<RESULT> iterator() {
		return getFactList().iterator();
	}

	public synchronized RESULT getFirst() {
		// Someone has shown interest.
		indResults.onGet();
		loadResults();
		
		if (results.isEmpty())
			return null;
		else
			return results.get(0);
	}

	public synchronized boolean isEmpty() {
		indResults.onGet();
		loadResults();

		return results.isEmpty();
	}

	@SuppressWarnings("unchecked")
	private void loadResults() {
		if (results == null) {
			// Load the set of successors from storage and cache them.
			results = new ArrayList<RESULT>();
			ArrayList<CorrespondenceFact> results = new ArrayList<CorrespondenceFact>();
			fact.getCommunity().executeQuery(results, fact.getId(), queryDefinition);
			for (CorrespondenceFact result: results) {
				this.results.add((RESULT)result);
			}
		}
	}
	
	public synchronized void invalidate() {
		indResults.onSet();
		results = null;
	}
}
