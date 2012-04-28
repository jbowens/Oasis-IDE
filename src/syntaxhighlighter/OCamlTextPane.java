package camel.syntaxhighlighter;

import javax.swing.JTextPane;

public class OCamlTextPane extends JTextPane {
  
  public OCamlTextPane(OCamlLexer lexer) {
    this.setEditorKitForContentType("text/plain", new OCamlEditorKit(lexer));
    this.setContentType("text/plain");
  }

}