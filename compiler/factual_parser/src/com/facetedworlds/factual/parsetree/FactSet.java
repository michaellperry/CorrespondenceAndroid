package com.facetedworlds.factual.parsetree;

/**
 * 	Order* ordersPendingInvoice {
 * 		[factName]Order [alias]o : [leftIdentifier]o.company = [rightIdentifier]this 
 * 			[condition{]where o.isPendingInvoice[}]
 *  } 
 *  
 * 	bool myPrediate {
 * 		exists [factName]otherSuccessor [alias]o : [leftIdentiifer]this = [rightIdentifier]o.otherSuccessorPredecessor
 *			[factCondition]where not o.otherPredicate
 *	}
 */
public class FactSet extends FileLocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2282808820280765167L;
	private String alias;
	private String factType;
	private CompoundIdentifier leftIdentifier;
	private CompoundIdentifier rightIdentifier;
	private FactSetCondition condition;
	
	public FactSet(String alias,
			String factName, CompoundIdentifier leftIdentifier,
			CompoundIdentifier rightIdentifier,int lineNumber, int columnNumber) {
		super(lineNumber, columnNumber);
		this.alias = alias;
		this.factType = factName;
		this.leftIdentifier = leftIdentifier;
		this.rightIdentifier = rightIdentifier;
		this.condition = new FactSetCondition(lineNumber, columnNumber);
	}

	public String getAlias() {
		return alias;
	}

	public String getFactType() {
		return factType;
	}

	public CompoundIdentifier getLeftIdentifier() {
		return leftIdentifier;
	}

	public CompoundIdentifier getRightIdentifier() {
		return rightIdentifier;
	}

	public FactSetCondition getCondition() {
		return condition;
	}

	public void swapIdentifiers() {
		CompoundIdentifier temp = leftIdentifier;
		leftIdentifier = rightIdentifier;
		rightIdentifier = temp;
	}
	
	@Override
	public String toString() {
		return "FactSet [alias=" + alias + ", factType=" + factType
				+ ", leftIdentifier=" + leftIdentifier + ", rightIdentifier="
				+ rightIdentifier + ", condition=" + condition + "]";
	}
}
