package camel.tests;

import camel.syntaxhighlighter.*;

import javax.swing.*;
import java.awt.Font;

public class SyntaxHighlighterTest {
	
	public static void main(String args[]) {
		
		JFrame testFrame = new JFrame();

		OCamlLexer lexer = new OCamlLexer();

		JEditorPane textPane = new JEditorPane();
		textPane.setFont( new Font("Courier New", Font.PLAIN, 14) );
		textPane.setEditorKit( new OCamlEditorKit( lexer, new SimpleStyleSet() ) );

		testFrame.getContentPane().add(textPane);

		testFrame.setBounds(100, 100, 600, 600);

		testFrame.setVisible(true);

		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}