grammar Puc;

init: prog;

prog: def* expr;

def: 'def' NAME ':' expr;

atom
  : NAME # Var
  | INT # IntLit
  | BOOL_LIT # BoolLit
  | TEXT_LIT # TextLit
  | 'fn' param=NAME '=>' body=expr # Lambda
  | fn=atom '(' arg=expr ')' # App
  | '(' inner=expr ')' # Parenthesized
  | 'if' condition=expr 'then' thenBranch=expr 'else' elseBranch=expr # If
  | 'let' NAME '=' bound=expr 'in' body=expr # Let
  ;

expr
  : atom # Unary
  | left=expr op='*' right=expr # Binary
  | left=expr op=('+' | '-' | '++') right=expr # Binary
  | left=expr op='==' right=expr # Binary
  | left=expr op='&&' right=expr # Binary
  | left=expr op='||' right=expr # Binary
  ;

TEXT_LIT: '"' ~('"')* '"';
BOOL_LIT: 'false' | 'true';
INT: [0-9]+;
NAME: [a-zA-Z_]+;
WS: [ \t\r\n] -> skip;