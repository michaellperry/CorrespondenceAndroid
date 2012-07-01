package com.facetedworlds.rhineland.common.cmdline;

import java.util.Map;

public class StringElementDefinition extends AbstractElementDefinition<String> {

	public StringElementDefinition(String flagCharacters, String parameterName,
			String defaultValue, String usageText) {
		super(flagCharacters, parameterName, defaultValue, usageText);
	}

	@Override
	public void applyElement(CommandLineIterator it, Map<String, Object> context) {
		
		String parameterValue = it.at(1);
		context.put( parameterName, parameterValue);
		
		// consume the flag and the string value
		it.next(2);
	}
}
