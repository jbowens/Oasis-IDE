package camel.syntaxhighlighter;

public class TextBlock {

  public enum TYPE { COMMENT, UNKNOWN };
  protected int startLine;
  protected int startChar;
  protected String text;

  public TextBlock(int startLine, int startChar) {
    this.startLine = startLine;
    this.startChar = startChar;
  }

  public void appendText(String text) {
    this.text = this.text + text;
  }

  public String getText() {
    return this.text;
  }

  public TextBlock.TYPE getType() {
    return TextBlock.TYPE.UNKNOWN;
  }

  public String toString() {
    return text;
  }

}