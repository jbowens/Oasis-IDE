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

	/**
	 * Creates a new tab and loads the given file.
	 *
	 * @param f - the file to load
	 * @param fh - the file handler to handle associated i/o operations
	 */
	public Tab(File f, FileHandler fh) {
		this.f = f;
		this.fh = fh;
		initialize();

		// Load the given file into the tab
		loadFile(f);
	}

	/**
	 * Create a new blank fh not associated with any file.
	 *
	 * @param fh - a filehandler to handle i/o operations for the tab
	 */
	public Tab(FileHandler fh) {
		this.fh = fh;
		initialize();
	}

	/**
	 * Initializes the tab.
	 */
	protected void initialize() {
		/* Create the editor pane */
		JEditorPane textPane = new JEditorPane();

		// Create an OCaml lexer for the syntax highlighter
		OCamlLexer lexer = new OCamlLexer();

		textPane.setFont( new Font("Courier New", Font.PLAIN, 14) );

		JScrollPane sc = new JScrollPane(textPane);
		setLayout(new BorderLayout());
		add(sc);

		/* Load the syntax highlighter editor kit */
		textPane.setEditorKit( new OCamlEditorKit( lexer ) );

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
