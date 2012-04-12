package camel.syntaxhighlighter;

import java.io.Reader;
import java.util.List;
import java.util.LinkedList;

/**
 * Splits an OCaml document by the comments. (Necessary because OCaml allows nested
 * comments) This can be used to preprocess OCaml before passing off the text to a
 * lexer.
 */
public class CommentSeparator {
  
  public CommentSeparator() { }

  /**
   * Given a reader that reads an OCaml Program, this method will return
   * a list of TextBlocks containing the text of the program. It will be
   * separated into comment or unknown types.
   */
  public List<TextBlock> separateComments(Reader r) {
      
      /* Records how many comments deep we are. */
      int commentStack = 0;

      List<TextBlock> els = new LinkedList<TextBlock>();

      TextBlock prev = null;
      TextBlock current = new TextBlock();
      els.add(current);
      char lastlastChar = '\0';
      char lastChar = '\0';
      char nextChar = (char) r.read();
      while( nextChar != '\0' ) {

        if( lastChar == '(' && nextChar == '*' ) {
          commentStack++;

          if( commentStack == 1 ) {
            // this is the first comment in this stack. Let's create a new comment block
            prev = current;
            current = new Comment();
            els.add(current);
          }

          current.appendText( String.valueOf( lastChar ) );

        } else if( lastChar == '*' && nextChar == ')' && lastlastChar != '(' ) {
          commentStack--;

          if( commentStack == 0 ) {
            // We just got out of a deep comment stack, so let's create a new text block
            prev = current;
            current = new TextBlock();
            els.add(current);
          }

          prev.appendText( String.valueOf( lastChar ) );
          prev.appendText( String.valueOf( nextChar ) );

          // move forward the character twice on this one
          lastlastChar = lastChar;
          lastChar = nextChar;
          nextChar = (char) reader.read();

        } else {
          current.appendText( String.valueOf( lastChar ) );
        }

        lastlastChar = lastChar;
        lastChar = nextChar;
        nextChar = (char) r.read();
      }

      current.appendText( String.valueOf( lastChar ) );x

      return els;

  }

}