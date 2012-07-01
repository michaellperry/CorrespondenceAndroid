package com.facetedworlds.factual.parsetree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FactOptionSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7665892977747370417L;
	private ArrayList<FactOption> options = new ArrayList<FactOption>();
	
	public void addOption( FactOption f ) {
		options.add(f);
	}
	
	public boolean hasOption( FactOption f ) {
		return options.contains(f);
	}
	
	public Collection<FactOption> getFactOptions() {
		return Collections.unmodifiableCollection(options);
	}
}
