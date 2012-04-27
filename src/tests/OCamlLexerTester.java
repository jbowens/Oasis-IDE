package camel.tests;

import java.util.ArrayList;
import camel.syntaxhighlighter.*;
import java.io.*;

public class OCamlLexerTester {
	
	public static void main(String args[]) {
		
		if( args.length != 1 ) {
			System.err.println("Usage: java OCamlLexerTester <input.ml>");
			System.exit(1);
		}

		OCamlLexer lexer = new OCamlLexer();

		ArrayList<Token> tokens = new ArrayList<Token>();

		try {
			
			FileReader reader = new FileReader( args[0] );

			lexer.tokenize(reader, tokens);

			for( Token t : tokens )
				System.out.println(t.getType() + " - " + t.getStart() + "..." + (t.getStart()+t.getLength()) );


		} catch( IOException e ) {
			System.err.println("IO Exception");
			e.printStackTrace();
		}

	}

}