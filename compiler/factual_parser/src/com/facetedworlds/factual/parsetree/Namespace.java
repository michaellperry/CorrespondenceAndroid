package com.facetedworlds.factual.parsetree;


public class Namespace extends FileLocation {

	@Override
	public String toString() {
		return "Namespace [identifier=" + identifier + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3037675024108039790L;
	private String identifier;
	
	public Namespace(String identifier, int lineNumber, int columnNumber) {
		super(lineNumber, columnNumber);
		
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
