package camel.syntaxhighlighter;

public Comment extends TextBlock {

  public Comment() {
  }

  @override
  public TextBlock.TYPE getType() {
    return TextBlock.TYPE.COMMENT;
  }

}