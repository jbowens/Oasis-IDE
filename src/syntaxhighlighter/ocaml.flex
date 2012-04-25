package camel.syntaxhighlighter;
 
%% 

%public
%class OCamlLexer
%extends JFlexLexer
%final
%unicode
%char
%type Token

%{
    /**
     * Create an empty lexer, yyrset will be called later to reset and assign
     * the reader
     */
    public OCamlLexer() {
        super();
    }

    @Override
    public int yychar() {
        return yychar;
    }

    private static final byte PARAN     = 1;
    private static final byte BRACKET   = 2;
    private static final byte CURLY     = 3;

%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]+

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9_]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F_]

OctIntegerLiteral = 0 [oO] {OctDigit} {1,15}
OctLongLiteral    = 0 [oO] {OctDigit} {1,21} [lL]
OctDigit          = [0-7_]

BinIntegerLiteral = 0 [bB] {BinDigit} {1,50}
BinDigit          = [0-1_]

/* Generic type limits */
GenericType       = \'[a-zA-Z]

/* floating point literals */        
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9_]+ \. [0-9_]* 
FLit2    = \. [0-9_]+ 
FLit3    = [0-9_]+ 
Exponent = [eE] [+-]? [0-9_]+

/* string and character literals */
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

%state STRING, CHARLITERAL

%%

<YYINITIAL> {
  
  "and"           |
  "as"            |
  "assert"        |
  "begin"         |
  "class"         |
  "constraint"    |
  "do"            |
  "done"          |
  "downto"        |
  "else"          |
  "end"           |
  "exception"     |
  "external"      |
  "false"         |
  "for"           |
  "fun"           |
  "function"      |
  "functor"       |
  "if"            |
  "in"            |
  "include"       |
  "inherit"       |
  "initializer"   |
  "lazy"          |
  "let"           |
  "match"         |
  "method"        |
  "module"        |
  "mutable"       |
  "new"           |
  "object"        |
  "of"            |
  "open"          |
  "or"            |
  "private"       |
  "rec"           |
  "sig"           |
  "struct"        |
  "then"          |
  "to"            |
  "true"          |
  "try"           |
  "type"          |
  "val"           |
  "virtual"       |
  "when"          |
  "while"         |
  "with"          { return token(TokenType.KEYWORD); }

  "int"           |
  "char"          |
  "string"        |
  "float"         |
  "bool"          |
  "unit"          |
  "()"            |
  "exn"           |
  "array"         |
  "list"          |
  "option"        |
  "int32"         |
  "int64"         |
  "nativeint"     |
  "lazy_t"        { return token(TokenType.TYPE); }

  "-"                     |
  "+"                     |
  "*"                     |
  "/"                     |
  "mod"                   |
  "abs"                   |
  "not"                   |
  "&&"                    |
  "compare"               |
  "raise"                 |
  "failwith"              |
  "||"                    |
  "**"                    |
  "sqrt"                  |
  "-."                    |
  "*."                    |
  "/."                    |
  "+."                    |
  "-."                    |
  "~-"                    |
  "~+"                    |
  "max_int"               |
  "min_int"               |
  "land"                  |
  "lor"                   |
  "lxor"                  |
  "lnot"                  |
  "lsl"                   |
  "lsr"                   |
  "asr"                   |
  "~-."                   |
  "~+."                   |
  "ceil"                  |
  "floor"                 |
  "exp"                   |
  "expm1"                 |
  "log"                   |
  "log10"                 |
  "log1p"                 |
  "cos"                   |
  "sin"                   |
  "tan"                   |
  "acos"                  |
  "asin"                  |
  "atan"                  |
  "atan2"                 |
  "cosh"                  |
  "sinh"                  |
  "tanh"                  |
  "abs_float"             |
  "mod_float"             |
  "frexp"                 |
  "ldexp"                 |
  "modf"                  |
  "classify_float"        |
  "truncate"              |
  "int_of_char"           |
  "char_of_int"           |
  "int_of_string"         |
  "string_of_int"         |
  "float_of_string"       |
  "string_of_float"       |
  "bool_of_string"        |
  "string_of_bool"        |
  "<"                     |
  "<="                    |
  "="                     |
  "<>"                    |
  ">="                    |
  ">"                     |
  "=="                    |
  "!="                    |
  "max"                   |
  "min"                   |
  "^"                     |
  "::"                    |
  "@"                     |
  "String.concat"         |
  "String.length"         |
  "String.get"            |
  "String.set"            |
  "String.index"          |
  "String.rindex"         |
  "String.contains"       |
  "String.sub"            |
  "String.make"           |
  "String.uppercase"      |
  "String.lowercase"      |
  "String.capitalize"     |
  "String.uncapitalize"   |
  "Char.uppercase"        |
  "Char.lowercase"        |
  "Char.escaped"          |
  "List.length"           |
  "List.hd"               |
  "List.tl"               |
  "List.nth"              |
  "List.rev"              |
  "fst"                   |
  "snd"                   |
  "print_char"            |
  "print_int"             |
  "print_float"           |
  "print_string"          |
  "print_endline"         |
  "print_newline"         |
  "prerr_char"            |
  "prerr_string"          |
  "prerr_int"             |
  "prerr_float"           |
  "prerr_endline"         |
  "prerr_newline"         |
  "read_line"             |
  "read_int"              |
  "read_float"            |
  "ignore"                |
  "ref"                   |
  "!"                     |
  ":="                    |
  "incr"                  |
  "decr"                  |
  "exit"                  { return token(TokenType.OPERATOR); }

  "[]"                    |
  "infinity"              |
  "neg_infinity"          |
  "nan"                   |
  "max_float"             |
  "min_float"             |
  "epsilon_float"         |
  "in_channel"            |
  "out_channel"           |
  "stdin"                 |
  "stdout"                |
  "stderr"                { return token(TokenType.SPECIAL_VALUE); }

  "("                     |
  ")"                     |
  "["                     |
  "]"                     |
  "{"                     |
  "}"                     |
  ";"                     |
  ":"                     |
  "|"                     { return token(TokenType.DELIMETER); }

    /* string literal */
  \"                             {  
                                    yybegin(STRING); 
                                    tokenStart = yychar; 
                                    tokenLength = 1; 
                                 }

 {GenericType}                   { return token(TokenType.TYPE); }

  /* character literal */
  \'                             {  
                                    yybegin(CHARLITERAL); 
                                    tokenStart = yychar; 
                                    tokenLength = 1; 
                                 }

  /* numeric literals */

  {DecIntegerLiteral}            |
  {DecLongLiteral}               |
  
  {HexIntegerLiteral}            |
  {HexLongLiteral}               |
 
  {OctIntegerLiteral}            |
  {OctLongLiteral}               |

  {BinIntegerLiteral}            |
  
  {FloatLiteral}                 |
  {DoubleLiteral}                |
  {DoubleLiteral}[dD]            { return token(TokenType.NUMBER); }

  /* whitespace */
  {WhiteSpace}                   { }

  /* identifiers */ 
  {Identifier}                   { return token(TokenType.IDENTIFIER); }
}


<STRING> {
  \"                             { 
                                     yybegin(YYINITIAL); 
                                     // length also includes the trailing quote
                                     return token(TokenType.STRING, tokenStart, tokenLength + 1);
                                 }
  
  {StringCharacter}+             { tokenLength += yylength(); }

  \\[0-3]?{OctDigit}?{OctDigit}  { tokenLength += yylength(); }
  
  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

<CHARLITERAL> {
  \'                             { 
                                     yybegin(YYINITIAL); 
                                     // length also includes the trailing quote
                                     return token(TokenType.STRING, tokenStart, tokenLength + 1);
                                 }
  
  {SingleCharacter}+             { tokenLength += yylength(); }
  
  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

