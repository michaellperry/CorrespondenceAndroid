package com.facetedworlds.factual.parsetree;

public class FactMemberComplex extends FactMemberPublishable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 8215829001152589280L;
	private String factType;
	
	public FactMemberComplex(FactType owningFact, String factType , String identifier, FactMemberSection section , boolean published , 
			FactCardinality cardinality , int lineNumber, int columnNumber) {
		super(owningFact, identifier, section, published, cardinality , lineNumber, columnNumber);
		this.factType = factType;
		this.cardinality = cardinality;
	}

	public String getFactType() {
		return factType;
	}

    @Override
    public String toString() {
        return "FactMemberComplex [factType=" + factType + ", published=" + published + ", cardinality=" + cardinality + "]";
    }
}
