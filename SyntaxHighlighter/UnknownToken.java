package camel.syntaxhighlighter;

/**
 * Represents a token whose identity has not yet been determined.
 * An unknown token may be split into many tokens later in the
 * lexing process.
 */
public class UnknownToken extends Token {

  /**
   * Constructs a new unknown token.
   */
  public UnknownToken(int line, int c) {
    super(TokenType.UNKNOWN, line, c);
  }

}