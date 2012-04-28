package camel.syntaxhighlighter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import java.io.StringReader;
import java.io.Reader;
import java.io.IOException;

public class OCamlDocument extends PlainDocument {
  
  /* The lexer used to lex this document */
  protected OCamlLexer lexer;

  /* The current list of this document's tokens */
  protected List<Token> tokens;

  /**
   * Constructs a new OCamlDocument.
   *
   * @param lexer an OCaml lexer
   */
  public OCamlDocument(OCamlLexer lexer) {
    super();
    putProperty(PlainDocument.tabSizeAttribute, 4);
    this.lexer = lexer;
  }

  /**
   * Returns the document's tokens.
   *
   * @return a list of the tokens created from the document
   */
  public List<Token> getTokens() {
    return tokens;
  }

  /**
   * Parses the document into tokens
   */
  protected void parse() {
    // if we have no lexer, then we must have no tokens...
    if (lexer == null) {
      tokens = null;
      return;
    }

    List<Token> toks = new ArrayList<Token>(getLength() / 10);
    int len = getLength();
    try {
      Reader textReader = new StringReader( getText(0, getLength() ) );
      lexer.tokenize(textReader, toks);
    } catch (BadLocationException ex) {
    } catch (IOException ex) {
    } finally {
      tokens = toks;
    }
  }

  @Override
  protected void fireChangedUpdate(DocumentEvent e) {
    parse();
    super.fireChangedUpdate(e);
  }

  @Override
  protected void fireInsertUpdate(DocumentEvent e) {
    parse();
    super.fireInsertUpdate(e);
  }

  @Override
  protected void fireRemoveUpdate(DocumentEvent e) {
    parse();
    super.fireRemoveUpdate(e);
  }

}