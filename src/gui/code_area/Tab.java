package camel.gui.code_area;

import java.awt.Font;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import camel.gui.controller.FileHandler;
import camel.syntaxhighlighter.OCamlLexer;
import camel.syntaxhighlighter.OCamlEditorKit;

public class Tab extends JPanel{

	JEditorPane textPane;
	File f;
	public Tab(File f, FileHandler fh) {

		// Create an OCaml lexer for the syntax highlighter
		OCamlLexer lexer = new OCamlLexer();

		/* Create the editor pane */
		JEditorPane textPane = new JEditorPane();

		textPane.setFont( new Font("Courier New", Font.PLAIN, 14) );

		/* Load the syntax highlighter editor kit */
		textPane.setEditorKit( new OCamlEditorKit( lexer ) );

		String input = fh.nextLine();
		String output = "";
		while(input != null)
		{
			output += input + "\n";
			input = fh.nextLine();
		}
		textPane.setText(output);
		JScrollPane sc = new JScrollPane(textPane);
		setLayout(new BorderLayout());
		add(sc);
		this.f = f;
	}
	
	public String getPath()
	{
		return f.getAbsolutePath();
	}
	public String getText()
	{
		return textPane.getText();
	}
	
	
}
