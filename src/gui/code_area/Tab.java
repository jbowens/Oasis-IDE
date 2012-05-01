package camel.gui.code_area;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import camel.gui.controller.FileHandler;
import camel.syntaxhighlighter.OCamlLexer;
import camel.syntaxhighlighter.OCamlEditorKit;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;

/**
 * A tab in the GUI. A tab has an associated text pane, and optionally, file that
 * the content of the tab is associated with.
 */
public class Tab extends JPanel{

	/* The text pane to be displayed in this tab */
	protected JEditorPane textPane;

	/* The file handler to handle this Tab's file actions */
	protected FileHandler fh;

	/* The file associated with this tab */
	protected File f;

	/* The style used in this tab */
	protected StyleSet style;

	/**
	 * Creates a new tab and loads the given file.
	 *
	 * @param f - the file to load
	 * @param fh - the file handler to handle associated i/o operations
	 */
	public Tab(File f, FileHandler fh, StyleSet s) {
		this.f = f;
		this.fh = fh;
		this.style = s;
		initialize();

		// Load the given file into the tab
		loadFile(f);
	}

	/**
	 * Create a new blank fh not associated with any file.
	 *
	 * @param fh - a filehandler to handle i/o operations for the tab
	 */
	public Tab(FileHandler fh, StyleSet s) {
		this.fh = fh;
		this.style = s;
		initialize();
	}

	public void paint(Graphics g) {
		style.apply( textPane );
		super.paint(g);
	}

	/**
	 * Initializes the tab.
	 */
	protected void initialize() {
		/* Create the editor pane */
		textPane = new JEditorPane();

		// Create an OCaml lexer for the syntax highlighter
		OCamlLexer lexer = new OCamlLexer();

		textPane.setFont( new Font(Font.MONOSPACED, Font.PLAIN, 15) );
		style.apply( textPane );

		/* Load the syntax highlighter editor kit */
		textPane.setEditorKit( new OCamlEditorKit( lexer, style ) );

		JScrollPane sc = new JScrollPane(textPane);

		setLayout(new BorderLayout());
		add(sc);

	}

	/**
	 * Load the given file into the tab.
	 *
	 * @param f - the file to load into the tab
	 */
	public void loadFile(File f) {

		String input = fh.nextLine();
		String output = "";
		while(input != null)
		{
			output += input + "\n";
			input = fh.nextLine();
		}
		textPane.setText(output);

	}
	
	/**
	 * Gets the path associated with the file's tab, if any.
	 *
	 * @return the path of this tab's file
	 */
	public String getPath() {
		if( f == null )
			return null;
		return f.getAbsolutePath();
	}

	/** 
	 * Return the currently stored in the file.
	 *
	 * @return the textpane's text
	 */
	public String getText() {
		return textPane.getText();
	}
	
}
