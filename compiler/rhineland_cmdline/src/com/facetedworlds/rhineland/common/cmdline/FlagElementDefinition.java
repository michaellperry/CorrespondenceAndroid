package com.facetedworlds.rhineland.common.cmdline;

import java.util.Map;

public class FlagElementDefinition extends AbstractElementDefinition<Boolean> {
	
	public FlagElementDefinition(String flagCharacters, String parameterName,
			Boolean defaultValue, String usageText) {
		super(flagCharacters, parameterName, defaultValue, usageText);
	}

	@Override
	public boolean isMatch(CommandLineIterator it) {
		if( it.hasNext() == false ) {
			return false;
		}
		
		String nextElement = it.at(0);
		if( flagString.compareTo(nextElement) == 0 ) {
			return true;
		}
		
		return false;
	}

	@Override
	public void applyElement(CommandLineIterator it, Map<String,Object> context ) {
		
		// Consume the flag
		it.next();
		
		context.put( this.parameterName, Boolean.valueOf( !defaultValue ) );
	}
}
