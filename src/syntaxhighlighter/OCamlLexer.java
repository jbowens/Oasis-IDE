package camel.syntaxhighlighter;

import java.util.ArrayList;
import java.io.StringReader;
import java.io.Reader;
import java.util.List;

/**
 * This class can be used to lex an entire OCaml document. It is used by the
 * syntax highlighter to convert raw text to tokens. The tokens can then be
 * used to highlight text based on the token it's a part of.
 */
public class OCamlLexer {
  
  /* A comment separator to preprocess inputs  */
  protected CommentSeparator commentSeparator;

  /* A lexer for everything but comments */
  protected CommentlessOCamlLexer lexer;

  /** 
   * Constructs a new OCaml lexer.
   */
  public OCamlLexer() {
    /* Create the needed resources to lex */
    commentSeparator = new CommentSeparator();
    lexer = new CommentlessOCamlLexer();
  }

  /**
   * Tokenizes the input from the reader and returns the tokens in the given
   * token list.
   *
   * @input input a reader for the text that should be tokenized
   * @input tokens a list of tokens to be populated with the tokens of input
   */
  public void tokenize(Reader input, List<Token> tokens) {

    /* Temporary list of tokens returned from comment separation preprocess */
    ArrayList<Token> initialTokens = new ArrayList<Token>();

    /* First take care of comments */
    commentSeparator.separateComments(input, initialTokens);

    /* Process the comment separated tokens */
    for( Token t : initialTokens ) {

      if( t.getType() == TokenType.COMMENT )
        tokens.add( t );
      else if( t.getType() == TokenType.UNKNOWN )
        lexer.parse(new StringReader(t.getText()), tokens);
      else
        tokens.add( t );

    }

  }

}