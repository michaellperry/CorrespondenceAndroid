package com.facetedworlds.factual.compiler;

import java.util.Map;

import com.facetedworlds.factual.parsetree.CompoundIdentifier;
import com.facetedworlds.factual.parsetree.FactType;
import com.facetedworlds.factual.parsetree.FactFile;
import com.facetedworlds.factual.parsetree.FactMember;
import com.facetedworlds.factual.parsetree.FactMemberComplex;
import com.facetedworlds.factual.parsetree.FactMemberPrimitive;
import com.facetedworlds.factual.parsetree.FactMemberQuery;
import com.facetedworlds.factual.parsetree.FactMemberPredicate;

public class CompoundIdResolver {

	public static String resolveFactTypeName( CompoundIdReference ref ) { 
		
		if( ref.isFactTypeReference() ) {
			return ref.getFact().getFactType();
		}
		
		FactMember m = ref.getFactMember();
		
		if( m instanceof FactMemberPrimitive ) {
			return ((FactMemberPrimitive) m).getPrimitiveDataType().toString().toLowerCase();
		}
		else if( m instanceof FactMemberComplex ) {
			return ((FactMemberComplex) m).getFactType();
		}
		else if( m instanceof FactMemberQuery ) {
			return ((FactMemberQuery) m).getFactType();
		}
		else if( m instanceof FactMemberPredicate ) {
			return "bool";
		}
		
		throw new IllegalStateException();
	}

	public static CompoundIdReference resolveToComplexFactMember( 
			FactFileVerifier verifier , FactCompileSpace compileSpace, FactFile factFile , 
			FactType sourceFact , CompoundIdentifier identifier , Map<String, FactType> aliasMap ) {

		CompoundIdReference returnRef = resolveToFactMember(verifier, compileSpace, factFile, sourceFact, identifier, aliasMap);
		if( returnRef == null ) {
			return null;
		}
		
		if( returnRef.isFactTypeReference() ) {
			return returnRef;
		}
		
		FactMember factMember = returnRef.getFactMember();
		
		if( factMember instanceof FactMemberPrimitive ) {
			verifier.emitError( identifier , "The identifier '" + identifier.toString() + "' resolves to a primitive type." );
			return null;
		}
		else if( factMember instanceof FactMemberPredicate ) {
			verifier.emitError( identifier , "The identifier '" + identifier.toString() + "' resolves to a predicate type." );
			return null;
		}
		
		return returnRef;
	}
	
	
	public static CompoundIdReference resolveToFactMember( 
			FactFileVerifier verifier , FactCompileSpace compileSpace, FactFile factFile , 
			FactType sourceFact , CompoundIdentifier identifier , Map<String, FactType> aliasMap ) {
	
		FactType currentFact = sourceFact;
		FactMember currentFactMember = null;
		CompoundIdReference currentReference = null;
		
		int offset = 0;
		for( String nextIdentifierPart : identifier.getIdentifierPartIterable() ) {

			// Identifier continuation after a primitive or predicate
			if( currentFact == null ) {
				verifier.emitError( identifier, "Unable to resolve all of the give identifier: " + identifier.toString() + ". Can't resolve: " + identifier.toString(offset) );
				return null;
			}
			
			// Check for the "this" reserved word...only valid @ offset == 0
			if( nextIdentifierPart.compareTo( "this" ) == 0 ) {
				
				if( offset != 0 ) {
					verifier.emitError(identifier, "The 'this' keyword is not legal in this position." );
					return null;
				}
				
				// We are @ offset == 0 and we have "this"...just continue since the current 
				// fact should be the same as the sourceFact.
			}
			else {
				if( offset == 0 ) {

					if( aliasMap == null ) {
						throw new IllegalStateException();
					}
					
					// This is an alias lookup
					currentFact = aliasMap.get( nextIdentifierPart );
					if( currentFact == null ) {
						verifier.emitError(identifier, "Undefined fact type alias '" + nextIdentifierPart + "'" );
						return null;
					}
				}
				else {
					
					// Identifier that should be a member
					currentFactMember = currentFact.getFactMemberByName( nextIdentifierPart );
					if( currentFactMember == null ) {
						verifier.emitError(identifier, "The member '" + nextIdentifierPart + "' is undefined for fact type '" + currentFact.getFactType()  + "'");
						return null;
					}
					
					// Set the currentFact to the given member's type
					if( currentFactMember instanceof FactMemberPrimitive ) {
						currentFact = null; // Set the current fact to null so we get a resolution error if we go through 
							// the loop again.
					}
					else if( currentFactMember instanceof FactMemberComplex ) {
						currentFact = factFile.getFactTypeByName(((FactMemberComplex)currentFactMember).getFactType());
					}
					else if( currentFactMember instanceof FactMemberQuery ) {
						currentFact = factFile.getFactTypeByName(((FactMemberQuery)currentFactMember).getFactType());
					}
					else if( currentFactMember instanceof FactMemberPredicate ) {
						currentFact = null; // Set the current fact to null so we get a resolution error if we go through
							// the loop again.
					}
					else {
						throw new IllegalStateException();
					}
				}
			}

			if( currentFactMember != null ) {
				currentReference = new CompoundIdReference( currentFactMember.getOwningFact() , currentFactMember , currentReference );
			}
			else {
				currentReference = new CompoundIdReference( currentFact , currentReference );
			}

			++offset;
		}
		
		if( currentReference == null ) {
			currentReference = new CompoundIdReference(currentFact, null, null);
		}
		return currentReference;
	}
	
	
}
