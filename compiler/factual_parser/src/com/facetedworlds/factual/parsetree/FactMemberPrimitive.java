package com.facetedworlds.factual.parsetree;

public class FactMemberPrimitive extends FactMemberPublishable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3177301994025854132L;
	private PrimitiveType primitiveDataType;
	
	public FactMemberPrimitive(FactType owningFact,String identifier, FactMemberSection section, boolean published, PrimitiveType nativeDataType , FactCardinality cardinality , int lineNumber, int columnNumber) {
		super(owningFact,identifier, section, published, cardinality , lineNumber, columnNumber);
		this.primitiveDataType = nativeDataType;
		
		if( section == FactMemberSection.QUERY ) {
			throw new IllegalStateException( "A 'FactMemberPrimitive' cannot be defined in the query section of a fact." );
		}
	}

	public PrimitiveType getPrimitiveDataType() {
		return primitiveDataType;
	}

    @Override
    public String toString() {
        return "FactMemberPrimitive [primitiveDataType=" + primitiveDataType + ", published=" + published + ", cardinality=" + cardinality
                + "]";
    }
}
