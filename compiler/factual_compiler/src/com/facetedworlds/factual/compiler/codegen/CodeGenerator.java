package com.facetedworlds.factual.compiler.codegen;

import com.facetedworlds.factual.compiler.FactCompileSpace;
import com.facetedworlds.rhineland.common.cmdline.CommandLineParser;

public interface CodeGenerator {

	boolean generateModelCode( CommandLineParser commandLineParser , FactCompileSpace compileSpace , CodeGeneratorWorkspace context );
}
