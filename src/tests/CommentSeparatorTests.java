package camel.tests;

import org.junit.*;
import java.util.*;
import org.junit.Assert.*;
import java.io.StringReader;

import camel.syntaxhighlighter.*;

public class CommentSeparatorTests {

    @Test
    public void testSimpleProgram() {
    	
        String simpleProg = "(* this is a comment! *)\nlet rec length(alist : 'a list) = match alist with\n| [] -> 0\n| _::tl -> 1 + length tl;;\n\nlength( [5; 4; 3; 2; 1] );;";

        StringReader reader = new StringReader(simpleProg);

        try {
          CommentSeparator separator = new CommentSeparator();

          ArrayList<Token> results = new ArrayList<Token>();
          separator.separateComments(reader, results);

          assert( results.size() == 3 );
          assert( results.get(1).getText().equals("(* this is a comment! *)") );
          assert( results.get(1).getType() == TokenType.COMMENT );
          assert( results.get(2).getType() == TokenType.UNKNOWN );

          System.out.println("Total length: " + simpleProg.length());

          for( Token t : results ) {
            System.out.println(t + "\n-" + simpleProg.substring(t.getStart(), (t.getStart() + t.getLength())) + "-\n\n");
          }


        } catch( java.io.IOException e ) {
          System.err.println("I/O ERROR");
          assert( false );
        }

    }

    public static void main(String args[]) {
      CommentSeparatorTests tests = new CommentSeparatorTests();
      tests.testSimpleProgram();
    }

}
