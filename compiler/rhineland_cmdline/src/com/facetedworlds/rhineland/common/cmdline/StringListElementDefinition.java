package com.facetedworlds.rhineland.common.cmdline;

import java.util.ArrayList;
import java.util.Map;

public class StringListElementDefinition extends AbstractElementDefinition<ArrayList<String>> {

	public StringListElementDefinition(	String parameterName, ArrayList<String> defaultValue,
			String usageText) {
		super("\0", parameterName, defaultValue, usageText);
	}

	@Override
	public boolean isMatch(CommandLineIterator it) {
		
		if( it.hasNext() == false )
			return false;
		
		if( it.isFlagAt(0) == false ) {
			return true;
		}
		
		return false;
	}


	@Override
	public void applyElement(CommandLineIterator it, Map<String, Object> context)
			throws CommandLineParserException {

		ArrayList<String> stringList = new ArrayList<String>();
		
		while( it.hasNext() ) {
			
			String nextParam = it.at(0);
			
			if( it.isFlagAt(0) == false ) {
				stringList.add( nextParam );
				it.next();
				continue;
			}
			
			break;
		}
		
		context.put( parameterName, stringList);
	}
}
