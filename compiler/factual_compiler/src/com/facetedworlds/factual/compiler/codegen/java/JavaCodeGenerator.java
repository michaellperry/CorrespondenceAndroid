package com.facetedworlds.factual.compiler.codegen.java;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.facetedworlds.factual.compiler.CompilerContextVariables;
import com.facetedworlds.factual.compiler.CompoundIdReference;
import com.facetedworlds.factual.compiler.CompoundIdResolver;
import com.facetedworlds.factual.compiler.FactCompileSpace;
import com.facetedworlds.factual.compiler.FactFileVerifier;
import com.facetedworlds.factual.compiler.codegen.CodeGenerator;
import com.facetedworlds.factual.compiler.codegen.CodeGeneratorAbstractBase;
import com.facetedworlds.factual.compiler.codegen.CodeGeneratorWorkspace;
import com.facetedworlds.factual.parsetree.ConditionModifier;
import com.facetedworlds.factual.parsetree.ExistenceModifier;
import com.facetedworlds.factual.parsetree.FactCardinality;
import com.facetedworlds.factual.parsetree.FactClause;
import com.facetedworlds.factual.parsetree.FactFile;
import com.facetedworlds.factual.parsetree.FactMember;
import com.facetedworlds.factual.parsetree.FactMemberComplex;
import com.facetedworlds.factual.parsetree.FactMemberPredicate;
import com.facetedworlds.factual.parsetree.FactMemberPrimitive;
import com.facetedworlds.factual.parsetree.FactMemberQuery;
import com.facetedworlds.factual.parsetree.FactMemberSection;
import com.facetedworlds.factual.parsetree.FactOption;
import com.facetedworlds.factual.parsetree.FactSet;
import com.facetedworlds.factual.parsetree.FactType;
import com.facetedworlds.factual.parsetree.PrimitiveType;
import com.facetedworlds.rhineland.common.cmdline.CommandLineParser;

public class JavaCodeGenerator extends CodeGeneratorAbstractBase implements CodeGenerator {

	protected File javaBaseSourceDirectory;
	protected File factFileBaseNamespaceDirectory;
	protected File factTypeOutputFile;
    protected ArrayList<FactType> communityFacts;
	
	protected PrintWriter factTypeOutputWriter;
	
	@Override
	protected boolean beforeCompileAll(CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context ) {
	    
	    // Reset the list of ALL facts in the module
	    communityFacts = new ArrayList<FactType>();
	    
		if( super.beforeCompileAll(commandLineParser, compileSpace, context) == false ) {
			return false;
		}
		
		// Setup the code generation context to be Java/Android compatible
		context.setIndentUsingTabs(false);
		context.setLineEndingCharacters("\n");
		
		// Make sure that the base output directory gets created.
		String javaBaseDirectoryString = commandLineParser.getParameterValueString( CompilerContextVariables.JAVA_OUTPUT_BASE_DIRECTORY );
		javaBaseSourceDirectory = new File( javaBaseDirectoryString );
		
		// Ensure the base java output directory exists.
		if( javaBaseSourceDirectory.exists() == false ) {
			System.out.println( "Creating Java output directory: " + javaBaseDirectoryString );
			
			if( javaBaseSourceDirectory.mkdirs() == false ) {
				System.out.println( "Unable to craete Java output directory: " + javaBaseDirectoryString );
				return false;
			}
		}

		return true;
	}

	@Override
	protected boolean beforeFactFileCompile(FactFile nextFactFile, CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context ) {
		if( super.beforeFactFileCompile(nextFactFile, commandLineParser, compileSpace, context) == false ) {
			return false;
		}
		
		// TODO: capitalization needs to be altered to Java syntax for namespace
		
		// For the namespace of the nextFactFile...ensure that the directory is created.
		String targetNamespace = nextFactFile.getNamespace().getIdentifier();
		factFileBaseNamespaceDirectory = new File( javaBaseSourceDirectory , new JavaIdentifierFormatting().formatNamespace( 
				targetNamespace.replace( '.' , File.separatorChar ) ) );
		
		if( factFileBaseNamespaceDirectory.exists() == false ) {
			if( factFileBaseNamespaceDirectory.mkdirs() == false ) {
				System.out.println( "Unable to create package directory: " + factFileBaseNamespaceDirectory.getAbsolutePath() );
				return false;
			}
		}
	
		return true;
	}
	
	@Override
	protected boolean beforeFactTypeCompile(FactFile nextFactFile, FactType nextFactType, CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context ) {

		if( super.beforeFactTypeCompile(nextFactFile, nextFactType,
				commandLineParser, compileSpace, context) == false ) {
			return false;
		}
		
		// TODO: capitalization needs to be altered to Java syntax for fact type name
		
		// Create the output file object 
		factTypeOutputFile = new File( factFileBaseNamespaceDirectory , new JavaIdentifierFormatting().formatClassname( nextFactType.getFactType() ) + ".java" );

		// Create an output stream to the file being generated
		try {
			factTypeOutputWriter = new PrintWriter(factTypeOutputFile);
			
		} catch (IOException e) {
			factTypeOutputWriter = null;
			System.out.println( "Error creating output file: " + factTypeOutputFile.getAbsolutePath() );
			return false;
		}
		
		return true;
}

	@Override
	protected boolean compileFact(FactFile nextFactFile, FactType nextFactType, CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context ) {
		
		try {
		    // Add this FactType to the community listing
		    communityFacts.add( nextFactType );
		    
			JavaIdentifierFormatting idFormatter = new JavaIdentifierFormatting();
			
			String namespace = idFormatter.formatNamespace( nextFactFile.getNamespace().getIdentifier() );
			String namespaceFactualCase = nextFactFile.getNamespace().getIdentifier();
			String factTypeName = idFormatter.formatClassname( nextFactType.getFactType() );
	
			CodeGeneratorWorkspace c = context;
			
			// Process mutables since they generate other classes...
			// processMutableClasses( nextFactType , idFormatter , c );
			
			// Output the package name 
			c.printHeader( "package " + namespace + ";" );
			
			// Output imports
			if( hasPrimitiveKeyFields(nextFactType)) {
				c.printHeader();
				c.printHeader( "import java.io.ByteArrayInputStream;" );
				c.printHeader( "import java.io.DataInputStream;" );
				c.printHeader( "import java.io.DataOutputStream;" );
				c.printHeader( "import java.io.IOException;" );
				c.printHeader( "import java.util.HashMap;" );

				if( nextFactType.getFactOptionSet().hasOption(FactOption.UNIQUE ) ) {
					c.printHeader( "import java.util.UUID;" );
				}
				c.printHeader();
				c.printHeader( "import com.updatecontrols.correspondence.*;" );
				c.printHeader( "import com.updatecontrols.correspondence.serialize.*;" );
				c.printHeader( "import com.updatecontrols.correspondence.memento.*;" );
				
				if( nextFactType.hasMembersInSection( FactMemberSection.QUERY ) ) {
					c.printHeader( "import com.updatecontrols.correspondence.query.*;" );
				}
				c.printHeader();
			}
			else {
				c.printHeader();
				c.printHeader( "import java.io.DataOutputStream;" );
				c.printHeader( "import java.io.IOException;" );
				c.printHeader( "import java.util.HashMap;" );
				c.printHeader();
				c.printHeader( "import com.updatecontrols.correspondence.*;" );
				c.printHeader( "import com.updatecontrols.correspondence.serialize.*;" );
				c.printHeader( "import com.updatecontrols.correspondence.memento.*;" );
				if( nextFactType.hasMembersInSection( FactMemberSection.QUERY ) ) {
					c.printHeader( "import com.updatecontrols.correspondence.query.*;" );
				}
				c.printHeader();
			}
			
			// Output the class
			c.print( String.format("public class %s extends CorrespondenceFact {" , factTypeName ) );
			c.indent();
			{
				c.print();
				c.print( "static class CorrespondenceFactFactoryImpl implements CorrespondenceFactFactory {" );
				c.indent();
				{
					c.print();
					c.print( "@Override" );
					c.print( "public CorrespondenceFact createFact(FactMemento factMemento, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws CorrespondenceException { ");
					c.indent();
					{
						if( hasPrimitiveKeyFields( nextFactType ) ) {
						
							writePrimitiveSerializationCode(nextFactType, idFormatter, c);
						}					
						else {
							writeNoPrimitivesSerializationCode(nextFactType, idFormatter, c );
						}
					}
					c.unindent();
					c.print( "}");
					
					c.print();
					c.print( "@Override" );
					c.print( "public void writeFactData(CorrespondenceFact fact, DataOutputStream out, HashMap<Class<?>, FieldSerializer> fieldSerializerByType) throws IllegalArgumentException, IllegalAccessException, IOException {" );
					c.indent();
					{
						if( hasPrimitiveKeyFields(nextFactType) ) {
							c.print( String.format( "%1$s factToWrite = (%1$s)fact;" , factTypeName ) );
							
							// See if the class is marked as "unique"
							if( nextFactType.getFactOptionSet().hasOption( FactOption.UNIQUE ) ) {
								c.print( "fieldSerializerByType.get(UUID.class).writeData(out, factToWrite.unique);"); 
							}
							
							// Write out all of the KEY section primitives
							for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
								if( nextFactMember.getSection() == FactMemberSection.KEY ) {
	
									// TODO I think I'm only interested in primitives here?
									if( nextFactMember instanceof FactMemberPrimitive ) {
										// Primitive predecessor											
										String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
	
										PrimitiveType primitiveType = ((FactMemberPrimitive)nextFactMember).getPrimitiveDataType();
										String fieldJavaType = primitiveType.getJavaDataType( nextFactMember.getCardinality() );
										
										c.print( String.format( "fieldSerializerByType.get( %1$s.class ).writeData(out, factToWrite.%2$s);" , fieldJavaType , fieldName ) );
									}
								}
							}
						}
					}
					c.unindent();
					c.print( "}" );
				}
				c.unindent();
				c.print( "}");  // end of factory
				
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// TYPE static declaration" );
				c.print( "//----------------------------------------------------------------------" );

				// Type declaration
				c.print( String.format( "static final CorrespondenceFactType TYPE = new CorrespondenceFactType( \"%s.%s\" , 1 );" , namespaceFactualCase , factTypeName ) );
				
				c.print( "@Override" );
				c.print( "public CorrespondenceFactType getCorrespondenceFactType() { ");
				c.indent(); 
				{
					c.print( "return TYPE;" );
				}
				c.unindent();
				c.print( "}" );
				
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// Role declarations" );
				c.print( "//----------------------------------------------------------------------" );
				
				// Role definitions
				for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
					if( nextFactMember.getSection() == FactMemberSection.KEY ) {

						if( nextFactMember instanceof FactMemberComplex ) {
							String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
							String fieldJavaType = idFormatter.formatClassname( ((FactMemberComplex) nextFactMember).getFactType() );
							String publishBoolean = ((FactMemberComplex) nextFactMember).getPublished() ? "true" : "false";
							
							c.print( String.format( "public static final Role ROLE_%1$s = new Role( new RoleMemento( TYPE , \"%1$s\" , %2$s.TYPE , %3$s ));",
									fieldName , fieldJavaType , publishBoolean ) ); 
						}
					}
					else if( nextFactMember.getSection() == FactMemberSection.MUTABLE ) {
                        
//					    if( nextFactMember instanceof FactMemberComplex ) {
//					        
//                            String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
//                            String fieldJavaType = idFormatter.formatClassname( ((FactMemberComplex) nextFactMember).getFactType() );
//                            String publishBoolean = ((FactMemberComplex) nextFactMember).getPublished() ? "true" : "false";
//                            
//                            c.print( String.format( "public static final Role ROLE_%1$s = new Role( new RoleMemento( TYPE , \"%1$s\" , %2$s.TYPE , %3$s ));",
//                                    fieldName , fieldJavaType , publishBoolean ) ); 
//                        }
//					    else if( nextFactMember instanceof FactMemberPrimitive ) {
//
//                            String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
//                            String fieldJavaType = idFormatter.formatMutableAssociationClass( nextFactType.getFactType(), nextFactMember.getIdentifier() );
//                            String publishBoolean = ((FactMemberComplex) nextFactMember).getPublished() ? "true" : "false";
//                            
//                            c.print( String.format( "public static final Role ROLE_%1$s = new Role( new RoleMemento( TYPE , \"%1$s\" , %2$s.TYPE , %3$s ));",
//                                    fieldName , fieldJavaType , publishBoolean ) ); 
//					    }
					}
				}

				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// Query definitions" );
				c.print( "//----------------------------------------------------------------------" );

				for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
					if ( nextFactMember.getSection() == FactMemberSection.QUERY ) {

						if( nextFactMember instanceof FactMemberPredicate ) {
							outputQueryDefinition( compileSpace , nextFactFile , idFormatter, c, nextFactMember);
						}
						else if( nextFactMember instanceof FactMemberQuery ) {
							outputQueryDefinition( compileSpace , nextFactFile , idFormatter, c, nextFactMember);
						}
					}
				}
				
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// Field declarations" );
				c.print( "//----------------------------------------------------------------------" );

				// Field definitions
				// Handle the "unique"
				if( nextFactType.getFactOptionSet().hasOption( FactOption.UNIQUE ) ) {
					c.print( "private UUID unique;"); 
				}
				for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
					if( nextFactMember.getSection() == FactMemberSection.KEY ) {

						String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
						
						if( nextFactMember instanceof FactMemberPrimitive ) {
							// Primitive predecessor											
							PrimitiveType primitiveType = ((FactMemberPrimitive)nextFactMember).getPrimitiveDataType();
							String fieldJavaType = primitiveType.getJavaDataType( nextFactMember.getCardinality() );
							
							c.print( String.format( "private %1$s %2$s;" , fieldJavaType , fieldName ) );
						}
						else if( nextFactMember instanceof FactMemberComplex ) {
							
							String javaFactTypeName = idFormatter.formatClassname(((FactMemberComplex) nextFactMember).getFactType());
							String predecessorType = "PredecessorObj";
							
							FactCardinality cardinality = ((FactMember) nextFactMember).getCardinality();
							if( cardinality == FactCardinality.MANY ) {
								predecessorType = "PredecessorList";
							}
							else if( cardinality == FactCardinality.OPTIONAL ) {
								predecessorType = "PredecessorOpt";
							}
							
							c.print( String.format( "private %1$s<%2$s> %3$s;" , predecessorType , javaFactTypeName , fieldName ) );
						}
					}
					else if( nextFactMember.getSection() == FactMemberSection.QUERY ) {
						// Generate a "Result<?>" field
						
						if( nextFactMember instanceof FactMemberPredicate ) {
							c.print( String.format( "private Result<%1$s> %2$sResult;" , 
									((FactMemberPredicate) nextFactMember).determinePredicateReturnFactType(),
									idFormatter.formatIdentifier( nextFactMember.getIdentifier() ) 
									) );
						}
						else {
							c.print( String.format( "private Result<%1$s> %2$sResult;" , 
									idFormatter.formatClassname( ((FactMemberQuery)nextFactMember).getFactType() ) ,
									idFormatter.formatIdentifier( nextFactMember.getIdentifier() ) 
									) );
						}
					}
				}
			
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// Field Constructor" );
				c.print( "//----------------------------------------------------------------------" );

				// Constructor using predecessor fields
				StringBuilder parameterList = new StringBuilder();
				for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
					if( nextFactMember.getSection() == FactMemberSection.KEY ) {

						String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
						String fieldJavaType = null;
						if( nextFactMember instanceof FactMemberPrimitive ) {
							// Primitive predecessor											
							PrimitiveType primitiveType = ((FactMemberPrimitive)nextFactMember).getPrimitiveDataType();
							fieldJavaType = primitiveType.getJavaDataType( nextFactMember.getCardinality() );
						}
						else if( nextFactMember instanceof FactMemberComplex ) {
						    FactMemberComplex factMemberComplex = (FactMemberComplex)nextFactMember;

					        fieldJavaType = idFormatter.formatClassname(((FactMemberComplex) nextFactMember).getFactType());
					        if( factMemberComplex.getCardinality() == FactCardinality.MANY ) {
					            fieldJavaType = "java.util.List<" + fieldJavaType + ">";
					        }
						}
						
						if( fieldJavaType != null ) {
							if( parameterList.length() > 0 ) { 
								parameterList.append( "," );
							}
							
							parameterList.append( fieldJavaType );
							parameterList.append( " " );
							parameterList.append( fieldName );
						}
					}
				}
				c.print( String.format( "public %1$s( %2$s ) {" , factTypeName , parameterList.toString() ) );
				c.indent();
				{
					if( nextFactType.getFactOptionSet().hasOption( FactOption.UNIQUE ) ) {
						c.print( "this.unique = UUID.randomUUID();" );
					}
					
					for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
						if( nextFactMember.getSection() == FactMemberSection.KEY ) {

							String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
							String fieldJavaType = null;
							if( nextFactMember instanceof FactMemberPrimitive ) {
								c.print( String.format( "this.%1$s = %1$s;" , fieldName ) );
							}
							else if( nextFactMember instanceof FactMemberComplex ) {
								String javaFactTypeName = idFormatter.formatClassname(((FactMemberComplex) nextFactMember).getFactType());
								String predecessorType = "PredecessorObj";
								
								FactCardinality cardinality = ((FactMember) nextFactMember).getCardinality();
								if( cardinality == FactCardinality.MANY ) {
									predecessorType = "PredecessorList";
								}
								else if( cardinality == FactCardinality.OPTIONAL ) {
									predecessorType = "PredecessorOpt";
								}
								
								fieldJavaType = String.format( "%s<%s>" , predecessorType , javaFactTypeName );
								
								c.print( String.format( "this.%1$s = new %2$s( this , ROLE_%1$s, %1$s);" , fieldName , fieldJavaType ) );
							}
						}
					}
				}
				c.print( "initResults();");
				c.unindent();
				c.print( "}" );
								
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// FactMemento constructor declarations" );
				c.print( "//----------------------------------------------------------------------" );

				// Constructor using FactMemento
				c.print( String.format( "private %1$s( FactMemento factMemento ) throws CorrespondenceException {" , factTypeName ) );
				c.indent();
				{
					for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
						if( nextFactMember.getSection() == FactMemberSection.KEY ) {

							String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
							String fieldJavaType = null;
							if( nextFactMember instanceof FactMemberComplex ) {
								String javaFactTypeName = idFormatter.formatClassname(((FactMemberComplex) nextFactMember).getFactType());
								String predecessorType = "PredecessorObj";
								
								FactCardinality cardinality = ((FactMember) nextFactMember).getCardinality();
								if( cardinality == FactCardinality.MANY ) {
									predecessorType = "PredecessorList";
								}
								else if( cardinality == FactCardinality.OPTIONAL ) {
									predecessorType = "PredecessorOpt";
								}
								
								fieldJavaType = String.format( "%s<%s>" , predecessorType , javaFactTypeName );
								
								c.print( String.format( "this.%1$s = new %2$s( this , ROLE_%1$s, factMemento );" , fieldName , fieldJavaType ) );
							}
						}
					}
				}
				c.print( "initResults();");
				c.unindent();
				c.print( "}" );
				
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// Results initialize method" );
				c.print( "//----------------------------------------------------------------------" );

				c.print( String.format( "private void initResults() {" ) );
				c.indent();
				{
					for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
						
						if( nextFactMember instanceof FactMemberPredicate ) {
							
							c.print( String.format( "this.%1$sResult = new Result<%2$s>( this , QUERY_%1$s() );" , 
									idFormatter.formatIdentifier( nextFactMember.getIdentifier() ),
									idFormatter.formatClassname( ((FactMemberPredicate) nextFactMember).determinePredicateReturnFactType() )
									));
						}
						else if( nextFactMember instanceof FactMemberQuery ) {
							
							c.print( String.format( "this.%1$sResult = new Result<%2$s>( this , QUERY_%1$s() );" , 
									idFormatter.formatIdentifier( nextFactMember.getIdentifier() ),
									idFormatter.formatClassname( ((FactMemberQuery) nextFactMember).getFactType() )
									));
						}
					}
				}
				c.unindent();
				c.print( "}" );
				
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// Query methods" );
				c.print( "//----------------------------------------------------------------------" );
				c.print();
				
				for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
					
					// This will cover queries and predicates.
					if( nextFactMember instanceof FactMemberQuery ) {
                        FactMemberQuery currentQuery = (FactMemberQuery) nextFactMember;
                        if( currentQuery.getGeneratedFromMutable() == false ) {
    					    
    						String fieldJavaType = ParseTreeToJava.factMemberToJavaType(nextFactMember);
    						
    						c.print( String.format( "public %1$s %2$s() {" , fieldJavaType , idFormatter.formatIdentifier(nextFactMember.getIdentifier() ) ) );
    						c.indent();
    						{
    							if( nextFactMember instanceof FactMemberPredicate ) {
    								ExistenceModifier existsMod = ((FactMemberPredicate) nextFactMember).getExistenceModifier();
    								
    								if( existsMod == ExistenceModifier.POSITIVE ) {
    									c.print( String.format( "return this.%1$sResult.isEmpty();" ,
    											idFormatter.formatIdentifier( nextFactMember.getIdentifier() ) ) );	
    								}
    								else {
    									c.print( String.format( "return !this.%1$sResult.isEmpty();" ,
    											idFormatter.formatIdentifier( nextFactMember.getIdentifier() ) ) );	
    								}
    							}
    							else {
							        c.print( String.format( "return this.%1$sResult.getFactList();" ,
							                idFormatter.formatIdentifier( nextFactMember.getIdentifier() ) ) );
    							}
    						}
    						c.unindent();
    						c.print( "}" );
                        }
                        else {
                            
                            String fieldJavaListType = ParseTreeToJava.factMemberToJavaType(nextFactMember);
                            String fieldJavaType = currentQuery.getFactType();
                            
                            FactType queryTargetType = nextFactFile.getFactTypeByName(fieldJavaType);
                            FactMember targetType = queryTargetType.getFactMemberByName( "value" );
                            String typeToUse = null;
                            if( targetType instanceof FactMemberPrimitive ) {
                                FactMemberPrimitive primMember = (FactMemberPrimitive)targetType;
                                typeToUse = primMember.getPrimitiveDataType().getJavaDataType(FactCardinality.ONE);
                            }
                            else {
                                FactMemberComplex complexMember = (FactMemberComplex)targetType;
                                typeToUse = complexMember.getFactType();
                            }
                            
                            c.print( String.format( "public %1$s %2$s() {" , typeToUse , idFormatter.formatIdentifier(nextFactMember.getIdentifier() ) ) );
                            c.indent();
                            {
                                c.print( String.format( "%1$s returnList = this.%2$sResult.getFactList();" , 
                                        fieldJavaListType , idFormatter.formatIdentifier( nextFactMember.getIdentifier() ) ) );
                                c.print( String.format( "return returnList.isEmpty() == true ? null : returnList.get(0).getValue();" ) );
                            }
                            c.unindent();
                            c.print( "}" );
                        }
						c.print();
					}
				}
				
				c.print();
				c.print( "//----------------------------------------------------------------------" );
				c.print( "// Getter method declarations" );
				c.print( "//----------------------------------------------------------------------" );
				c.print();
				
				if( nextFactType.getFactOptionSet().hasOption(FactOption.UNIQUE) ) {
					c.print( "public UUID getUnique() {" );
					c.indent();
					{
						c.print( "return this.unique;" );
					}
					c.unindent();
					c.print( "}" );
					c.print();
				}
				
				// Getter methods for predecessors
				for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
					if( nextFactMember.getSection() == FactMemberSection.KEY ) {

						String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
						String fieldNameForGetter = idFormatter.formatIdentifierForGetterSetter( nextFactMember.getIdentifier() );
						String fieldJavaType = null;
						if( nextFactMember instanceof FactMemberPrimitive ) {
							
							PrimitiveType primitiveType = ((FactMemberPrimitive)nextFactMember).getPrimitiveDataType();
							fieldJavaType = primitiveType.getJavaDataType( nextFactMember.getCardinality() );

						}
						else if( nextFactMember instanceof FactMemberComplex ) {
							String javaFactTypeName = idFormatter.formatClassname(((FactMemberComplex) nextFactMember).getFactType());
							if( nextFactMember.getCardinality() == FactCardinality.MANY ) {
							    javaFactTypeName = "java.util.List<" + javaFactTypeName + ">";
							}
//							String predecessorType = "PredecessorObj";
//							
//							FactCardinality cardinality = ((FactMember) nextFactMember).getCardinality();
//							if( cardinality == FactCardinality.MANY ) {
//								predecessorType = "PredecessorList";
//							}
//							else if( cardinality == FactCardinality.OPTIONAL ) {
//								predecessorType = "PredecessorOpt";
//							}
							
							fieldJavaType = javaFactTypeName;
						}

						c.print( String.format( "public %1$s get%2$s() {" , fieldJavaType , fieldNameForGetter ) ) ;
						c.indent();
						{
							if( nextFactMember instanceof FactMemberPrimitive ) {
								c.print( String.format( "return this.%1$s;" , fieldName ) );
							}
							else {
							    if( nextFactMember.getCardinality() == FactCardinality.MANY ) {
                                    c.print( String.format( "return this.%1$s.getFactList();" , fieldName ) );
							    }
							    else {
							        c.print( String.format( "return this.%1$s.getFact();" , fieldName ) );
							    }
							}
						}
						c.unindent();
						c.print( "}" );
						c.print();
					}
				}
				
			}
			c.unindent();
			c.print( "}" ); // end of class 
		}
		catch( IOException e ) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
			return false;
		}
		return true;
	}

    private void outputQueryDefinition(FactCompileSpace compileSpace, FactFile factFile, JavaIdentifierFormatting idFormatter,
            CodeGeneratorWorkspace c, FactMember nextFactMember) throws IOException {

        FactFileVerifier verifier = new FactFileVerifier();

        FactMemberQuery query = (FactMemberQuery) nextFactMember;
        String queryName = idFormatter.formatIdentifier(nextFactMember.getIdentifier());

        c.print(String.format("public static QueryDefinition QUERY_%1$s() { ", queryName));
        c.indent();
        {
            c.print("return new QueryDefinition()");

            int currentOffset = 0;
            FactType workFactType = nextFactMember.getOwningFact();
            HashMap<String, FactType> aliasMap = new HashMap<String, FactType>();
            for (FactSet nextFactSet : query.getFactSets()) {

                String joinClause = null;

                // Update the fact alias map
                aliasMap.put(nextFactSet.getAlias(), factFile.getFactTypeByName(nextFactSet.getFactType()));

                // The FileVerifier will always ensure that the progression is
                // left to right.
                CompoundIdReference leftReferenceTemp = CompoundIdResolver.resolveToFactMember(verifier, compileSpace, factFile,
                        workFactType, nextFactSet.getLeftIdentifier(), aliasMap);
                CompoundIdReference rightReferenceTemp = CompoundIdResolver.resolveToFactMember(verifier, compileSpace, factFile,
                        workFactType, nextFactSet.getRightIdentifier(), aliasMap);

                // Create a sequence of left references to process...
                LinkedList<CompoundIdReference> leftReferenceSequence = new LinkedList<CompoundIdReference>();
                do {
                    if( leftReferenceTemp.isFactTypeReference() == false ) {
                        leftReferenceSequence.addFirst(leftReferenceTemp);
                    }
                } while ((leftReferenceTemp = leftReferenceTemp.getParentReference()) != null);

                // Create a sequence of right references to process...
                LinkedList<CompoundIdReference> rightReferenceSequence = new LinkedList<CompoundIdReference>();
                while (rightReferenceTemp != null) {
                    
                    if( rightReferenceTemp.isFactTypeReference() == false ) {
                        rightReferenceSequence.addLast(rightReferenceTemp);
                    }
                    rightReferenceTemp = rightReferenceTemp.getParentReference();
                }


                for (CompoundIdReference leftReference : leftReferenceSequence) {

                    // Predecssor join
                    if (nextFactSet.getCondition().getFactClauseCount() == 0) {
                        joinClause = String.format(".joinPredecessors( %1$s.ROLE_%2$s )",
                                idFormatter.formatClassname(leftReference.getFact().getFactType()),
                                idFormatter.formatIdentifier(leftReference.getFactMember().getIdentifier()));
                        c.print(joinClause);
                    } else {
                        // Predecessor with conditions

                        StringBuilder factConditionString = new StringBuilder();
                        factConditionString.append("new ConditionCollection()");
                        for (FactClause nextClause : nextFactSet.getCondition().getFactClauses()) {
                            FactType aliasedType = aliasMap.get(nextClause.getAlias());
                            String memberPredicate = nextClause.getMemberName();
                            FactMemberPredicate predicate = (FactMemberPredicate)aliasedType.getFactMemberByName(memberPredicate);
                            
                            ExistenceModifier exists = predicate.getExistenceModifier();
                            ConditionModifier isTrue = nextClause.getConditionModifier();
                            
							if ((isTrue == ConditionModifier.POSITIVE) ^ (exists == ExistenceModifier.POSITIVE)) {
                                // not (exists) or (not exists)
                                factConditionString.append(".isEmpty( ");
                            } else {
                                // (exists) or not (not exists)
                                factConditionString.append(".isNotEmpty( ");
                            }

                            factConditionString.append(String.format("%1$s.QUERY_%2$s()",
                                    idFormatter.formatClassname(aliasedType.getFactType()), idFormatter.formatIdentifier(memberPredicate)));

                        }
                        factConditionString.append(" )");

                        joinClause = String
                                .format(".joinPredecessors( %1$s.ROLE_%2$s, %3$s )",
                                        idFormatter.formatClassname(leftReference.getFact().getFactType()),
                                        idFormatter.formatIdentifier(leftReference.getFactMember().getIdentifier()),
                                        factConditionString.toString());
                        c.print(joinClause);
                    }
                }

                CompoundIdReference rightReference = null;
                Iterator<CompoundIdReference> rightReferenceIt = rightReferenceSequence.iterator();
                while (rightReferenceIt.hasNext()) {
                    rightReference = rightReferenceIt.next();

                    // successor join
                    if (nextFactSet.getCondition().getFactClauseCount() == 0) {
                        joinClause = String.format(".joinSuccessors( %1$s.ROLE_%2$s )",
                                idFormatter.formatClassname(rightReference.getFact().getFactType()),
                                idFormatter.formatIdentifier(rightReference.getFactMember().getIdentifier()));
                        c.print(joinClause);
                        
                    } else {
                        // Successor with conditions

                        StringBuilder factConditionString = new StringBuilder();
                        factConditionString.append("new ConditionCollection()");
                        for (FactClause nextClause : nextFactSet.getCondition().getFactClauses()) {
                            FactType aliasedType = aliasMap.get(nextClause.getAlias());
                            String memberPredicate = nextClause.getMemberName();
                            FactMemberPredicate predicate = (FactMemberPredicate)aliasedType.getFactMemberByName(memberPredicate);
                            
                            ExistenceModifier exists = predicate.getExistenceModifier();
                            ConditionModifier isTrue = nextClause.getConditionModifier();
                            
							if ((isTrue == ConditionModifier.POSITIVE) ^ (exists == ExistenceModifier.POSITIVE)) {
                                // not (exists) or (not exists)
                                factConditionString.append(".isEmpty( ");
                            } else {
                                // (exists) or not (not exists)
                                factConditionString.append(".isNotEmpty( ");
                            }

                            factConditionString.append(String.format("%1$s.QUERY_%2$s()",
                                    idFormatter.formatClassname(aliasedType.getFactType()), idFormatter.formatIdentifier(memberPredicate)));

                        }
                        factConditionString.append(" )");

                        joinClause = String.format(".joinSuccessors( %1$s.ROLE_%2$s , %3$s )",
                                idFormatter.formatClassname(rightReference.getFact().getFactType()),
                                idFormatter.formatIdentifier(rightReference.getFactMember().getIdentifier()),
                                factConditionString.toString());
                        c.print(joinClause);
                    }

                }

                ++currentOffset;
                if( rightReference != null ) {
                    workFactType = rightReference.getFact();
                }
            }
        }
        c.print( ";" );
        c.unindent();
        c.print("}");
    }

	private void writePrimitiveSerializationCode(FactType nextFactType,
			JavaIdentifierFormatting idFormatter, CodeGeneratorWorkspace c)
			throws IOException {
		
		String factTypeName = idFormatter.formatClassname(nextFactType.getFactType());
		
		c.print( "try {" );
		c.indent();
		{
			c.print( String.format( "%1$s newFact = new %1$s(factMemento);" , factTypeName ) );
			c.print();
			
			c.print( "DataInputStream in = new DataInputStream(new ByteArrayInputStream(factMemento.getData()));" );
			c.print( "try {" );
			c.indent();
			{
				// See if the class is marked as "unique"
				if( nextFactType.getFactOptionSet().hasOption( FactOption.UNIQUE ) ) {
					c.print( "newFact.unique = (UUID) fieldSerializerByType.get( UUID.class ).readData( in );"); 
				}
				
				// Loop through all fact members that are keys
				for( FactMember nextFactMember : nextFactType.getFactMembers() ) {
					if( nextFactMember.getSection() == FactMemberSection.KEY ) {
	
						// TODO I think I'm only interested in primitives here?  The ctor takes care of construction 
						// for the complex predecessors.
						if( nextFactMember instanceof FactMemberPrimitive ) {
							// Primitive predecessor											
							String fieldName = idFormatter.formatIdentifier( nextFactMember.getIdentifier() );
	
							//PrimitiveType primitiveType = ((FactMemberPrimitive)nextFactMember).getPrimitiveDataType();
							String fieldJavaType = ParseTreeToJava.factMemberToJavaObjectType(nextFactMember);
							
							c.print( String.format( "newFact.%s = (%s) fieldSerializerByType.get( %s.class ).readData(in);" , fieldName , fieldJavaType , fieldJavaType ) );
						}
					}
				}
			}
			c.unindent();
			c.print( "}" );
			c.print( "finally {" );
			c.indent();
			{
				c.print( "try { in.close(); } catch( IOException e ) {}" );
			}
			c.unindent();
			c.print( "}" );
			c.print( "return newFact;" );
		}
		c.unindent();
		c.print( "}" );
		c.print( "catch( IOException e ) {");
		c.indent();
		{
			c.print( String.format( "throw new CorrespondenceException(\"Failed to load fact of type %s.\", e);" , factTypeName) );
		}
		c.unindent();
		c.print( "}" );
	}
	
	private void writeNoPrimitivesSerializationCode(FactType nextFactType, JavaIdentifierFormatting idFormatter, CodeGeneratorWorkspace c)	throws IOException {
		String factTypeName = idFormatter.formatClassname(nextFactType.getFactType());
		
		c.print( String.format( "%1$s newFact = new %1$s(factMemento);" , factTypeName ) );
		c.print();
		c.print( "return newFact;" );
	}

	@Override
	protected boolean afterFactTypeCompile(FactFile nextFactFile, FactType nextFactType, CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context) {
		try {
			context.outputTo(factTypeOutputWriter);
			factTypeOutputWriter.close();
		}
		catch( IOException e ) {
			System.out.println( "Error attempting to close generated class file: " + factTypeOutputFile.getAbsolutePath() );
			return false;
		}
		finally {
			if( factTypeOutputWriter != null ) {
				factTypeOutputWriter.close();
			}
		}
		
		if( !super.afterFactTypeCompile(nextFactFile, nextFactType, commandLineParser, compileSpace, context) ) 
			return false;
		
		return true;
	}
	
	private boolean hasPrimitiveKeyFields( FactType factType ) {
		
		if( factType.getFactOptionSet().hasOption( FactOption.UNIQUE ) ) {
			return true;
		}
		for( FactMember nextFactMember : factType.getFactMembers() ) {
			
			if( ( nextFactMember.getSection() == FactMemberSection.KEY ) && ( nextFactMember instanceof FactMemberPrimitive ) ) {
				return true;
			}
		}
		return false;
	}

    protected boolean afterFactFileCompile( FactFile nextFactFile , CommandLineParser commandLineParser, FactCompileSpace compileSpace, CodeGeneratorWorkspace context  ) {
        boolean returnValue = super.afterCompileAll(commandLineParser, compileSpace, context);
        if( returnValue == false )
            return false;
        
        try {
            // Need to generate the Community object
            CodeGeneratorWorkspace moduleWorkspace = new CodeGeneratorWorkspace();
    
            JavaIdentifierFormatting idFormatter = new JavaIdentifierFormatting();
            String namespace = idFormatter.formatNamespace( nextFactFile.getNamespace().getIdentifier() );
            
            moduleWorkspace.printHeader( "package " + namespace + ";" );
            moduleWorkspace.printHeader();
            
            moduleWorkspace.print( "public class CorrespondenceModel implements com.updatecontrols.correspondence.Module {" );
            moduleWorkspace.indent();
            {
                moduleWorkspace.print( "public void registerTypes(com.updatecontrols.correspondence.Community community) " +
                		"throws com.updatecontrols.correspondence.CorrespondenceException {" );
                moduleWorkspace.indent();
                {
                    moduleWorkspace.print( "community" );
                    moduleWorkspace.indent();
                    {
                        for( FactType nextFactType : communityFacts ) {
                            moduleWorkspace.print( String.format( ".addType( %s.TYPE , new %s.CorrespondenceFactFactoryImpl() , new com.updatecontrols.correspondence.FactMetadata() )" ,
                                    idFormatter.formatClassname( nextFactType.getFactType() ),
                                    idFormatter.formatClassname( nextFactType.getFactType() )
                                    ) );
                        }
                        
                        for( FactType nextFactType : communityFacts ) {
                            
                            for( FactMember nextfactMember : nextFactType.getFactMembers() ) {
                                
                                if( nextfactMember instanceof FactMemberQuery ) {
                                    
                                    FactMemberQuery queryDef = (FactMemberQuery) nextfactMember;

                                    // TODO
                                    moduleWorkspace.print( String.format( ".addQuery( %s.TYPE , %s.QUERY_%s() )" ,
                                            idFormatter.formatClassname( nextFactType.getFactType() ),
                                            idFormatter.formatClassname( nextFactType.getFactType() ),
                                            idFormatter.formatIdentifier( queryDef.getIdentifier() )
                                            ) );
                                    
                                }
                            }
                            
                        }
                        
                        moduleWorkspace.print( ";" );
                    }
                    moduleWorkspace.unindent();
                }
                moduleWorkspace.unindent();
                moduleWorkspace.print( "}" );
            }
            moduleWorkspace.unindent();
            moduleWorkspace.print( "}" );
            
            // Write out the contents of the moduleWorkspace
            File communityFile = new File( factFileBaseNamespaceDirectory , new JavaIdentifierFormatting().formatClassname( "CorrespondenceModel" ) + ".java" );
            writeCodeWorkspace(compileSpace, moduleWorkspace, communityFile);
            
            communityFacts.clear();
            return true;
        }
        catch( IOException e ) {
            System.out.println( "IOException while outputing CorrespondenceModel class." );
            e.printStackTrace();
            return false;
             
        }
    }

    private boolean writeCodeWorkspace( FactCompileSpace compileSpace, CodeGeneratorWorkspace context, File outputFile ) {
        PrintWriter outWriter = null;
        try {
            outWriter = new PrintWriter(outputFile);
            context.outputTo(outWriter);
        }
        catch( Throwable t ) {
            System.out.println( "Unable to write CommunityModule file: " + outputFile.getAbsolutePath() );
            t.printStackTrace();
            return false;
        }
        finally {
            if( outWriter != null ) {
                outWriter.flush();
                outWriter.close();
            }
        }
        
        return true;
    }
}
