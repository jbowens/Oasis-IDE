package camel.syntaxhighlighter;

/**
 * Represents a token of the output.
 */
public class Token {

  /* The type of the token */
  protected TokenType type;

  /* The character number where this token begins */
  protected int startChar;

  /* The length of this token */
  protected int length;

  /* The raw text of the token */
  protected String text;

  /* The pair number of this token. */
  protected byte pair;

  /**
   * Creates a new token.
   */
  public Token(TokenType type, int startChar, int length) {
    this.type = type;
    this.length = length;
    this.startChar = startChar;
  }

  public Token(TokenType type, byte pair) {
    this.type = type;
    this.pair = pair;
  }

  /**
   * Appends text to this token. This is used when
   * constructing unknown tokens before lexing.
   */
  public void appendText(String text) {
    this.text = this.text + text;
    this.length++;
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
   * Returns the length of this token.
   *
   * @return the number of characters in the token
   */
  public int getLength() {
    return length;
  }

  /**
   * A string representation of this token.
   */
  public String toString() {
    return text;
  }

}