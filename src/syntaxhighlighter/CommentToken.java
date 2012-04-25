package camel.syntaxhighlighter;

/**
 * Represents a comment token.
 */
public class CommentToken extends Token {

  /**
   * Creates a new comment token.
   */
  public CommentToken(int charNumber) {
    super(TokenType.COMMENT, charNumber, 2);
  }

}