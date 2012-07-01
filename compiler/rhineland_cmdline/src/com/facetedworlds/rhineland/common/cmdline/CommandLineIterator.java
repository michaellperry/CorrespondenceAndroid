package com.facetedworlds.rhineland.common.cmdline;

import java.util.ArrayList;

public class CommandLineIterator {

	private ArrayList<String> commandList;
	private int pointer;
	
	public CommandLineIterator( String[] commandList ) {
		
		if( commandList == null ) {
			this.commandList = new ArrayList<String>(); 
		}
		else {
			this.commandList = new ArrayList<String>(commandList.length);
			for( String n : commandList ) {
				this.commandList.add(n);
			}
		}
		this.pointer = 0;
	}
	
	public boolean hasNext() {
		return hasNext(1);
	}
	
	public boolean hasNext( int count ) {
		return pointer + count - 1 < commandList.size();
	}
	
	public void next() {
		next(1);
	}
	
	public void next( int count ) {
		pointer += count;
		if( pointer > commandList.size() ) {
			throw new IllegalStateException( "Consumed too many command line elements." );
		}
	}
	
	public String at( int offset ) {
		return commandList.get( offset + pointer );
	}
	
	public boolean isFlagAt( int offset ) {
		
		String nextParam = at(offset);
		
		if( nextParam.startsWith("-") == true ) {
			
			// See if this is a negative number...
			int length = nextParam.length();
			boolean foundDigit = false;
			for( int i = 1 ; i < length ; i++ ) {
				
				if( Character.isDigit(nextParam.charAt(i)) == true ) {
					foundDigit = true;
				}
				else {
					foundDigit = false;
					break;
				}
			}
			
			return !foundDigit;
		}
		return false;
	}
}
