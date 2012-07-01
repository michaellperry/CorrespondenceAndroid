package com.facetedworlds.factual.parsetree;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import com.facetedworlds.factual.parser.FactualParserException;

public class FactType extends FileLocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2429684073542604790L;
	private String factType;
	private FactOptionSet factOptionSet = new FactOptionSet();
	
	private LinkedHashMap<String,FactMember> members = new LinkedHashMap<String, FactMember>();
	
	public FactType(String identifier , int lineNumber, int columnNumber) {
		super(lineNumber,columnNumber);
		
		this.factType = identifier;
	}
	
	public FactOptionSet getFactOptionSet() {
		return factOptionSet;
	}

	public String getFactType() {
		return factType;
	}

	public void addFactMember( FactMember newFactMember ) throws FactualParserException {
		
		// Check for duplicate fact members names.
		if( members.containsKey(newFactMember.getIdentifier()) && members.get( newFactMember.getIdentifier() ).getSection() != FactMemberSection.MUTABLE  ) {
			throw new FactualParserException(newFactMember, "The fact member '" + newFactMember.getIdentifier() + "' is already defined" );
		}
		
		members.put( newFactMember.getIdentifier() , newFactMember );
	}
	
	public FactMember getLastFactMember() {
		FactMember[] factArray = members.values().toArray( new FactMember[ members.values().size() ] );
		return factArray[ factArray.length - 1 ];
	}
	
	public Collection<FactMember> getFactMembers() {
		return Collections.unmodifiableCollection( members.values() );
	}
	
	public int getFactMemberCount() {
		return members.size();
	}
	
	public FactMember getFactMemberByName( String factName ) {
		for( FactMember nextFactMember : getFactMembers() ) {
			if( nextFactMember.getIdentifier().compareTo( factName ) == 0 ) {
				return nextFactMember;
			}
		}
		
		return null;
	}

	public boolean hasMembersInSection( FactMemberSection section ) {
		for( FactMember nextFactMember : getFactMembers() ) {
			if( nextFactMember.getSection() == section ) 
				return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Fact [factType=" + factType + ", factOptionSet="
				+ factOptionSet + ", members=" + members + "]";
	}
}
