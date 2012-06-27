package com.updatecontrols;

public class Independent extends Precedent {

	public void onGet() {
		// Establish dependency between the current update
		// and this field.
		recordDependent();
	}
	
	public void onSet() {
		// When an independent field changes,
		// its dependents become out-of-date.
		makeDependentsOutOfDate();
	}
}
