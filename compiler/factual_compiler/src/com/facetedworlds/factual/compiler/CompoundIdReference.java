package com.facetedworlds.factual.compiler;

import java.io.Serializable;

import com.facetedworlds.factual.parsetree.FactMember;
import com.facetedworlds.factual.parsetree.FactType;

public class CompoundIdReference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5268810855940896985L;
	private FactType fact;
	private FactMember factMember;
	private CompoundIdReference parentReference;
	
	
	public CompoundIdReference() {
		this.fact = null;
		this.factMember = null;
		this.parentReference = null;
	}
	
	public CompoundIdReference(FactType fact, FactMember factMember, CompoundIdReference parentReference ) {
		super();
		this.fact = fact;
		this.factMember = factMember;
		this.parentReference = parentReference;
	}

	public CompoundIdReference(FactType fact , CompoundIdReference parentReference ) {
		super();
		this.fact = fact;
		this.factMember = null;
		this.parentReference = parentReference;
	}

	public FactType getFact() {
		return fact;
	}
	
	public void setFact( FactType factType ) {
		this.fact = factType;
	}

	public FactMember getFactMember() {
		return factMember;
	}
	
	public void setFactMember( FactMember factMember ) {
		this.factMember = factMember;
	}
	
	public boolean isFactTypeReference() {
		return factMember == null;
	}
	
	public boolean isFactTypeMemberReference() {
		return !isFactTypeReference();
	}
	
	public CompoundIdReference getParentReference() {
		return parentReference;
	}

	public void setParentReference(CompoundIdReference parentReference) {
		this.parentReference = parentReference;
	}

	@Override
	public String toString() {
		return "CompoundIdReference [fact=" + fact + ", factMember="
				+ factMember + ", parentReference=" + parentReference + "]";
	}
}
