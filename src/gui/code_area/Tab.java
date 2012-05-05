package camel.gui.code_area;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.text.Document;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import camel.Config;
import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
import camel.syntaxhighlighter.OCamlLexer;
import camel.syntaxhighlighter.OCamlEditorKit;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;

/**
 * A tab in the GUI. A tab has an associated text pane, and optionally, file that
 * the content of the tab is associated with.
 */
public class Tab extends JPanel implements DocumentListener {

	/* The text pane to be displayed in this tab */
	protected JEditorPane textPane;

	/* The file handler to handle this Tab's file actions */
	protected FileHandler fh;

	/* The file associated with this tab */
	protected File f;

	/* The style used in this tab */
	protected StyleSet style;

	/* The line numbers */
	protected LineNumbersRuler lineNums;

	/* The code area the tab belongs to */
	protected CodeArea codeArea;

	/* The scroll pane */
	protected JScrollPane sc;

	/* Whether or not changes have been made since the last save */
	protected boolean changes = false;

	/**
	 * Creates a new tab and loads the given file.
	 *
	 * @param f - the file to load
	 * @param fh - the file handler to handle associated i/o operations
	 */
	public Tab(CodeArea codeArea, File f, FileHandler fh, StyleSet s) {
		this.codeArea = codeArea;
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
	public Tab(CodeArea codeArea, FileHandler fh, StyleSet s) {
		this.codeArea = codeArea;
		this.fh = fh;
		this.style = s;
		initialize();
	}

	/**
	 * Paints the tab
	 */
	public void paint(Graphics g) {
		style.apply( textPane );
		super.paint(g);
	}

	/**
	 * Initializes the tab.
	 */
	protected void initialize() {

		setLayout(new BorderLayout());

		/* Create the editor pane */
		textPane = new JEditorPane();
		textPane.setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) );

		// Create an OCaml lexer for the syntax highlighter
		OCamlLexer lexer = new OCamlLexer();

		textPane.setFont( codeArea.getFont() );
		style.apply( textPane );

		/* Load the syntax highlighter editor kit */
		textPane.setEditorKit( new OCamlEditorKit( lexer, style ) );

		sc = new JScrollPane(textPane);
		sc.setBorder(BorderFactory.createEmptyBorder());

		/* This must happen AFTER setting the editor kit to the OCaml Editor kit */
		lineNums = new LineNumbersRuler(style);
		lineNums.install(textPane);

		sc.setRowHeaderView(lineNums);

		sc.getViewport().setView(textPane);

		if( ! lineNumbersEnabled() )
			hideLineNumbers();

		add(sc, BorderLayout.CENTER);

		/* Begin listening to the document changes */
		textPane.getDocument().addDocumentListener( this );

	}

	/**
	 * Responds to a change in display preferences.
	 */
	public void updateDisplayPreferences() {
		if( lineNumbersEnabled() )
			showLineNumbers();
		else
			hideLineNumbers();
	}

	/**
	 * Determines if line numbers are enabled.
	 */
	public boolean lineNumbersEnabled() {
		Config c = codeArea.getApplication().getConfig();

		if( ! c.settingExists("linenumbers") )
			return true;
		else
			return c.getSetting("linenumbers").equalsIgnoreCase("true");

	}

	/**
	 * Shows the line numbers
	 */
	public void showLineNumbers() {
		sc.getRowHeader().add(lineNums);
	}

	/**
	 * Hides the line numbers
	 */
	public void hideLineNumbers() {
		sc.getRowHeader().remove(lineNums);
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

		changes = false;
	}
	
	/**
	 * Gets the text pane of this tab.
	 */
	public JEditorPane getTextPane() {
		return textPane;
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
	 * Gets the file associated with the tab's
	 *
	 * @return the file of this tab
	 */
	public File getFile() {
		return f;
	}

	/** 
	 * Return the currently stored in the file.
	 *
	 * @return the textpane's text
	 */
	public String getText() {
		return textPane.getText();
	}
	
	/**
	 * Set the file location associated with this tab/file.
	 *
	 * @param file the new file location
	 */
	public void setFileLocation(File file) {
		f = file;
		codeArea.setTabTitle(this, file.getName());
		changes = false;
	}

	/**
	 * Determines whether or not changes have been made since this tab was last saved.
	 * 
	 * @return true if there are unsaved changes, false otherwise
	 */
	public boolean unsavedChanges() {
		return changes;
	}

	/**
	 * Called whenever this tab is going to be closed. The tab should try to
	 * get its affairs in order before it dies.
	 */
	public void close() {
		// TODO: Implement
	}

	/**
	 * Called whenever the document contained changes.
	 */
	protected void documentChanged() {
		changes = true;
	}

	/* DocumentListener interface methods */
	public void changedUpdate(DocumentEvent evt) {
		documentChanged();
	}
	public void insertUpdate(DocumentEvent evt) {
		documentChanged();
	}
	public void removeUpdate(DocumentEvent evt) {
		documentChanged();
	}

}
