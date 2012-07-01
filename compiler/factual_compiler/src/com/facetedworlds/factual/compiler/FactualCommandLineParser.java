package com.facetedworlds.factual.compiler;

import com.facetedworlds.rhineland.common.cmdline.CommandLineParser;
import com.facetedworlds.rhineland.common.cmdline.FlagElementDefinition;
import com.facetedworlds.rhineland.common.cmdline.StringElementDefinition;

public class FactualCommandLineParser extends CommandLineParser {

	public FactualCommandLineParser() {
		super( "java -jar factual_compiler.jar [-i includeURLList] -t targetFactManifestURL" );
		
		addElementDefinition( new FlagElementDefinition( "java", 
				CompilerContextVariables.JAVA_OUTPUT_FLAG, Boolean.FALSE, 
				"Indicate that the Factual compiler should output Java(tm) model code." ) );
		
		addElementDefinition( new StringElementDefinition( "jao", 
				CompilerContextVariables.JAVA_OUTPUT_BASE_DIRECTORY, null ,
				"Indicate that base directory for Java(tm) or Android(tm) output." ) );
		
		addElementDefinition( new StringElementDefinition( "facts", 
				CompilerContextVariables.TARGET_FACT_MANIFEST, null,
				"Used to specify the target fact manifest to be compiled." ) );

		addElementDefinition( new StringElementDefinition( "includes", 
				CompilerContextVariables.FACT_INCLUDE_MANIFESTS, null, 
				"Used to specify the list of files or URLs of the fact manifest modules that you wish to include." ));

	}
	
	@Override
	protected boolean verifyCommandLine() {
		
		// There must be an include path specified
		String includeListing = getParameterValueString(CompilerContextVariables.FACT_INCLUDE_MANIFESTS);
		if( includeListing != null && includeListing.isEmpty() ) {
			System.out.println( "Please include a valid fact file include path using the '-i' command line option." );
			return false;
		}
		
		// Make sure a manifest was entered to be compiled
		String targetManifest = getParameterValueString(CompilerContextVariables.TARGET_FACT_MANIFEST);
		if( targetManifest == null ) {
			System.out.println( "Please include a target fact file manifest to be compiled using the '-t' command line option." );
			return false;
		}
		
		return true;
	}
}
