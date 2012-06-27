package com.updatecontrols.correspondence.memento;

public class GetResultMemento {

	private FactTreeMemento factTree;
	private TimestampID timestamp;
	
	public GetResultMemento(FactTreeMemento factTree, TimestampID timestamp) {
		super();
		this.factTree = factTree;
		this.timestamp = timestamp;
	}

	public FactTreeMemento getFactTree() {
		return factTree;
	}

	public TimestampID getTimestamp() {
		return timestamp;
	}
}
