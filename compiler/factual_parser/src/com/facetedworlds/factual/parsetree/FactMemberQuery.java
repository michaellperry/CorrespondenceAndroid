package com.facetedworlds.factual.parsetree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FactMemberQuery extends FactMember {

	/**
	 * 	[factName]Order* [identifier]ordersPendingInvoice {
	 * 		[factSet]Order o : o.company = this 
	 * 			where o.isPendingInvoice
	 *  } 
	 */
	private static final long serialVersionUID = -3113441622635046731L;
	private String factType;
	private ArrayList<FactSet> factSets = new ArrayList<FactSet>();
	private boolean generatedFromMutable;
	
	public FactMemberQuery(FactType owningFact, String factType,String identifier, int lineNumber, int columnNumber) {
		super(owningFact, identifier, FactMemberSection.QUERY , FactCardinality.ONE , lineNumber, columnNumber);
		this.factType = factType;
		this.generatedFromMutable = false;
	}

	public String getFactType() {
		return factType;
	}
	
	public Collection<FactSet> getFactSets() {
		return Collections.unmodifiableCollection(factSets);
	}
	
	public void addFactSet( FactSet f ) {
		factSets.add(f);
	}

	@Override
	public String toString() {
		return "FactMemberQuery [factType=" + factType + ", factSets="
				+ factSets + "]";
	}

    public boolean getGeneratedFromMutable() {
        return generatedFromMutable;
    }

    public void setGeneratedFromMutable(boolean generatedFromMutable) {
        this.generatedFromMutable = generatedFromMutable;
    }
}
