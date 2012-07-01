package com.facetedworlds.factual.parsetree;

import java.util.ArrayList;
import java.util.Collections;

public class CompoundIdentifier extends FileLocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3534785614930350947L;
	private boolean relativeToThis; // starts with "this."  !absolute then it begins with an alias
	private ArrayList<String> identifierParts = new ArrayList<String>();
	
	public CompoundIdentifier( String dottedIdentifier , int lineNumber, int columnNumber ) {
		super( lineNumber, columnNumber );
		
		String[] parts = dottedIdentifier.split("\\.");
		for( String nextPart : parts ) {
			identifierParts.add( nextPart );
		}
		
		relativeToThis = false;
		if( parts[0].compareTo("this") == 0) {
			relativeToThis = true;
		}
	}
	
	public boolean isComplex() {
		return identifierParts.size() >= 3;
	}
	
	public boolean isRelativeToThis() {
		return relativeToThis;
	}
	
	public boolean isThisOnly() {
		return relativeToThis && identifierParts.size() == 1;
	}

	public Iterable<String> getIdentifierPartIterable() {
		return Collections.unmodifiableList( identifierParts );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for( String nextPart : getIdentifierPartIterable() ) {
			
			if( sb.length() > 0 ) {
				sb.append( "." );
			}
			sb.append( nextPart );
		}
		return sb.toString();
	}
	
	public String toString( int startOffset ) {
		StringBuilder sb = new StringBuilder();
		
		int count = 0;
		for( String nextPart : getIdentifierPartIterable() ) {
			if( count >= startOffset ) {
			
				if( sb.length() > 0 ) {
					sb.append( "." );
				}
				sb.append( nextPart );
			}
			++count;
		}
		return sb.toString();
	}
	
	public CompoundIdentifier createSubId( int offset , int len ) {
		
		StringBuilder sb = new StringBuilder();
		
		for( int i = offset ; i < (offset+len) ; i++ ) {
			
			if( sb.length() > 0 ) {
				sb.append( "." );
			}
			sb.append( getIdentifierPart(i));
		}
		return new CompoundIdentifier(sb.toString(), this.getLineNumber(), this.getColumnNumber());
	}
	
	public String getIdentifierPart( int offset ) {
		return this.identifierParts.get(offset);
	}
	
	public int getIdentifierPartCount() {
		return this.identifierParts.size();
	}
}
