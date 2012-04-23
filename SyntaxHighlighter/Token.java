package camel.syntaxhighlighter;

/**
 * Represents a token of the output.
 */
public class Token {

  /* The type of the token */
  protected TokenType type;

  /* The line this token begins */
  protected int startLine;

  /* The character on the line that token begins */
  protected int startChar;

  /* The raw text of the token */
  protected String text;

  /**
   * Creates a new token.
   */
  public Token(TokenType type, int startLine, int startChar) {
    this.type = type;
    this.startLine = startLine;
    this.startChar = startChar;
  }

  /**
   * Appends text to this token. This is used when
   * constructing unknown tokens before lexing.
   */
  public void appendText(String text) {
    this.text = this.text + text;
  }

  /**
   * Returns the text pertaining to this token.
   * This is only guaranteed to be available for tokens of
   * type UNKNOWN.
   */
  public String getText() {
    return this.text;
  }

  /**
   * Returns the type of this token.
   */
  public TokenType getType() {
    return type;
  }

  /**
   * A string representation of this token.
   */
  public String toString() {
    return text;
  }

}