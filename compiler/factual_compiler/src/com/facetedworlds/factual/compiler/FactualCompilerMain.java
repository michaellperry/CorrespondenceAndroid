package com.facetedworlds.factual.compiler;

public class FactualCompilerMain {

	public static void main(String[] args) {
		
		System.exit(run(args));
	}

	private static int run(String[] args) {
		try {
			// Create an instance of the FactualCompiler
			FactualCompiler fc = new FactualCompiler();
			
			return fc.compile(args); 
		}
		catch( Throwable t ) {
			t.printStackTrace();
			return 1;
		}
	}
}
