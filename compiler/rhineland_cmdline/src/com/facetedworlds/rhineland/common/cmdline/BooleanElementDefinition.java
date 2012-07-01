package com.facetedworlds.rhineland.common.cmdline;

import java.util.Map;

public class BooleanElementDefinition extends AbstractElementDefinition<Boolean> {

	public BooleanElementDefinition(String flagCharacters, String parameterName,
			Boolean defaultValue, String usageText) {
		super(flagCharacters, parameterName, defaultValue, usageText);
	}

	@Override
	public void applyElement(CommandLineIterator it, Map<String, Object> context)
			throws CommandLineParserException {
		
		String parameterValue = it.at(1);
		
		if( parameterValue.equals( "true" ) == false && parameterValue.equals("false" ) == false ) {
			throw new CommandLineParserException( "Unable to parse boolean parameter for option '" + flagString + "'." ); 
		}
		
		boolean parameterValueBoolean = defaultValue == null ? Boolean.FALSE : defaultValue.booleanValue();
		try {
			parameterValueBoolean = Boolean.valueOf(parameterValue);
		}
		catch( Throwable t ) {
			throw new CommandLineParserException( "Unable to parse boolean parameter for option '" + flagString + "'." ); 
		}
		
		context.put( parameterName, Boolean.valueOf(parameterValueBoolean) );
		
		// consume the flag and the string value
		it.next(2);
	}

}
