package com.facetedworlds.factual.compiler.codegen.java;

public class JavaIdentifierFormatting {

	public String formatNamespace( String namespace ) {
		
		String[] namespaceSections = namespace.split( "\\." );
		
		StringBuilder sb = new StringBuilder();
		for( String nextSection : namespaceSections ) {
			
			if( sb.length() > 0 ) {
				sb.append( "." );
			}
			
			sb.append( nextSection.toLowerCase() );
		}
		
		return sb.toString();
	}
	
	public String formatClassname( String classname ) {
		
		if( Character.isUpperCase( classname.charAt(0) ) ) {
			return classname;
		}
		
		return Character.toUpperCase( classname.charAt(0) ) + classname.substring(1);
	}
	
	public String formatIdentifier( String id ) {
		if( Character.isLowerCase( id.charAt(0) ) ) {
			return id;
		}
		
		return Character.toLowerCase( id.charAt(0) ) + id.substring(1);
	}
	
	public String formatIdentifierForGetterSetter( String id ) { 
		if( Character.isUpperCase( id.charAt(0) ) ) {
			return id;
		}
		
		return Character.toUpperCase( id.charAt(0) ) + id.substring(1);
	}
	
	public String formatMutableAssociationClass( String originalFactType , String associatedField ) {
	    
	    return formatClassname(originalFactType) + "__" + formatIdentifier( associatedField );
	}
}
