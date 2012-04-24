package camel.syntaxhighlighter;

 
%% 

%public
%class OCamlLexer
%extends DefaultJFlexLexer
%final
%unicode
%char
%type Token

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]+

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

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
  "with"          { return token(TokenType.KEYWORD) }

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
  "lazy_t"        { return token(TokenType.TYPE) }

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
  "exit"                  { return token(TokenType.OPERATOR) }

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
  "stderr"                { return token(TokenType.SPECIAL_VALUE) }
}