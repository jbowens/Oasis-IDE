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
  public Iterator<Token> getTokens(int p0, int p1) {
    return new TokenIterator(p0, p1);
  }

  /**
   * This class is used to iterate over tokens between two positions
   */
  class TokenIterator implements ListIterator<Token> {

    int start;
    int end;
    int ndx = 0;

    @SuppressWarnings("unchecked")
    private TokenIterator(int start, int end) {
      this.start = start;
      this.end = end;
      if (tokens != null && !tokens.isEmpty()) {
        Token token = new Token(TokenType.COMMENT, start, end - start);
        ndx = Collections.binarySearch((List) tokens, token);
        // we will probably not find the exact token...
        if (ndx < 0) {
          // so, start from one before the token where we should be...
          // -1 to get the location, and another -1 to go back..
          ndx = (-ndx - 1 - 1 < 0) ? 0 : (-ndx - 1 - 1);
          Token t = tokens.get(ndx);
          // if the prev token does not overlap, then advance one
          if ((t.getStart()+t.getLength()) <= start) {
            ndx++;
          }

        }
      }
    }

    @Override
    public boolean hasNext() {
      if (tokens == null) {
        return false;
      }
      if (ndx >= tokens.size()) {
        return false;
      }
      Token t = tokens.get(ndx);
      if (t.getStart() >= end) {
        return false;
      }
      return true;
    }

    @Override
    public Token next() {
      return tokens.get(ndx++);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPrevious() {
      if (tokens == null) {
        return false;
      }
      if (ndx <= 0) {
        return false;
      }
      Token t = tokens.get(ndx);
      if ((t.getStart()+t.getLength()) <= start) {
        return false;
      }
      return true;
    }

    @Override
    public Token previous() {
      return tokens.get(ndx--);
    }

    @Override
    public int nextIndex() {
      return ndx + 1;
    }

    @Override
    public int previousIndex() {
      return ndx - 1;
    }

    @Override
    public void set(Token e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(Token e) {
      throw new UnsupportedOperationException();
    }
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

  /**
   * Counts the number of lines in the document.
   */
  public int countLines() {
    int len = getLength();
    try {
      if( len == 0 )
        return 1;
      else {
        String txt = getText(0, len);
        int count = 1;
        for( int i = 0; i < len; i++ )
          if( txt.charAt(i) == '\n' )
            count++;
        return count;
      }
    } catch( BadLocationException ex ) {
      return 0;
    }
  }

  /**
   * Gets the line position associated with a caret position
   *
   * @param charOffset the number of characters from the start
   * @return the line number the given position is on
   */
  public int getLinePosition( int charOffset ) {
    CaretPosition caretPos = getCaretPosition(charOffset);
    if( caretPos == null )
      return -1;
    else
      return caretPos.line;
  }

  /**
   * Gets the column position associated with a caret position
   *
   * @param charOffset the number of characters from the start
   * @return the column number the given position is on
   */
  public int getColumnPosition( int charOffset ) {
    CaretPosition caretPos = getCaretPosition(charOffset);
    if( caretPos == null )
      return -1;
    else
      return caretPos.column;
  }

  /**
   * Gets the caret position from a character offset.
   *
   * @param charOffset the number of characters from the start
   * @return a CaretPosition object representing the line and column
   */
  public CaretPosition getCaretPosition( int charOffset ) {
    int len = getLength();
    try {
      String txt = getText(0, len);
      int line = 1;
      int column = 1;
      for( int i = 0; i < len && i < charOffset; i++ ) {
        if( txt.charAt(i) == '\n' ) {
          line++;
          column = 1;
        } else
          column++;
      }
      return new CaretPosition(line, column);
    } catch( BadLocationException ex ) {
      return null;
    }
  }

  /**
   * A utility class for representing a line and column pair.
   */
  public class CaretPosition {
    public final int line;
    public final int column;
    public CaretPosition(int l, int c) {
      line = l;
      column = c;
    }
  }

}