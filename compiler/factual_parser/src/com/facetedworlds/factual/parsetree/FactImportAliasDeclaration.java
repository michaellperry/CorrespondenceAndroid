package com.facetedworlds.factual.parsetree;

public class FactImportAliasDeclaration extends FileLocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6662901437314295418L;
	private String originalFactType;
	private String localFactType;
	
	
	public FactImportAliasDeclaration(String originalFactType,
			String localFactType, int lineNumber , int columnNumber ) {
		super( lineNumber , columnNumber );
		this.originalFactType = originalFactType;
		this.localFactType = localFactType;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getOriginalFactType() {
		return originalFactType;
	}


	public String getLocalFactType() {
		return localFactType;
	}
}
