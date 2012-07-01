package com.facetedworlds.factual.parsetree;

public class FactClause extends FileLocation {

	/**
	 * 	Order* ordersPendingInvoice {
	 * 		Order o :  o.company =  this 
	 * 			 where [alias]o.[memberName]isPendingInvoice
	 *  } 
	 *  
	 * 	bool myPrediate {
	 * 		exists otherSuccessor o : this = o.otherSuccessorPredecessor
	 *			where [existenceModifier]not [alias]o.[memberName]otherPredicate
	 *	}
	 */
	private static final long serialVersionUID = -4297184758853352361L;
	private ConditionModifier conditionModifier = ConditionModifier.POSITIVE;
	private String alias;
	private String memberName;
	
	public FactClause(String alias, String memberName, ConditionModifier conditionModifier , int lineNumber, int columnNumber) {
		super(lineNumber, columnNumber);
		this.conditionModifier = conditionModifier;
		this.alias = alias;
		this.memberName = memberName;
	}

	public ConditionModifier getConditionModifier() {
		return conditionModifier;
	}

	public String getAlias() {
		return alias;
	}

	public String getMemberName() {
		return memberName;
	}
}
