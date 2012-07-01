package com.facetedworlds.factual.parsetree;

public class FactMemberPublishable extends FactMember {

    /**
     * 
     */
    private static final long serialVersionUID = 952573451449488125L;
    protected boolean published;

    public FactMemberPublishable(FactType owningFact, String identifier, FactMemberSection section, boolean published, FactCardinality cardinality,
            int lineNumber, int columnNumber) {
        super(owningFact, identifier, section, cardinality, lineNumber, columnNumber);
        this.published = published;
    }

    public boolean getPublished() {
        return published;
    }

    @Override
    public String toString() {
        return "FactMemberPublishable [published=" + published + ", cardinality=" + cardinality + "]";
    }
}
