package com.facetedworlds.rhineland.common.cmdline;

public class CommandLineParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2716501399844342102L;

	public CommandLineParserException() {
	}

	public CommandLineParserException(String message) {
		super(message);
	}

	public CommandLineParserException(Throwable cause) {
		super(cause);
	}

	public CommandLineParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
