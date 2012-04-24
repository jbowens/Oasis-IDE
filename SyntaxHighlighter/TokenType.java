package camel.syntaxhighlighter;

/**
 * The possible types a token can be.
 */
public enum TokenType {

  UNKNOWN,
  OPERATOR,
  DELIMETER,
  KEYWORD,
  IDENTIFIER,
  NUMBER,
  STRING,
  METASTRING,
  COMMENT,
  TYPE,
  SPECIAL_VALUE,
  DEFAULT;

}