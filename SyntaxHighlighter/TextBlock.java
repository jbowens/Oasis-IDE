package camel.syntaxhighlighter;

public TextBlock {

  public enum TYPE { COMMENT, UNKNOWN };
  protected int startLine;
  protected int endLine;
  protected int startChar;
  protected int endChar;
  protected String text;

  public TextBlock() {
  }

  public String appendText(String text) {
    this.text = this.text + text;
  }

  public String getText() {
    return this.text;
  }

  public TextBlock.TYPE getType() {
    return UNKNOWN;
  }


}