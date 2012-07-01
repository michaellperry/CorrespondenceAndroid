package com.facetedworlds.factual.compiler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.facetedworlds.factual.compiler.codegen.CodeGeneratorWorkspace;
import com.facetedworlds.factual.compiler.codegen.java.JavaCodeGenerator;
import com.facetedworlds.factual.compiler.manifest.FactualManifest;
import com.facetedworlds.factual.compiler.manifest.URLUtilities;
import com.facetedworlds.factual.parser.FactualLexer;
import com.facetedworlds.factual.parser.FactualParser;
import com.facetedworlds.factual.parsetree.FactFile;
import com.facetedworlds.rhineland.common.cmdline.CommandLineParserException;

public class FactualCompiler {

	private static final String FACT_COMPILER_VERSION = "0.1.0.0 Alpha";
	private static final String FACT_COMPILER_TITLE = "Factual Language Compiler";
	private static final String FACT_COMPILER_COPYRIGHT = "(c)Copyright 2008-2011 Michael L. Perry, Faceted Worlds, LLC\nAll Rights Reserved"; 
	
	private FactualCommandLineParser commandLineParser;
	private FactFileVerifier verifier;
	private ArrayList<URL> includedFactFiles;
	
	private FactCompileSpace compileSpace;
	
	public FactualCompiler() {
		
		// Reset the compiler's internal state 
		reset();
	}
	
	public int compile( String[] args ) {
		
		try {
			System.out.println();
			System.out.println( FACT_COMPILER_TITLE + " Version " + FACT_COMPILER_VERSION );
			System.out.println( FACT_COMPILER_COPYRIGHT );
			System.out.println();
			
			if( args == null || args.length == 0 ) {
				commandLineParser.usage();
				return 2;
			}
			
			// Parse the incoming command line
			if( commandLineParser.parse(args) == false ) {
				return 1; // didn't pass the "verifyCommandLine" method.
			}
			
			// Parse the include listing and ensure that all of the referenced files and directories are present.
			String includedManifests = commandLineParser.getParameterValueString(CompilerContextVariables.FACT_INCLUDE_MANIFESTS);
			if( includedManifests != null ) {
				if( generateFullIncludeListing(includedManifests) == false ) {
					return 1;
				}
			}
			
			// Compile all of the referenced include files.
			if( parseAllIncludeFiles() == false ) {
				return 1;
			}
			
			// Compile the target manifest
			if( parseTargetManifest() == false ) {
				return 1;
			}
			
			// Verify all includes
			if( verifyAllFactFiles() == false ) {
				return 1;
			}
			
			// Generate code for the target fact files...
			if( generateCodeForFactFiles() == false ) {
				return 1;
			}
		}
		catch( CommandLineParserException e ) {
			System.out.println( e.getMessage() );
			return 1;
		}
		
		return 0;
	}
	
	public void reset() {
		verifier = new FactFileVerifier();
		includedFactFiles = new ArrayList<URL>();
		compileSpace = new FactCompileSpace();
		commandLineParser = new FactualCommandLineParser();
	}
	
	public FactFileVerifier getFactFileVerifier() {
		return this.verifier;
	}
	
	private boolean generateFullIncludeListing(String includeListing) {
		boolean error = false;

		System.out.println( "Parsing included fact modules..." );
		
		String[] includedManifests = includeListing.split( File.pathSeparator );
		for( String nextManifestString : includedManifests ) {
			
			URL nextManifestURL = URLUtilities.createURL(nextManifestString);
			System.out.println( "    Parsing fact manifest: " + nextManifestURL.toExternalForm() );
			try {
				FactualManifest nextManifest = FactualManifest.loadManifest(nextManifestURL);

				for( URL nextFactFileURL : nextManifest.getFactFileIterable() ) {
					System.out.println( "        Parsing fact file: " + nextFactFileURL.toExternalForm() );
					
					includedFactFiles.add( nextFactFileURL );
				}
			}
			catch( IOException e ) {
				System.out.println( "Unable to read manifest file: " + nextManifestURL.toExternalForm() );
				error = true;
				break;
			}
		}
		if( error ) {
			return false;
		}
		
		return true;
	}
	
	protected boolean parseAllIncludeFiles() {
		
		boolean error = false;
		for( URL nextIncludeFile : includedFactFiles ) {
			FactFile parsedFactFile = parseFactFile( nextIncludeFile );
			if( parsedFactFile == null ) {
				error = true;
			}
			else {
				compileSpace.addIncludedFactFile(parsedFactFile);
			}
		}
		
		return !error;
	}
	
	protected FactFile parseFactFile( URL nextIncludeFile ) {
		
		try {
			String factFileData = URLUtilities.loadURLData(nextIncludeFile);
			
			FactualLexer lexer = new FactualLexer( new ANTLRStringStream(factFileData) );
			FactualParser parser = new FactualParser( new CommonTokenStream( lexer ) );
			parser.setParserFilename(nextIncludeFile.toExternalForm());
			
			parser.parseFactual();
			
			parser.getFactFile().setFactFileURL(nextIncludeFile);
			
			// If there are syntactic errors...return false;
			if( parser.getNumberOfSyntaxErrors() > 0 ) {
				return null;
			}
			
			return parser.getFactFile();
		}
		catch( IOException e ) {
			System.out.println( nextIncludeFile.toExternalForm() + ": IO Exception: " + e.getMessage() );
			return null;
		} catch (RecognitionException e) {
			System.out.println( nextIncludeFile.toExternalForm() + ": Recognition Exception: " + e.getMessage() );
			return null;
		}
	}
	
	public boolean parseTargetManifest() {

		String targetManifest = commandLineParser.getParameterValueString(CompilerContextVariables.TARGET_FACT_MANIFEST);
		URL targetManifestURL = URLUtilities.createURL(targetManifest);

		if( targetManifestURL == null ) {
			System.out.println( "Invalid target fact manifest file: " + targetManifest );
			return false;
		}
		
		FactualManifest targetManifestObject = null;
		try {
			targetManifestObject = FactualManifest.loadManifest(targetManifestURL);
		} catch (IOException e) {
			System.out.println( "Unable to read target manifest file: " + targetManifest );
			return false;
		}
		
		System.out.println( "Parsing fact module: " + targetManifestURL.toExternalForm() );
		
		boolean error = false;
		for( URL nextFactFileTaretURL : targetManifestObject.getFactFileIterable() ) {

			System.out.println( "    Parsing fact file: " + nextFactFileTaretURL.toExternalForm() );
			
			FactFile targetFactFile = parseFactFile(nextFactFileTaretURL);
			if( targetFactFile == null ) {
				error = true;
			}
			else {
				compileSpace.addTargetFactFile(targetFactFile);
			}
		}
		
		if( error == true ) {
			return false;
		}
		
		return true;
	}
	
	public FactCompileSpace getCompileSpace() { 
		return compileSpace;
	}
	
	public boolean verifyAllFactFiles() {

		for( FactFile nextFactFile : compileSpace.getIncludedFactFiles() ) {
			verifier.setCurrentFactFilename(nextFactFile.getFactFileURL().toExternalForm());
			verifier.verify(compileSpace,nextFactFile);
		}
		
		for( FactFile nextFactFile : compileSpace.getTargetFactFiles() ) {
			verifier.setCurrentFactFilename(nextFactFile.getFactFileURL().toExternalForm());
			verifier.verify(compileSpace,nextFactFile);
		}
		
		return verifier.getErrorCount() == 0; 
	}
	
	// TODO fix all the public methods...they should be protected
	protected boolean generateCodeForFactFiles() {
		
		boolean outputGenerated = false;
		
		// Is Java output requested?
		if( (commandLineParser.getParameterValueBoolean( CompilerContextVariables.JAVA_OUTPUT_FLAG)) == true ) {
			outputGenerated = true;	
			
			if( generateJavaOutput() == false ) {
				return false;
			}
		}
		
		if( outputGenerated == false ) {
			System.out.println();
			System.out.println( "No code output type was specified.  No model code was generated." );
		}
		
		return true;
	}
	
	public boolean generateJavaOutput() {
		
		// Create a Java code generator
		JavaCodeGenerator javaCodeGenerator = new JavaCodeGenerator();
		return javaCodeGenerator.generateModelCode(commandLineParser, compileSpace, new CodeGeneratorWorkspace());
	}
}