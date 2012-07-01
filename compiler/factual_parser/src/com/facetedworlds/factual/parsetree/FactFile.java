package com.facetedworlds.factual.parsetree;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FactFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8623983171290336889L;
	private URL factFileURL;
	private Namespace namespace;
	private ArrayList<FactImportDeclaration> factImportDeclarations = new ArrayList<FactImportDeclaration>();
	private ArrayList<FactType> facts = new ArrayList<FactType>();
	
	public FactFile() {
		
	}
	
	public void setFactFileURL( URL url ) {
		factFileURL = url;
	}
	
	public URL getFactFileURL() {
		return factFileURL;
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	public void setNamespace( Namespace namespace ) {
		
		if( this.namespace != null ) {
			throw new IllegalStateException( "Namespace has already been set." );
		}
		
		this.namespace = namespace;
	}
	
	public void addImportDeclaration( FactImportDeclaration decl ) {
		factImportDeclarations.add( decl );
	}
	
	public int getFactImportDeclarationCount() {
		return factImportDeclarations.size();
	}
	
	public Collection<FactImportDeclaration> getFactImportDeclarations() {
		return Collections.unmodifiableCollection(factImportDeclarations);
	}
	
	public Collection<FactType> getFacts() {
		return Collections.unmodifiableCollection(facts);
	}
	
	public void addFact( FactType f ) {
		facts.add( f );
	}
	
	public boolean doesFactTypeExist( String factType ) {
		
		for( FactType nextFact : facts ) {
			if( nextFact.getFactType().compareTo(factType) == 0 ) {
				return true;
			}
		}
		
		// Didn't find it defined locally...see if this has been aliased.
		for( FactImportDeclaration importDecl : getFactImportDeclarations() ) {
			
			for( FactImportAliasDeclaration aliasDecl : importDecl.getFactImportAliasDeclarations() ) {
				
				String importTarget = aliasDecl.getLocalFactType() == null ? aliasDecl.getOriginalFactType() : aliasDecl.getLocalFactType();
				if( factType.equals( importTarget ) ) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public FactType getFactTypeByName( String factType ) {
		
		for( FactType nextFact : facts ) {
			if( nextFact.getFactType().compareTo(factType) == 0 ) {
				return nextFact;
			}
		}
		
		return null;
	}
}
