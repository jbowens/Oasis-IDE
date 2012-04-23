package camel.syntaxhighlighter;

public class Comment extends TextBlock {

  public Comment() {
  }

  @Override
  public TextBlock.TYPE getType() {
    return TextBlock.TYPE.COMMENT;
  }

}