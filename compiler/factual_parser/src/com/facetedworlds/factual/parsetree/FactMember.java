package com.facetedworlds.factual.parsetree;

public class FactMember extends FileLocation {

	@Override
	public String toString() {
		return "FactMember [identifier=" + identifier + ", owningFact="
				+ owningFact + ", section=" + section + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6002364670005560002L;
	private String identifier;
	private FactType owningFact;
	private FactMemberSection section;
	protected FactCardinality cardinality;
	
	public FactMember(FactType owningFact , String identifier , FactMemberSection section , FactCardinality cardinality , int lineNumber, int columnNumber) {
		super(lineNumber, columnNumber);
		this.owningFact = owningFact;
		this.identifier = identifier;
		this.section = section;
		this.cardinality = cardinality;
	}

	public String getIdentifier() {
		return this.identifier;
	}
	
	public FactMemberSection getSection() {
		return this.section;
	}
	
	public FactType getOwningFact() {
		return this.owningFact;
	}

	public FactCardinality getCardinality() {
		 return cardinality;
	}
}
