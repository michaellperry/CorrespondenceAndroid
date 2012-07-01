package com.facetedworlds.factual.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.facetedworlds.factual.parsetree.FactFile;

public class FactCompileSpace {

	private ArrayList<FactFile> includedFactFiles = new ArrayList<FactFile>();
	private HashMap<String, FactFile> namespaceToFactFile = new HashMap<String, FactFile>();
	private ArrayList<FactFile> targetFactFiles = new ArrayList<FactFile>();
	
	public FactCompileSpace() {
		
	}
	
	public void addIncludedFactFile( FactFile factFile ) {
		includedFactFiles.add( factFile );
		namespaceToFactFile.put( factFile.getNamespace().getIdentifier(), factFile );
	}
	
	public int getDeclaredNamespaceCount() {
		return namespaceToFactFile.size();
	}
	
	public int getTargetFactFileCount() {
		return targetFactFiles.size();
	}
	
	public int getIncludedFactFileCount() {
		return includedFactFiles.size();
	}
	
	public void addTargetFactFile( FactFile targetFactFile ) {
		targetFactFiles.add(targetFactFile);
		namespaceToFactFile.put( targetFactFile.getNamespace().getIdentifier(), targetFactFile );
	}
	
	public Collection<FactFile> getIncludedFactFiles() {
		return includedFactFiles;
	}
	
	public Collection<FactFile> getTargetFactFiles() {
		return targetFactFiles;
	}
	
	public FactFile getFactFileByNamespace( String namespace ) {
		return namespaceToFactFile.get( namespace );
	}
	
}
