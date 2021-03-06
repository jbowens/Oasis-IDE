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
  public UnknownToken(int charStart, int length) {
    super(TokenType.UNKNOWN, charStart, length);
  }

  /**
   * Constructs a new unknown token with unknown length.
   */
  public UnknownToken(int charStart) {
    super(TokenType.UNKNOWN, charStart, 0);
  }

}