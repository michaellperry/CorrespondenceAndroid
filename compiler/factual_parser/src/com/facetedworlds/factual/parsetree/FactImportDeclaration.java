package com.facetedworlds.factual.parsetree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FactImportDeclaration extends FileLocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6217344963269809811L;
	private String namespace;
	private ArrayList<FactImportAliasDeclaration> factImportAliasDeclarations = new ArrayList<FactImportAliasDeclaration>();
	
	public FactImportDeclaration( String namespace , int lineNumber , int columnNumber ) {
		super( lineNumber , columnNumber );
		this.namespace = namespace;
	}
	
	public Collection<FactImportAliasDeclaration> getFactImportAliasDeclarations() {
		return Collections.unmodifiableCollection(factImportAliasDeclarations);
	}
	
	public int getFactImportAliasDeclarationCount() {
		return factImportAliasDeclarations.size();
	}
	
	public void addFactImportAliasDeclaration( FactImportAliasDeclaration d ) {
		factImportAliasDeclarations.add(d);
	}
	
	public String getNamespace() {
		return this.namespace;
	}
}
