package camel.syntaxhighlighter;

import javax.swing.text.*;

public class OCamlEditorKit extends DefaultEditorKit implements ViewFactory {

  protected ViewFactory ocamlViewFactory;

  protected OCamlLexer lexer;

  protected StyleSet style;

  public OCamlEditorKit(OCamlLexer lexer, StyleSet style) {
    ocamlViewFactory = new OCamlViewFactory(style);
    this.lexer = lexer;
    this.style = style;
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
    return new OCamlView(element, style);
  }

}