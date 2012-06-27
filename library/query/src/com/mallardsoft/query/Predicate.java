package com.mallardsoft.query;

public interface Predicate<ROW> {

	boolean where(ROW row);
	
}
