package com.facetedworlds.factual.parsetree;

/**
 * 
 * 	bool [identifier]myPrediate {
 * 		[existenceMod] exists otherSuccessor o : this = o.otherSuccessorPredecessor
 *			where not o.otherPredicate
 *	}
 *
 */
public class FactMemberPredicate extends FactMemberQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4074881101389914865L;
	private ExistenceModifier existenceModifier = ExistenceModifier.POSITIVE;
	
	public FactMemberPredicate(FactType owningFact, String identifier, int lineNumber, int columnNumber) {
		super(owningFact, "bool" , identifier , lineNumber , columnNumber );
		
	}

	public ExistenceModifier getExistenceModifier() {
		return existenceModifier;
	}

	public void setExistenceModifier(ExistenceModifier existenceModifier) {
		this.existenceModifier = existenceModifier;
	}
	
	@Override
	public String toString() {
		return "FactMemberPredicate [existenceModifier=" + existenceModifier
				+ "]";
	}
	
	public String determinePredicateReturnFactType() {
		
		FactSet firstFactSet = this.getFactSets().iterator().next();
		return firstFactSet.getFactType();
	}
}
