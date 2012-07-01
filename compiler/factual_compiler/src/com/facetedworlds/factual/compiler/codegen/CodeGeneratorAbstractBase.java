package com.facetedworlds.factual.compiler.codegen;

import com.facetedworlds.factual.compiler.FactCompileSpace;
import com.facetedworlds.factual.parsetree.FactFile;
import com.facetedworlds.factual.parsetree.FactType;
import com.facetedworlds.rhineland.common.cmdline.CommandLineParser;

public abstract class CodeGeneratorAbstractBase implements CodeGenerator {

	@Override
	public boolean generateModelCode(CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context ) {
		
		boolean returnValue = true;
		
		if( beforeCompileAll(commandLineParser, compileSpace, context) == false ) {
			return false;
		}

		for( FactFile nextFactFile : compileSpace.getTargetFactFiles() ) {
			
			if( beforeFactFileCompile(nextFactFile, commandLineParser, compileSpace, context) == false ) {
				returnValue = false;
				break;
			}

			if( compileFactFile(nextFactFile, commandLineParser, compileSpace, context) == false ) {
				return false;
			}
			
			if( afterFactFileCompile(nextFactFile, commandLineParser, compileSpace, context) == false ) {
				returnValue = false;
				break;
			}
		}
		
		if( afterCompileAll(commandLineParser, compileSpace, context) == false ) {
			returnValue = false;
		}
		
		return returnValue;
	}
	
	protected boolean beforeCompileAll( CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
		return true;
	}
	
	protected boolean beforeFactFileCompile( FactFile nextFactFile , CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
		return true;
	}

	protected boolean compileFactFile( FactFile nextFactFile , CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
		
		boolean returnValue = true;
		
		for( FactType nextFactType : nextFactFile.getFacts() ) {
			
			if( beforeFactTypeCompile(nextFactFile, nextFactType, commandLineParser, compileSpace, context ) == false ) {
				return false;
			}
			
			if( compileFact(nextFactFile, nextFactType, commandLineParser, compileSpace, context) == false ) {
				returnValue = false;
			}
			
			if( afterFactTypeCompile(nextFactFile, nextFactType, commandLineParser, compileSpace, context) == false ) {
				return false;
			}
		}
		
		return returnValue;
	}
	
	protected boolean beforeFactTypeCompile( FactFile nextFactFile , FactType nextFactType , CommandLineParser commandLineParser , FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
		return true;
	}
	
	protected abstract boolean compileFact(  FactFile nextFactFile , FactType nextFactType , CommandLineParser commandLineParser , FactCompileSpace compileSpace, CodeGeneratorWorkspace context  );
	
	protected boolean afterFactTypeCompile( FactFile nextFactFile , FactType nextFactType , CommandLineParser commandLineParser , FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
		context.clearOutput();
		return true;
	}
	
	protected boolean afterFactFileCompile( FactFile nextFactFile , CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
		return true;
	}
	
	protected boolean afterCompileAll( CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
		return true;
	}
}
