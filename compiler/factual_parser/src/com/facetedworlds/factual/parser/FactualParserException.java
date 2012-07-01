package com.facetedworlds.factual.parser;

import com.facetedworlds.factual.parsetree.FileLocation;

public class FactualParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileLocation fileLocation;
	
	public FactualParserException() {
		fileLocation = new FileLocation();
	}

	public FactualParserException(String message) {
		super(message);
		fileLocation = new FileLocation();
	}

	public FactualParserException(Throwable cause) {
		super(cause);
		fileLocation = new FileLocation();
	}

	public FactualParserException(String message, Throwable cause) {
		super(message, cause);
		fileLocation = new FileLocation();
	}

	public FactualParserException( FileLocation fileLocation ) {
		this.fileLocation = fileLocation;
	}

	public FactualParserException(FileLocation fileLocation ,String message) {
		super(message);
		this.fileLocation = fileLocation;
	}

	public FactualParserException(FileLocation fileLocation , Throwable cause) {
		super(cause);
		this.fileLocation = fileLocation;
	}

	public FactualParserException(FileLocation fileLocation , String message, Throwable cause) {
		super(message, cause);
		this.fileLocation = fileLocation;
	}

	public FileLocation getFileLocation() {
		return this.fileLocation;
	}
}
