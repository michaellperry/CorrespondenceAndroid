package com.facetedworlds.rhineland.common.cmdline;

import java.util.Map;

public class IntegerElementDefinition extends AbstractElementDefinition<Integer> {

	public IntegerElementDefinition(String flagCharacters, String parameterName,
			Integer defaultValue, String usageText) {
		super(flagCharacters, parameterName, defaultValue, usageText);
	}

	@Override
	public void applyElement(CommandLineIterator it, Map<String, Object> context) throws CommandLineParserException {
		
		String parameterValue = it.at(1);
		
		int parameterValueInt = defaultValue == null ? 0 : defaultValue.intValue();
		try {
			parameterValueInt = Integer.parseInt(parameterValue);
		}
		catch( Throwable t ) {
			throw new CommandLineParserException( "Unable to parse integer parameter for option '" + flagString + "'." ); 
		}
		
		context.put( parameterName, Integer.valueOf(parameterValueInt) );
		
		// consume the flag and the string value
		it.next(2);
	}

}
