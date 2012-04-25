package camel.syntaxhighlighter;

/**
 * Represents a comment token.
 */
public class CommentToken extends Token {

  /**
   * Creates a new comment token.
   */
  public Comment(int lineNumber, int charNumber) {
    super(TokenType.COMMENT, lineNumber, charNumber);
  }

}