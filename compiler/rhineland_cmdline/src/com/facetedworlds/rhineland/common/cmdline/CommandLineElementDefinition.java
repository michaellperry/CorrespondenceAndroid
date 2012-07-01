package com.facetedworlds.rhineland.common.cmdline;

import java.util.Map;

public interface CommandLineElementDefinition {

	public String getUsageText();
	public boolean isMatch( CommandLineIterator it ) throws CommandLineParserException;
	public void applyDefaults( Map<String, Object> context );
	public void applyElement( CommandLineIterator it , Map<String,Object> context ) throws CommandLineParserException;
}
