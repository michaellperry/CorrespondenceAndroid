package com.facetedworlds.factual.compiler.codegen.java;

import com.facetedworlds.factual.parsetree.FactCardinality;
import com.facetedworlds.factual.parsetree.FactMember;
import com.facetedworlds.factual.parsetree.FactMemberComplex;
import com.facetedworlds.factual.parsetree.FactMemberPredicate;
import com.facetedworlds.factual.parsetree.FactMemberPrimitive;
import com.facetedworlds.factual.parsetree.FactMemberQuery;

public class ParseTreeToJava {

	private final static JavaIdentifierFormatting idFormatter = new JavaIdentifierFormatting();
	
	public static String factMemberToJavaType( FactMember factMember ) {
		
		if( factMember == null ) {
			throw new IllegalArgumentException( "factMember may not be null." );
		}
		
		if( factMember instanceof FactMemberPrimitive ) {
			return ((FactMemberPrimitive) factMember).getPrimitiveDataType().getJavaDataType( factMember.getCardinality() );
		}
		
		if( factMember instanceof FactMemberComplex ) {
			String javaFactTypeName = idFormatter.formatClassname(((FactMemberComplex) factMember).getFactType());
			String predecessorType = "PredecessorObj";
			
			FactCardinality cardinality = ((FactMember) factMember).getCardinality();
			if( cardinality == FactCardinality.MANY ) {
				predecessorType = "PredecessorList";
			}
			else if( cardinality == FactCardinality.OPTIONAL ) {
				predecessorType = "PredecessorOpt";
			}
			
			return predecessorType + "<" + javaFactTypeName + ">";
		}
		
		if( factMember instanceof FactMemberPredicate ) {
			return "boolean";
		}
		
		if( factMember instanceof FactMemberQuery ) {
			return String.format( "java.util.List<%1$s>" , idFormatter.formatClassname(((FactMemberQuery) factMember).getFactType()));
		}
		

		throw new IllegalArgumentException( "factMember must be a primitive or a complex type." ); 
	}
	
	public static String factMemberToJavaObjectType( FactMember factMember ) {
		
		if( factMember == null ) {
			throw new IllegalArgumentException( "factMember may not be null." );
		}
		
		if( factMember instanceof FactMemberPrimitive ) {
			return ((FactMemberPrimitive) factMember).getPrimitiveDataType().getJavaObjectDataType( factMember.getCardinality() );
		}
		
		if( factMember instanceof FactMemberComplex ) {
			String javaFactTypeName = idFormatter.formatClassname(((FactMemberComplex) factMember).getFactType());
			String predecessorType = "PredecessorObj";
			
			FactCardinality cardinality = ((FactMember) factMember).getCardinality();
			if( cardinality == FactCardinality.MANY ) {
				predecessorType = "PredecessorList";
			}
			else if( cardinality == FactCardinality.OPTIONAL ) {
				predecessorType = "PredecessorOpt";
			}
			
			return predecessorType + "<" + javaFactTypeName + ">";
		}
		
		if( factMember instanceof FactMemberPredicate ) {
			return "boolean";
		}
		
		if( factMember instanceof FactMemberQuery ) {
			return String.format( "java.util.List<%1$s>" , idFormatter.formatClassname(((FactMemberQuery) factMember).getFactType()));
		}
		

		throw new IllegalArgumentException( "factMember must be a primitive or a complex type." ); 
	}
}
