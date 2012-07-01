package com.facetedworlds.factual.compiler.codegen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CodeGeneratorWorkspace {

	private int indentationLevel = 0;
	private boolean indentUsingTabs = true;
	private String lineEndingCharacters = "\n";
	
	private ArrayList<String> headerOutputLines = new ArrayList<String>();
	private ArrayList<String> bodyOutputLines = new ArrayList<String>();
	private ArrayList<String> trailerOutputLines = new ArrayList<String>();
	
	public CodeGeneratorWorkspace() {
	}

	public void indent() {
		++indentationLevel;
	}
	
	public void unindent() {
		
		if( indentationLevel == 0 ) {
			throw new IllegalStateException( "Indentation underflow in CodeGenerationContext" );
		}
		
		--indentationLevel;
	}
	
	public String indentString( int depth ) {
		
		StringBuilder sb = new StringBuilder();

		String indentString = "    ";
		if( indentUsingTabs ) {
			indentString = "\t";
		}
		
		for( int i = 0 ; i < depth ; i++ ) {
			sb.append( indentString );
		}
	
		return sb.toString();
	}
	
	private String generateIndentString() {
		
		return indentString(this.indentationLevel);
	}

	public void printHeader() {
		printHeader(null);
	}

	public void printHeader( String outputLine ) {
		
		if( outputLine == null || outputLine.length() == 0 ) {
			headerOutputLines.add( "" );
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( generateIndentString() );
		sb.append( outputLine );

		
		headerOutputLines.add( sb.toString() );
	}
	
	public void print() throws IOException {
		print( null );
	}
	
	public void print( String outputLine ) throws IOException {
		
		if( outputLine == null || outputLine.length() == 0 ) {
			bodyOutputLines.add( "" );
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( generateIndentString() );
		sb.append( outputLine );
		
		bodyOutputLines.add( sb.toString() );
	}

	public void printTrailer() {
		printTrailer(null);
	}

	public void printTrailer( String outputLine ) {
		
		if( outputLine == null || outputLine.length() == 0 ) {
			trailerOutputLines.add( "" );
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( generateIndentString() );
		sb.append( outputLine );
		
		trailerOutputLines.add( sb.toString() );
	}
	public boolean isIndentUsingTabs() {
		return indentUsingTabs;
	}

	public void setIndentUsingTabs(boolean indentUsingTabs) {
		this.indentUsingTabs = indentUsingTabs;
	}

	public String getLineEndingCharacters() {
		return lineEndingCharacters;
	}

	public void setLineEndingCharacters(String lineEndingCharacters) {
		this.lineEndingCharacters = lineEndingCharacters;
	}
	
	public void outputTo( PrintWriter writer ) throws IOException {
		
		for( String nextLine : headerOutputLines ) {
			writer.print( nextLine );
			writer.print( lineEndingCharacters );
		}
		for( String nextLine : bodyOutputLines ) {
			writer.print( nextLine );
			writer.print( lineEndingCharacters );
		}
		for( String nextLine : trailerOutputLines ) {
			writer.print( nextLine );
			writer.print( lineEndingCharacters );
		}
	}
	
	public void clearOutput() {
		headerOutputLines.clear();
		bodyOutputLines.clear();
		trailerOutputLines.clear();
	}
}
