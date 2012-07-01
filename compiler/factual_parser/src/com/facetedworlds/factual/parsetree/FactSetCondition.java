package com.facetedworlds.factual.parsetree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FactSetCondition extends FileLocation {

	private static final long serialVersionUID = -5810452068561443529L;
	private ArrayList<FactClause> clauses = new ArrayList<FactClause>();
	
	public FactSetCondition(int lineNumber, int columnNumber) {
		super(lineNumber, columnNumber);
	}
	
	public void addFactClause( FactClause c ) {
		clauses.add(c);
	}
	
	public Collection<FactClause> getFactClauses() {
		return Collections.unmodifiableCollection(clauses);
	}
	
	public int getFactClauseCount() { 
		return clauses.size();
	}

	@Override
	public String toString() {
		return "FactSetCondition [clauses=" + clauses + "]";
	}
}
