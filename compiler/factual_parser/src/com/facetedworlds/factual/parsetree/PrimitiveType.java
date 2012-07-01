package com.facetedworlds.factual.parsetree;

public enum PrimitiveType {

	BYTE( "byte" , "byte" , "Byte" ),
	STRING( "string" , "String" , "String" ),
	INT( "int" , "int" , "Integer" ),
	LONG( "long" , "long" , "Long" ),
	FLOAT( "float", "float" , "Float" ),
	DOUBLE( "double" , "double" , "Double" ),
	DECIMAL( "decimal" , "double" , "Double" ), // TODO Correct Type?  Fixed point data type - BigDecimal
	CHAR( "char" , "char" , "Character" ),
	DATE( "date" , "java.util.Date" , "java.util.Date" ), // date, no time
	TIME( "time" , "java.util.Date", "java.util.Date" ),  // timestamp
	BOOL( "bool" , "bool" , "Boolean" );
	
	private String correspondenceType;
	private String javaDataType;
	private String javaObjectDataType;
	
	PrimitiveType( String correspondenceType , String javaPrimitiveDataType , String javaObjectDataType ) {
	    this.correspondenceType = correspondenceType;
		this.javaDataType = javaPrimitiveDataType;
		this.javaObjectDataType = javaObjectDataType;
	}
	
	public String getCorrespondenceType() {
	    return correspondenceType;
	}
	
	public String getJavaDataType( FactCardinality f ) {

		switch( f ) {
			case ONE:
				return javaDataType;
			
			case OPTIONAL:
				return javaObjectDataType;
				
			case MANY:
				return "java.util.List<" + javaObjectDataType + ">";
		}
		
		throw new IllegalStateException();
	}
	
	public String getJavaObjectDataType( FactCardinality f ) {

		switch( f ) {
			case ONE:
				return javaObjectDataType;
			
			case OPTIONAL:
				return javaObjectDataType;
				
			case MANY:
				return "java.util.List<" + javaObjectDataType + ">";
		}
		
		throw new IllegalStateException();
	}
}
