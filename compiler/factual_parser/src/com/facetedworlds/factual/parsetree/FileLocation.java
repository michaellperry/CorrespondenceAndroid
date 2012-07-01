package com.facetedworlds.factual.parsetree;

import java.io.Serializable;

public class FileLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1199418782208898763L;
	private int lineNumber;
	private int columnNumber;
	
	public FileLocation() {
		this.lineNumber = this.columnNumber = 1;
	}
	
	public FileLocation( int lineNumber , int columnNumber ) { 
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}
}
