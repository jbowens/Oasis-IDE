package camel.syntaxhighlighter;

import javax.swing.text.*;

public class OCamlEditorKit extends DefaultEditorKit implements ViewFactory {

  protected ViewFactory ocamlViewFactory;

  protected OCamlLexer lexer;

  public OCamlEditorKit(OCamlLexer lexer) {
    ocamlViewFactory = new OCamlViewFactory();
    this.lexer = lexer;
  }

  @Override
  public ViewFactory getViewFactory() {
    return ocamlViewFactory;
  }

  @Override
  public String getContentType() {
    return "plain/text";
  }

  /**
   * Called by Swing to get a document.
   */
  @Override
  public Document createDefaultDocument() {
    return new OCamlDocument(lexer);
  }

  public View create(Element element) {
    return new OCamlView(element);
  }

}