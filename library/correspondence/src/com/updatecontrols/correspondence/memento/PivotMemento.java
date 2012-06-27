package com.updatecontrols.correspondence.memento;

public class PivotMemento {

	private FactID pivotId;
	private TimestampID timestamp;
	
	public PivotMemento(FactID pivotId, TimestampID timestamp) {
		super();
		this.pivotId = pivotId;
		this.timestamp = timestamp;
	}

	public FactID getPivotId() {
		return pivotId;
	}

	public TimestampID getTimestamp() {
		return timestamp;
	}
}
