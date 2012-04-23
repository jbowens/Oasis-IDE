package camel.syntaxhighlighter;

public class Comment extends TextBlock {

  public Comment(int lineNumber, int charNumber) {
    super(lineNumber, charNumber);
  }

  @Override
  public TextBlock.TYPE getType() {
    return TextBlock.TYPE.COMMENT;
  }

}