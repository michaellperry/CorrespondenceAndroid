package com.facetedworlds.rhineland.common.cmdline;

import java.util.Map;

public abstract class AbstractElementDefinition<T> implements CommandLineElementDefinition {

	protected String flagString;
	protected String parameterName;
	protected T defaultValue;
	protected String usageText;

	public AbstractElementDefinition( String flagCharacters , String parameterName , T defaultValue , String usageText ) {
		this.flagString = "-" + flagCharacters;
		this.parameterName = parameterName;
		this.defaultValue = defaultValue;
		this.usageText = usageText;
	}

	@Override
	public String getUsageText() {
		return flagString + " " + usageText;
	}

	@Override
	public boolean isMatch(CommandLineIterator it) throws CommandLineParserException {
		if( it.hasNext(2) == false ) {
			return false;
		}
		
		String nextElement = it.at(0);
		if( flagString.compareTo(nextElement) == 0 ) {

			if( it.isFlagAt(1) ) {
				throw new CommandLineParserException( "Missing argument for parameter '" + flagString + "'." );
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public void applyDefaults(Map<String, Object> context) {
		context.put( parameterName, defaultValue );
	}

	@Override
	public abstract void applyElement(CommandLineIterator it, Map<String, Object> context) throws CommandLineParserException;
}
