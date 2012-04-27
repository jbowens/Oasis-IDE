package camel.tests;

import org.junit.*;
import java.util.*;
import org.junit.Assert.*;
import java.io.StringReader;
import java.util.ArrayList;

import camel.syntaxhighlighter.*;

public class LexerTests {

    @Test
    public void testSimpleProgram() {
      
        String simpleProg = "let rec length(alist : 'a list) = match alist with\n| [] -> 0\n| _::tl -> 1 + length tl;;\n\nlength( [5; 4; 3; 2; 1] );;";

        StringReader reader = new StringReader(simpleProg);


          List<Token> results = new ArrayList<Token>();

          CommentlessOCamlLexer lexer = new CommentlessOCamlLexer();
          lexer.parse( reader, results );

          for( Token t : results )
            System.out.println(t.toString());

      

    }

}
