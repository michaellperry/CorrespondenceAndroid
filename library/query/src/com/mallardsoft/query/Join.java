package com.mallardsoft.query;

public class Join<FIRST, SECOND> {

	private FIRST first;
	private SECOND second;
	
	public Join(FIRST first, SECOND second) {
		this.first = first;
		this.second = second;
	}

	public FIRST getFirst() {
		return first;
	}
	
	public SECOND getSecond() {
		return second;
	}
	
}
