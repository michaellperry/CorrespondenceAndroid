package com.facetedworlds.rhineland.common.cmdline;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandLineParser {

	private ArrayList<CommandLineElementDefinition> definitions = new ArrayList<CommandLineElementDefinition>();
	private HashMap<String, Object> context = new HashMap<String, Object>();
	private String shortCommandLine;
	
	public CommandLineParser() {
		this.shortCommandLine = "";
	}
	
	public CommandLineParser( String shortCommandLine ) {
		this.shortCommandLine = shortCommandLine;
	}
	
	public void reset() {
		context.clear();
	}
	
	public void usage() {
		System.out.println();
		System.out.println("Usage:");
		System.out.println( "    " + this.shortCommandLine );
		System.out.println();
		for( CommandLineElementDefinition ed : definitions ) {
			System.out.println( "    " + ed.getUsageText() );
		}
	}
	
	public void addElementDefinition( CommandLineElementDefinition def ) {
		definitions.add(def);
	}
	
	public boolean parse( String[] args ) throws CommandLineParserException {

		reset();
		
		for( CommandLineElementDefinition ed : definitions ) {
			ed.applyDefaults(context);
		}
		
		CommandLineIterator iterator = new CommandLineIterator(args);
		while( iterator.hasNext() ) {
			
			boolean matched = false;
			for( CommandLineElementDefinition ed : definitions ) {

				if( ed.isMatch(iterator) ) {
					ed.applyElement(iterator, context);
					matched = true;
					break;
				}
			}
			
			if( matched == false ) {
				throw new CommandLineParserException( "Unknown command or flag: " + iterator.at(0) );
			}
		}
		
		return verifyCommandLine();
	}
	
	protected boolean verifyCommandLine() {
		return true;
	}
	
	public Object getParameterValue( String parameterName ) {
		return context.get( parameterName );
	}
	
	public String getParameterValueString( String parameterName ) {
		Object t = context.get(parameterName);
		return t == null ? null : t.toString();
	}
	
	public int getParameterValueInt( String parameterName ) {
		Object t = context.get(parameterName);
		if( t == null ) {
			return 0;
		}

		if( t instanceof Number ) {
			return ((Number)t).intValue();
		}
		
		if( t instanceof String ) {
			return Integer.parseInt(t.toString());
		}
		
		return 0;
	}
	
	public float getParameterValueFloat( String parameterName ) {
		Object t = context.get(parameterName);
		if( t == null ) {
			return 0f;
		}

		if( t instanceof Number ) {
			return ((Number)t).floatValue();
		}
		
		if( t instanceof String ) {
			return Float.parseFloat(t.toString());
		}
		
		return 0;
	}
	
	public boolean getParameterValueBoolean( String parameterName ) {
		Object t = context.get(parameterName);
		if( t == null ) {
			return Boolean.FALSE;
		}
		
		if( t instanceof Boolean ) {
			return ((Boolean)t).booleanValue();
		}
		
		if( t instanceof Number ) {
			return ((Number)t).intValue() != 0;
		}
		
		return false;
	}
	
	public int getParameterCount() {
		return context.size();
	}
}
