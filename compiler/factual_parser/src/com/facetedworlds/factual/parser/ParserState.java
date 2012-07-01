package com.facetedworlds.factual.parser;

import com.facetedworlds.factual.parsetree.CompoundIdentifier;
import com.facetedworlds.factual.parsetree.ConditionModifier;
import com.facetedworlds.factual.parsetree.ExistenceModifier;
import com.facetedworlds.factual.parsetree.FactCardinality;
import com.facetedworlds.factual.parsetree.FactImportAliasDeclaration;
import com.facetedworlds.factual.parsetree.FactImportDeclaration;
import com.facetedworlds.factual.parsetree.FactMember;
import com.facetedworlds.factual.parsetree.FactType;
import com.facetedworlds.factual.parsetree.FactClause;
import com.facetedworlds.factual.parsetree.FactFile;
import com.facetedworlds.factual.parsetree.FactMemberComplex;
import com.facetedworlds.factual.parsetree.FactMemberPredicate;
import com.facetedworlds.factual.parsetree.FactMemberPrimitive;
import com.facetedworlds.factual.parsetree.FactMemberSection;
import com.facetedworlds.factual.parsetree.FactMemberQuery;
import com.facetedworlds.factual.parsetree.FactSet;
import com.facetedworlds.factual.parsetree.Namespace;
import com.facetedworlds.factual.parsetree.PrimitiveType;

public class ParserState {

	private FactFile factFile;
	
	// Parser state information for namespace - cardinality 1
	private Namespace namespace;

	// Parse state information for current import statement
	private FactImportDeclaration currentImportDeclaration;
	
	// Parser state for current fact being parsed
	private FactType currentFact;
	private FactMemberSection currentFactMemberSection = FactMemberSection.KEY;
	private PrimitiveType currentFactMemberPrimitiveType = PrimitiveType.BOOL;
	
	private FactSet currentFactSet;
	
	public ParserState() {
		factFile = new FactFile();
	}
	
	public FactFile getFactFile() {
		return factFile;
	}
	
	public void addNamespace( Namespace ns ) throws FactualParserException {
		
		// Ensure that the namespace if valid
		IdentifierValidation.validateNamespace(ns);

		// Set the namespace into the FactFile
		factFile.setNamespace(ns);
		
		// This namespace now becomes the current namespace.
		namespace = ns;
	}
	
	public Namespace getNamespace() {
		return this.namespace;
	}
	
	public FactType getCurrentFact() {
		return currentFact;
	}
	
	public void startFact( FactType f ) {
		
		if( currentFact != null ) {
			throw new IllegalStateException( "Fact is already started." );
		}
		
		currentFact = f;
	}
	
	public void completeFact() {
		factFile.addFact( currentFact );
		currentFact = null;
	}
	
	public void setCurrentFactMemberSection( FactMemberSection f ) {
		currentFactMemberSection = f;
	}
	
	public void setCurrentFactMemberPrimitiveType( String typeString ) {
		currentFactMemberPrimitiveType = PrimitiveType.valueOf( typeString.toUpperCase() );
	}
	
	public void addPrimitiveFactMember( String identifier , String published, FactCardinality c , int lineNumber , int columnNumber ) {
		FactMemberPrimitive newFactMember = new FactMemberPrimitive(currentFact, identifier, currentFactMemberSection, published != null && published.length() > 0 ,
		        currentFactMemberPrimitiveType, c , lineNumber, columnNumber);
		currentFact.addFactMember(newFactMember);
	}
	
	public void addComplexFactMember( String factType , String identifier , String published, FactCardinality cardinality , int lineNumber , int columnNumber ) {
		FactMember newFactMember = new FactMemberComplex(currentFact, factType, identifier, currentFactMemberSection, published != null && published.length() > 0 , 
				cardinality , lineNumber, columnNumber);
		currentFact.addFactMember(newFactMember);
	}
	
	public void addQueryFactMember( String factType , String identifier , int lineNumber , int columnNumber ) {
		FactMemberQuery newFactMember = new FactMemberQuery(currentFact, factType, identifier, lineNumber, columnNumber);
		currentFact.addFactMember(newFactMember);
	}
	
	public void addQueryFactSet( String factType , String alias , String leftIdentifier , String rightIdentifier , int lineNumber , int columnNumber ) {
		
		CompoundIdentifier leftId = new CompoundIdentifier(leftIdentifier, lineNumber, columnNumber);
		CompoundIdentifier rightId = new CompoundIdentifier(rightIdentifier, lineNumber, columnNumber);
		FactSet newFactSet = new FactSet(alias, factType, leftId, rightId, lineNumber, columnNumber);
		((FactMemberQuery)currentFact.getLastFactMember()).addFactSet( newFactSet );
		currentFactSet = newFactSet; // Condition decl will need this
	}
	
	public void addQueryFactClause( String alias , String memberName , String existence , int lineNumber , int columnNumber ) {
		ConditionModifier conditionModifier = ConditionModifier.POSITIVE;
		if( existence != null && existence.compareTo( "not" ) == 0 ) {
			conditionModifier = ConditionModifier.NEGATIVE;
		}
		FactClause newClause = new FactClause(alias, memberName, conditionModifier, lineNumber, columnNumber);
		
		currentFactSet.getCondition().addFactClause(newClause);
	}

	public void addPredicateFactMember( String identifier , int lineNumber , int columnNumber ) {
		FactMemberPredicate newFactMember = new FactMemberPredicate(currentFact, identifier, lineNumber, columnNumber);
		currentFact.addFactMember( newFactMember );
	}
	
	public void applyExistenceModifierToPredicate( String notFlag ) {
		
		ExistenceModifier existenceModifier = ExistenceModifier.POSITIVE;
		
		if( notFlag != null && notFlag.length() > 0 ) {
			existenceModifier = ExistenceModifier.NEGATIVE;
		}
		((FactMemberPredicate)currentFact.getLastFactMember()).setExistenceModifier(existenceModifier);
	}
	
	public void addFactImportDeclaration( FactImportDeclaration importDecl ) {
		currentImportDeclaration = importDecl;
		factFile.addImportDeclaration(importDecl);
	}
	
	public void addFactImportAliasDeclaration( FactImportAliasDeclaration aliasDecl ) {
		currentImportDeclaration.addFactImportAliasDeclaration( aliasDecl );
	}
}

