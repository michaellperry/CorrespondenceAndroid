package com.facetedworlds.factual.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import com.facetedworlds.factual.compiler.codegen.java.JavaIdentifierFormatting;
import com.facetedworlds.factual.parsetree.CompoundIdentifier;
import com.facetedworlds.factual.parsetree.FactCardinality;
import com.facetedworlds.factual.parsetree.FactImportAliasDeclaration;
import com.facetedworlds.factual.parsetree.FactImportDeclaration;
import com.facetedworlds.factual.parsetree.FactMemberPrimitive;
import com.facetedworlds.factual.parsetree.FactMemberPublishable;
import com.facetedworlds.factual.parsetree.FactType;
import com.facetedworlds.factual.parsetree.FactClause;
import com.facetedworlds.factual.parsetree.FactMemberPredicate;
import com.facetedworlds.factual.parsetree.FactSetCondition;
import com.facetedworlds.factual.parsetree.FactFile;
import com.facetedworlds.factual.parsetree.FactMember;
import com.facetedworlds.factual.parsetree.FactMemberComplex;
import com.facetedworlds.factual.parsetree.FactMemberQuery;
import com.facetedworlds.factual.parsetree.FactMemberSection;
import com.facetedworlds.factual.parsetree.FactOption;
import com.facetedworlds.factual.parsetree.FactSet;
import com.facetedworlds.factual.parsetree.FileLocation;
import com.facetedworlds.factual.parsetree.Namespace;

public class FactFileVerifier {

	private ArrayList<String> errorList = new ArrayList<String>();
	private boolean emitToStdError;
	private String currentFactFilename = "";
	
	public FactFileVerifier() {
		this.emitToStdError = true;
	}
	
	public void setCurrentFactFilename( String filename ) {
		this.currentFactFilename = filename;
	}

	public void verify( FactFile factFile ) {
		verify( null , factFile );
	}
	
	public void verify( FactCompileSpace compileSpace , FactFile factFile ) {
		
		// There should be one and only one namespace
		Namespace ns = factFile.getNamespace();
		if( ns == null ) {
			emitError( 1 , 1 , "Missing namespace declaration." );
		}
		
		// The namespace must have a valid identifier
		String namespaceId = ns.getIdentifier();
		if( namespaceId == null || namespaceId.length() < 1 ) {
			emitError( ns , "Missing namespace identifier." );
		}
		
		// Validate the includes against the compiler space if available.
		validateImports(compileSpace, factFile);
		
		// Create a listing of all fact names
		HashSet<String> factNameSet = new HashSet<String>();
		for( FactType f : factFile.getFacts() ) {
			
			if( factNameSet.contains( f.getFactType() ) ) {
				emitError( f , "Duplicate fact name '" + f.getFactType() + "'" );
				continue;
			}
			
			factNameSet.add(f.getFactType());

			// Ensure that there are no duplicate definitions of fact options
			// TODO Unique should be the first key!
			countFactOption( f , FactOption.UNIQUE );
			
			// Check the fact members
			validateFactMembers( compileSpace , factFile, f );
		}
		
		// Expand mutables 
		expandMutables( compileSpace , factFile , factNameSet );
	}

    private void expandMutables( FactCompileSpace compileSpace , FactFile factFile , HashSet<String> factNameSet ) {
        
        ArrayList<FactType> typesToAdd = new ArrayList<FactType>();
        
        for( FactType nextFactType : factFile.getFacts() ) {
            
            for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
                
                if( nextFactMember.getSection() == FactMemberSection.MUTABLE ) {
                    
                    String mutableFactTargetClass = new JavaIdentifierFormatting().formatMutableAssociationClass(nextFactType.getFactType(),
                            nextFactMember.getIdentifier() );
                    
                    // Make sure that this name isn't in the factNameSet already
                    if( factNameSet.contains( mutableFactTargetClass ) ) {
                        emitError( nextFactMember , "Declared mutable cause the generation of an already existing fact type: " + 
                                nextFactMember.getIdentifier() + " (" + mutableFactTargetClass + ")" );
                        continue;
                    }
                    
                    FactType newFactType = new FactType( mutableFactTargetClass, nextFactMember.getLineNumber(), nextFactMember.getColumnNumber() );
                    
                    // Add owning predecessor
                    // TODO fix this 
                    boolean isPublished = nextFactMember instanceof FactMemberPublishable ? ((FactMemberPublishable)nextFactMember).getPublished() : false;
                    
                    String ownerMemberName = nextFactType.getFactType().substring(0,1).toLowerCase() + nextFactType.getFactType().substring( 1 );
                    FactMemberComplex parentPredecessor = new FactMemberComplex(newFactType, nextFactType.getFactType(), 
                            ownerMemberName, FactMemberSection.KEY , 
                            isPublished ,
                            FactCardinality.ONE , nextFactMember.getLineNumber() , nextFactMember.getColumnNumber() );
                    newFactType.addFactMember(parentPredecessor);
                    
                    // Add the actual data to be held
                    if( nextFactMember instanceof FactMemberPrimitive ) {
                        //dataType = ((FactMemberPrimitive)nextFactMember).getPrimitiveDataType().getCorrespondenceType();
                        FactMemberPrimitive dataPrececessor = new FactMemberPrimitive(nextFactType, "value", FactMemberSection.KEY, 
                                ((FactMemberPrimitive) nextFactMember).getPublished(), // pass along the published flag
                                ((FactMemberPrimitive) nextFactMember).getPrimitiveDataType(),
                                FactCardinality.ONE, nextFactMember.getLineNumber() , nextFactMember.getColumnNumber() );
                        newFactType.addFactMember(dataPrececessor);
                    }
                    else {
                        String dataType = ((FactMemberComplex)nextFactMember).getFactType();
                        FactMemberComplex dataPredecessor = new FactMemberComplex( newFactType , dataType , "value" , 
                                FactMemberSection.KEY, false , FactCardinality.ONE , nextFactMember.getLineNumber() , nextFactMember.getColumnNumber() );
                        newFactType.addFactMember(dataPredecessor);
                    }
                    
                    // Generate a query to get the mutable.
                    FactMemberQuery mutableQuery = new FactMemberQuery(nextFactType, newFactType.getFactType(), nextFactMember.getIdentifier(), 
                            nextFactMember.getLineNumber() , nextFactMember.getColumnNumber() );
                    mutableQuery.setGeneratedFromMutable(true);
                    int lineNumber = nextFactMember.getLineNumber();
                    int columnNumber = nextFactMember.getColumnNumber();
                    
                    FactSet joinSuccessorSet = new FactSet("a", newFactType.getFactType(), 
                            new CompoundIdentifier( "this", lineNumber, columnNumber),
                            new CompoundIdentifier( "a." + ownerMemberName , lineNumber, columnNumber), 
                            nextFactMember.getLineNumber(), nextFactMember.getColumnNumber() );
                    mutableQuery.addFactSet( joinSuccessorSet );
                    nextFactType.addFactMember(mutableQuery);
                    
                    
                    // Update the listing of factNameSet
                    factNameSet.add( mutableFactTargetClass );
                    
                    // Add the newly generated class to the fact file
                    typesToAdd.add( newFactType );
                }
            }
        }
        
        for( FactType nextFactType : typesToAdd ) {
            factFile.addFact(nextFactType);
        }
    }
    

	private void validateImports(FactCompileSpace compileSpace,
			FactFile factFile) {
		
		// TODO Need to also make sure that none of the import decl's collide on names.
		
		if( compileSpace == null && factFile.getFactImportDeclarationCount() > 0 ) {
			throw new IllegalStateException( "Null FactCompileSpace provided for a fact file that has import declarations." );
		}
		for( FactImportDeclaration nextImportDecl : factFile.getFactImportDeclarations() ) {
			
			String importedNamespace = nextImportDecl.getNamespace();
			FactFile importedFactFile = compileSpace.getFactFileByNamespace(importedNamespace);
			
			if( importedFactFile == null ) {
				emitError( nextImportDecl , "Undefined namespace referenced: " + importedNamespace );
				continue;
			}
			
			for( FactImportAliasDeclaration nextAlias : nextImportDecl.getFactImportAliasDeclarations() ) {
				
				String externalFactTypeString = nextAlias.getOriginalFactType();
				FactType externalFactType = importedFactFile.getFactTypeByName(externalFactTypeString);
				
				if( externalFactType == null ) {
					emitError( nextAlias , "Fact type '" + externalFactTypeString + "' does not exist in the namespace '" + importedNamespace + "'" );
				}
			}
		}
	}
	
	protected void validateFactMembers( FactCompileSpace compileSpace , FactFile factFile , FactType f ) {
		
		// TODO No duplicate member names inside of a fact
		for( FactMember nextMember : f.getFactMembers() ) {
			
			if( nextMember instanceof FactMemberComplex ) {
				// TODO do i still need to pass f 
				validateFactMemberComplex(factFile, f, (FactMemberComplex)nextMember);
			}
			else if( nextMember instanceof FactMemberQuery ) {
				validateFactMemberQuery( compileSpace , factFile , (FactMemberQuery)nextMember );
			}
			else if( nextMember instanceof FactMemberPrimitive ) {
			    validateFactMemberPrimitive( compileSpace, factFile , (FactMemberPrimitive)nextMember );
			}
			
		}
	}

	private void validateFactMemberComplex(FactFile factFile, FactType f, FactMemberComplex complexMember) {

		if( complexMember.getPublished() == true && complexMember.getSection() == FactMemberSection.QUERY ) {
			emitError( complexMember , "Only 'key' and 'mutable' fact members can be published." );
		}
		
		// Make sure that the referenced complex fact type is defined...
		if( factFile.doesFactTypeExist( complexMember.getFactType() ) == false ) {
			emitError( complexMember , "Unknown fact type referenced: '" + complexMember.getFactType() + "'" );
		}
	}
	
	private void validateFactMemberQuery(FactCompileSpace compileSpace, FactFile factFile , FactMemberQuery query) {
		
		boolean isPredicate = query instanceof FactMemberPredicate;
		
		// Predicates do not need thier fact types checked...they are always 'bool'
		if( !isPredicate && factFile.doesFactTypeExist( query.getFactType() ) == false ) {
			emitError( query , "Query references undefined fact type '" + query.getFactType() + "'");
		}

		int offset = 0;
		
		// currentFact always starts with "this"
		FactType currentFact = query.getOwningFact();
		
		HashMap<String, FactType> aliasMap = new HashMap<String, FactType>();
		String lastFactSetType = null;
		for( FactSet nextFactSet : query.getFactSets() ) {
			
			if( factFile.doesFactTypeExist( nextFactSet.getFactType() ) == false  ) {
				emitError( nextFactSet , "Query references undefined fact type '" + nextFactSet.getFactType() );
			} 
			
			// Update the fact alias map
			if( aliasMap.containsKey(nextFactSet.getAlias() ) ) {
				emitError( nextFactSet , "Fact query set contains duplicate definition for alias '" + nextFactSet.getAlias() + "'" );
			}
			aliasMap.put( nextFactSet.getAlias(), factFile.getFactTypeByName( nextFactSet.getFactType() ) );
			
			// First set must have "this" on either the left or the right side.
			if( offset == 0 ) {
				// Chaining is from THIS fact down to successor
				// On first set, one will start with this (order doesn't matter)

				if( nextFactSet.getLeftIdentifier().isRelativeToThis() == false && nextFactSet.getRightIdentifier().isRelativeToThis() == false ) {
					emitError( nextFactSet , "Initial fact set must have a reference to 'this' in either the left or right connection" );
					break; // no need to go through the rest of the sets.
				}
			}
			
			// Resolve the left identifier
			CompoundIdReference leftReference = CompoundIdResolver.resolveToFactMember(this, compileSpace, factFile, currentFact, nextFactSet.getLeftIdentifier(), aliasMap);
			if( leftReference == null && nextFactSet.getLeftIdentifier().isThisOnly() == false ) {
				// error would have already been reported...
				break;
			}
			
			// Resolve the right identifier
			CompoundIdReference rightReference = CompoundIdResolver.resolveToFactMember(this, compileSpace, factFile, currentFact, nextFactSet.getRightIdentifier(), aliasMap);
			if( rightReference == null && nextFactSet.getRightIdentifier().isThisOnly() == false ) {
				// error would have already been reported
				break;
			}
				
			// If the right reference resolves to the current fact...then normalize so that left 
			// is always the starting point.
			if( rightReference.getFact() == currentFact ) {
				CompoundIdReference temp = leftReference;
				leftReference = rightReference;
				rightReference = temp;
				
				// Mutate the tree so that the left is always the current fact type...this will
				// make code generation easier.
				nextFactSet.swapIdentifiers();
			}

			//	Make sure that current leftIdentifier does reference the current fact type
			if( leftReference.getFact() != currentFact ) {
				emitError( leftReference.getFactMember() , "Neither left nor right connection statement references the current fact type." );
				break;
			}
			
			// both left and right identifier should resolve to the same fact type (after normalization, "this" will always be the right identifier)
			String rightFactTypeName = CompoundIdResolver.resolveFactTypeName(rightReference);
			String leftFactTypeName = CompoundIdResolver.resolveFactTypeName(leftReference);
			
			if( leftFactTypeName.compareTo(rightFactTypeName) != 0 ) {
				emitError( leftReference.getFactMember() , "Improper chain statement, left and right identifiers must resolve to the same fact type" );
				break;
			}
			
			validateFactSetCondition( compileSpace, factFile , currentFact , nextFactSet , nextFactSet.getCondition() );

			// Store the type of the last fact set...
//			if( rightReference.isFactTypeReference() ) {
				//lastFactSetType = rightReference.getFact().getFactType();
				lastFactSetType = aliasMap.get( nextFactSet.getAlias() ).getFactType();
				currentFact = rightReference.getFact();
//			}
//			else {
//				lastFactSetType = ((FactMemberComplex)rightReference.getFactMember()).getFactType();
//				//currentFact = factFile.getFactTypeByName(((FactMemberComplex)rightReference.getFactMember()).getFactType());
//				currentFact = rightReference.getFact();
//			}
			
			offset++;
		}

		// Type of the last set should be equal to the type of the query
		if( !isPredicate && lastFactSetType.compareTo( query.getFactType() ) != 0 ) {
			emitError( query , "The query set(s) do not resolve to the proper fact type: " + query.getFactType() );
		}
	}
	
	protected void validateFactMemberPrimitive(FactCompileSpace compileSpace, FactFile factFile , FactMemberPrimitive primitive) {
	    
	    // If this primitive is published, then we need to make sure that it is in the mutable section
	    if( primitive.getPublished() == true ) {
	        if( primitive.getSection() != FactMemberSection.MUTABLE ) {
	            emitError(primitive, String.format( "The 'publish' keyword can only be used on primitive values that are in the 'mutable' section of a fact type." ) );
	        }
	    }
	}
	
	protected void validateFactSetCondition( FactCompileSpace compileSpace,  FactFile factFile , FactType targetFact , FactSet factSet , FactSetCondition factSetCondition ) {

		// Clause can occur anywhere...and must evaluate to a resolvable type - this relative or type relative 
		for( FactClause nextClause : factSetCondition.getFactClauses() ) {

			// Clause alias must match set alias.
			if( factSet.getAlias().compareTo( nextClause.getAlias() ) != 0 ) {
				emitError( nextClause, "Unknown alias '" + nextClause.getAlias() + "' in fact set clause." );
			}
			
			// Make sure that the given member exists.
			StringBuilder idBuilder = new StringBuilder();
			idBuilder.append( nextClause.getAlias() );
			idBuilder.append( "." );
			idBuilder.append( nextClause.getMemberName() );
			
			CompoundIdentifier memberId = new CompoundIdentifier( idBuilder.toString() , nextClause.getLineNumber() , nextClause.getColumnNumber() );
			
			HashMap<String, FactType> aliasMap = new HashMap<String, FactType>();
			aliasMap.put( factSet.getAlias() , factFile.getFactTypeByName( factSet.getFactType() ) );
			
			CompoundIdReference ref = CompoundIdResolver.resolveToFactMember(this, compileSpace, factFile, targetFact, memberId, aliasMap);
			if( ref == null ) {
				return; // error already reported.
			}
			
			if( ref.isFactTypeReference() ) {
				emitError(nextClause, "The clause must refer to a fact member, not a fact: " + memberId.toString() );
			}
			
			FactMember resolvedMember = ref.getFactMember();
			if(! (resolvedMember instanceof FactMemberPredicate )) {
				emitError( nextClause, "The clause must refer to a predicate query type." );
			}
		}
	}
	
	protected void emitError( FileLocation f , String error ) {
		emitError( f.getLineNumber() , f.getColumnNumber() , error );
	}
	
	protected void emitError( int line , int column , String error ) {
		String s = currentFactFilename + " line " + line + ":" + column + " " + error;
		errorList.add(s);
		
		if( emitToStdError ) {
			System.err.println( s );
		}
	}
	
	protected void countFactOption( FactType f , FactOption fo ) {
		int count = 0;
		for( FactOption nextOption : f.getFactOptionSet().getFactOptions() ) {
			if( nextOption == fo ) {
				++count;
				
				if( count > 1 ) {
					emitError(f, "Duplicate fact option declared: '" + fo.toString().toLowerCase() + "'" );
				}
			}
		}
	}
	
	public int getErrorCount() {
		return errorList.size();
	}
	
	public Iterable<String> getErrorListIterable() {
		return Collections.unmodifiableList(errorList);
	}
}
