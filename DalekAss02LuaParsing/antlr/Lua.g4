grammar Lua;

// BNF source: Offical Lua BNF grammar @http://www.lua.org/manual/5.2/manual.html#3.1

@header {
	package ptt.dalek.antlr;
	
}

chunk 
	: block EOF
	;

block 
    : (stat)* retstat?
	;

stat 
	: ';'
	| varlist '=' explist
	| functioncall
	| label
	| 'break'
	| 'goto' NAME
	| 'do' block 'end'
	| 'while' exp 'do' block 'end'
	| 'repeat' block 'until' exp
	| 'if' exp 'then' block ('elseif' exp 'then' block)* ('else' block)? 'end'
	| 'for' NAME '=' exp ',' exp  (',' exp)? 'do' block 'end'
	| 'for' namelist 'in' explist 'do' block 'end'
	| 'function' funcname funcbody
	| 'local function' NAME funcbody
	| 'local' namelist ('=' explist)? 
	;
 
retstat 
	: 'return' explist? (';')? 
	;

label 
	: '::' NAME '::'
	;

funcname 
	: NAME ('.' NAME)* (':' NAME)? 
	;

varlist 
	: var (',' var)* 
	;

namelist 
	: NAME (',' NAME)* 
	;

explist 
	: exp (',' exp)* 
	;

exp 
	: 'nil' 
	| 'false' 
	| 'true' 
	| NUMBER 
	| STRING 
	| '...' 
	| functiondef
	| prefixexp 
	| tableconstructor 
	| exp binop exp 
	| unop exp ;

/*
note: this 

var :
	NAME | prefixexp '[' exp ']' | prefixexp '.' NAME ;

prefixexp :
	var | functioncall | '(' exp ')' ;

functioncall :
	prefixexp args | prefixexp ':' NAME args ;
*/

var 
	: NAME | prefixexp '[' exp ']' | prefixexp '.' NAME 
	;

prefixexp 
	: NAME | prefixexp '[' exp ']' | prefixexp '.' NAME		//var
	| prefixexp args | prefixexp ':' NAME args				//functioncall
	| '(' exp ')' 
	;

functioncall 
	: prefixexp args | prefixexp ':' NAME args 
	;

args 
	: '(' explist? ')' | tableconstructor | STRING
	; 
	
functiondef 
	: 'function' funcbody 
	;

funcbody 
	: '(' parlist? ')' block 'end' 
	;

parlist 
	: namelist (',' '...')? | '...' 
	;

tableconstructor 
	: '{' fieldlist? '}' 
	;

fieldlist 
	: field (fieldsep field)* fieldsep? 
	;

field 
	: '[' exp ']' '=' exp | NAME '=' exp | exp 
	; 

fieldsep 
	: ',' | ';' 
	;

binop
	: '+' 
	| '-' 
	| '*' 
	| '/' 
	| '^' 
	| '%' 
	| '..'
	| '<' 
	| '<=' 
	| '>' 
	| '>=' 
	| '==' 
	| '~='
    | 'and' 
    | 'or' 
    ;

unop     
	: '-' 
	| 'not' 
	| '#' 
	;

// multiline fehlt
STRING   
	: ('"' (~'"')* '"') 
	| ('\'' (~'\'')* '\'') 
	;

NAME     
	: ([a-z] | [A-Z] | '_') ([a-z] | [A-Z] | [0-9] | '_')* 
	;

NUMBER   
	: LUANUMBER 
	| HEXNUMBER 
	;

// unvollständig
HEXNUMBER 
	: '0' [xX] HEXDIGITS ('.' HEXDIGITS+)? ([pP] [-+]? DIGITS)? ;

LUANUMBER 
	: (DIGITS ('.'| ('.' DIGITS)?)
	| DIGITS? '.' DIGITS) ([eE] [-+]? DIGITS)?
	;

fragment DIGIT 
	: [0-9] 
	;
	
fragment HEXDIGIT 
	: [a-f] 
	| [A-F] 
	| DIGIT 
	;
	
fragment DIGITS 
	: DIGIT+ 
	;
	
fragment HEXDIGITS 
	: HEXDIGIT+ 
	;

// skip whitespace + comments
WS      
	: (' '|'\r'? '\n'|'\t'|('--[[')(.)*?(']]')|('--')(.)*?('\n'))+ { skip(); }
	;