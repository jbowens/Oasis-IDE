package camel.syntaxhighlighter;

/**
 * Represents a comment token.
 */
public class CommentToken extends Token {

  /**
   * Creates a new comment token.
   */
  public Comment(int charNumber, int length) {
    super(TokenType.COMMENT, charNumber, length);
  }

  /**
   * Creates a new comment of unknown length.
   */
  public Comment(int charNumber) {
    super(TokenType.COMMENT, charNumber, 0);
  }

}