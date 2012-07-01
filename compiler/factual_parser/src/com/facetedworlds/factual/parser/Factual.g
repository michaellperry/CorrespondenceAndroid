// --------------------------------------------------------------------------
// (c) 2009-2011 Michael L. Perry and Faceted Worlds, LLC.  
// All Rights Reserved
//
// MIT License
// --------------------------------------------------------------------------

grammar Factual;

@lexer::header {
	package com.facetedworlds.factual.parser;
}

@parser::header {
	package com.facetedworlds.factual.parser;
	import com.facetedworlds.factual.parsetree.*;
	import java.util.Stack;
}

@parser::members {
	private ParserState parserState = new ParserState();
	private String parserFilename = "";
	
	public void reset() {
		parserState = new ParserState();
	}
	
	public FactFile getFactFile() {
		return parserState.getFactFile();
	}
	
	public int getLine( RuleReturnScope s ) {
		return ((Token)s.getStart()).getLine();
	}
	
	public int getColumn( RuleReturnScope s ) {
		return ((Token)s.getStart()).getCharPositionInLine();
	}
	
	public int getLine( Token s ) {
		return s.getLine();
	}
	
	public int getColumn( Token s ) {
		return s.getCharPositionInLine();
	}
	
	public void setParserFilename( String filename ) {
		this.parserFilename = filename;
	}
	
	public void emitErrorMessage(String msg) {
        System.out.println(parserFilename + " " + msg);
    }	
}

// --------------------------------------------------------------------------
// Lexical Rules
// --------------------------------------------------------------------------

WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;


LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;

ID  :   ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

SEMI    :   ';';
COLON   :   ':';
DOT 	:   '.';
LCURLY  :   '{';
RCURLY  :   '}';
COMMA   :   ',';
EQUALS  :   '=';
LPAREN  :   '(';
RPAREN  :   ')';

MANY    :   '*';
OPTIONAL:   '?';

// --------------------------------------------------------------------------
// Parser Rules
// --------------------------------------------------------------------------

parseFactual
    : root_decl
    ; 
    
root_decl
    :   namespace_decl import_decl* fact_decl+
    ;

// ------------------------------------------------------------------------------------------
// 'Namespace' related non-terminals
// ------------------------------------------------------------------------------------------
    
namespace_decl
    :   'namespace' namespaceName=dotted_identifier SEMI {
		parserState.addNamespace( new Namespace( $namespaceName.text , getLine(namespaceName), getColumn(namespaceName) ) );     
    } 
    ;  
    

// ------------------------------------------------------------------------------------------
// 'import' related non-terminals
// ------------------------------------------------------------------------------------------
 
import_decl
	:	'import' namespace=dotted_identifier {
		parserState.addFactImportDeclaration( new FactImportDeclaration( $namespace.text , getLine( namespace ), getColumn( namespace ) ) );
	}  LCURLY import_alias_spec+ RCURLY 
	;    
    
import_alias_spec
	: originalFactType=ID ( 'as' localFactType=ID )? SEMI {
		parserState.addFactImportAliasDeclaration( new FactImportAliasDeclaration( $originalFactType.text , $localFactType.text , getLine( originalFactType ), getColumn( originalFactType ) ) );
	}
	;
	        
// ------------------------------------------------------------------------------------------
// 'Fact' related non-terminals
// ------------------------------------------------------------------------------------------
fact_decl
    :   'fact' factName=ID {
    	parserState.startFact(new FactType( $factName.text , getLine(factName), getColumn(factName) ) );
    }
    fact_block
    {
    	parserState.completeFact();
    }
    ;

fact_block
    :   LCURLY ( segment_decl )+ RCURLY
    ;
    
segment_decl
	: key_section_decl
	  mutable_section_decl?
      query_section_decl?	
	;
	
key_section_decl 
	: 'key' COLON {
		parserState.setCurrentFactMemberSection( FactMemberSection.KEY );
	}
		( pattern_options_decl | primitive_decl | complex_decl )*
	;
	
mutable_section_decl
	: 'mutable' COLON {
		parserState.setCurrentFactMemberSection( FactMemberSection.MUTABLE );
	}
		( primitive_decl | complex_decl | delete_decl | undelete_decl )*
	;
	
query_section_decl
	: 'query' COLON {
		parserState.setCurrentFactMemberSection( FactMemberSection.QUERY );
	}
		( query_set_decl | predicate_decl )*
	;	
	
pattern_options_decl
	:	unique_decl
	;
	
unique_decl
	:	'unique' SEMI {
		parserState.getCurrentFact().getFactOptionSet().addOption( FactOption.UNIQUE );
	}
	;
	
delete_decl
	: 'delete' SEMI {
		parserState.getCurrentFact().getFactOptionSet().addOption( FactOption.DELETE );
		}
	;
	
undelete_decl
	: 'undelete' SEMI {
		parserState.getCurrentFact().getFactOptionSet().addOption( FactOption.UNDELETE );
		}
	;

// ------------------------------------------------------------------------------------------
// Primitive fact member declaration
// ------------------------------------------------------------------------------------------
primitive_decl
	:
		{
			FactCardinality c = FactCardinality.ONE;
		}
		( publishFlag = 'publish' )?
	    primitive_datatype_decl ( MANY {c = FactCardinality.MANY; } | OPTIONAL {c = FactCardinality.OPTIONAL; } )? identifier=ID SEMI {
		parserState.addPrimitiveFactMember( $identifier.text , $publishFlag.text, c , getLine( identifier ) , getColumn( identifier ) );
	}
	;

// ------------------------------------------------------------------------------------------
// Complex fact member declaration
// ------------------------------------------------------------------------------------------
complex_decl
	:	
	{
		FactCardinality c = FactCardinality.ONE;
	}
	( publishFlag = 'publish' )? factType=ID 
		( 
			MANY {c = FactCardinality.MANY; } | 
			OPTIONAL {c = FactCardinality.OPTIONAL; } )? 
			identifier=ID SEMI {
				parserState.addComplexFactMember( $factType.text , $identifier.text , $publishFlag.text , c , getLine( factType ) , getColumn( factType ) );
			}
	;

// ------------------------------------------------------------------------------------------
// Query set fact member declaration
// ------------------------------------------------------------------------------------------

query_set_decl
	:  factType=ID MANY identifier=ID LCURLY {
		parserState.addQueryFactMember( $factType.text , $identifier.text , getLine( factType ) , getColumn( factType ) );
	}
		(fact_set_decl)+
		RCURLY
	;

fact_set_decl
	:	factType=ID alias=ID COLON leftIdentifier=dotted_identifier EQUALS rightIdentifier=dotted_identifier {
		parserState.addQueryFactSet( $factType.text , $alias.text , $leftIdentifier.text , $rightIdentifier.text , getLine( factType ) , getColumn( factType ) );
	}
			(fact_condition_decl)?
	;

fact_condition_decl
	:	'where' fact_clause_decl ( 'and' fact_clause_decl )*
	;

fact_clause_decl
	:  (existence='not')? alias=ID DOT memberName=ID {
		parserState.addQueryFactClause( $alias.text , $memberName.text , $existence.text , getLine(alias), getColumn(alias) );
	}
	;
	
// ------------------------------------------------------------------------------------------
// Predicate set fact member declaration
// ------------------------------------------------------------------------------------------

predicate_decl
	: 'bool' identifier=ID LCURLY {
		parserState.addPredicateFactMember( $identifier.text , getLine( identifier ) , getColumn( identifier ) );
	} 
		(predicate_set_decl)+
		RCURLY 
	;
	
predicate_set_decl
	:	(notFlag='not')? 'exists' {
		parserState.applyExistenceModifierToPredicate( $notFlag.text );  
	}
		(fact_set_decl)+
	;
// ------------------------------------------------------------------------------------------
// General Use Non-Terminal Symbols
// ------------------------------------------------------------------------------------------

dotted_identifier
    :   ID ( DOT ID )*
    ;
    
primitive_datatype_decl
    :   primitiveType = ( 'byte' | 'string' | 'int' | 'long' | 'float' | 'double' | 'decimal' | 'char' | 'date' | 'time' | 'bool' ) {
    	parserState.setCurrentFactMemberPrimitiveType( $primitiveType.text );
    }
	;

