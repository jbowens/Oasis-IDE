package camel.syntaxhighlighter;

import javax.swing.JTextPane;

public class OCamlTextPane extends JTextPane {
  
  public OCamlTextPane() {
    this.setEditorForContentType("text/plain", new OCamlEditorKit());
    this.setContentType("text/plain");
  }

}