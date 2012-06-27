package com.updatecontrols.correspondence.memory;

import java.util.Date;

import com.updatecontrols.correspondence.memento.FactID;

public class DateIndexMemento {

	private FactID id;
	private String indexName;
	private Date value;
	
	public DateIndexMemento(FactID id, String indexName, Date value) {
		this.id = id;
		this.indexName = indexName;
		this.value = value;
	}

	public FactID getId() {
		return id;
	}

	public String getIndexName() {
		return indexName;
	}

	public Date getValue() {
		return value;
	}
	
}
