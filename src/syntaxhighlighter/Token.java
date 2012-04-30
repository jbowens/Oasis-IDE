package camel.syntaxhighlighter;

/**
 * Represents a token of the output.
 */
public class Token implements Comparable {

  /* The type of the token */
  protected TokenType type;

  /* The character number where this token begins */
  protected int startChar;

  /* The length of this token */
  protected int length;

  /* The raw text of the token */
  protected StringBuilder text;

  /* The pair number of this token. */
  protected byte pair;

  /**
   * Creates a new token.
   */
  public Token(TokenType type, int startChar, int length) {
    this.type = type;
    this.length = length;
    this.startChar = startChar;
    text = new StringBuilder();
  }

  public Token(TokenType type, int startChar, int length, byte pair) {
    this.type = type;
    this.length = length;
    this.startChar = startChar;
    this.type = type;
    this.pair = pair;
    text = new StringBuilder();
  }

  /**
   * Appends text to this token. This is used when
   * constructing unknown tokens before lexing.
   *
   * @param text the text to append to the 
   */
  public void appendText(String text) {
    if( text == null )
      return;
      
    this.text.append(text);
    this.length += text.length();
  }

  /**
   * Returns the text pertaining to this token.
   * This is only guaranteed to be available for tokens of
   * type UNKNOWN.
   */
  public String getText() {
    return this.text.toString();
  }

  /**
   * Returns the type of this token.
   */
  public TokenType getType() {
    return type;
  }

  /**
   * Returns the character number where this token begins.
   *
   * @return the start character of this token
   */
  public int getStart() {
    return startChar;
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
    return String.valueOf(type) + " - " + getStart() + "..." + (getStart()+getLength());
  }

  /**
   * Sets a start for the token.
   *
   * @param start new start position
   */
  public void setStart(int start) {
    this.startChar = start;
  }

  /**
   * Sets the length of this token.
   *
   * @param length new length
   */
  public void setLength(int length) {
    this.length = length;  
  }

  @Override
  public boolean equals(Object obj) {
      if (obj instanceof Object) {
        Token token = (Token) obj;
          return ((this.getStart() == token.getStart()) &&
                  (this.getLength() == token.getLength()) &&
                  (this.getType() == token.getType()));
        } else
            return false;
    }

    @Override
    public int compareTo(Object o) {
        Token t = (Token) o;
        if (this.getStart() != t.getStart()) {
            return (this.getStart() - t.getStart());
        } else if (this.getLength() != t.getLength()) {
            return (this.getLength() - t.getLength());
        } else {
            return type.compareTo(t.getType());
        }
    }
}