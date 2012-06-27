package com.updatecontrols.correspondence.memento;

public class MessageMemento {

    private FactID pivotId;
    private FactID factId;
    
	public MessageMemento(FactID pivotId, FactID factId) {
		super();
		this.pivotId = pivotId;
		this.factId = factId;
	}

	public FactID getPivotId() {
		return pivotId;
	}

	public FactID getFactId() {
		return factId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((factId == null) ? 0 : factId.hashCode());
		result = prime * result + ((pivotId == null) ? 0 : pivotId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageMemento other = (MessageMemento) obj;
		if (factId == null) {
			if (other.factId != null)
				return false;
		} else if (!factId.equals(other.factId))
			return false;
		if (pivotId == null) {
			if (other.pivotId != null)
				return false;
		} else if (!pivotId.equals(other.pivotId))
			return false;
		return true;
	}
    
}
