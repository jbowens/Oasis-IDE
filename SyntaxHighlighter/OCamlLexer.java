package camel.syntaxhighlighter;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is a lexer for OCaml.
 */
public class OCamlLexer {
  
  /* A special preprocess for identifying comments */
  protected CommentSeparator commentSeparator;

  /**
   * Creates a new OCaml Lexer
   */
  public OCamlLexer() {
    commentSeparator = new CommentSeparator();
  }

  /**
   * Lexes the given input and returns a list of the tokens represented.
   */
  public List<Token> lex(Reader input) {
    
    // Identify the comments in an initial sweep through the text
    List<Token> commentSeparatedTokens = commentSeparator.separateComments( input );

    ArrayList<Token> tokens = new ArrayList<Token>();

    for( Token t : commentSeparatedTokens ) {

      // Skip any empty tokens
      if( t.getLength() == 0 )
        continue;

      // If it's a comment, no need to lex it.
      if( t.getType() == TokenType.COMMENT ) {
        tokens.add( t );
        continue;
      }

      // TODO: Something else

    }

    return tokens;

  }

}