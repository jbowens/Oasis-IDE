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
import javax.swing.JSplitPane;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JButton;

import camel.Config;
import camel.gui.interactions.InteractionsPanel;
import camel.gui.controller.FileHandler;
import camel.syntaxhighlighter.OCamlLexer;
import camel.syntaxhighlighter.OCamlEditorKit;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;


/**
 * A tab in the Debug GUI. A tab has an associated text pane, and optionally, file that
 * the content of the tab is associated with.  The debug tab is NOT editable.
 */

public class DebugTab extends Tab {

	protected DebugStepper step;

	public DebugTab(CodeArea codeArea, File f, FileHandler fh, StyleSet s) {
		super(codeArea, f, fh, s);
		this.step = new DebugStepper(this);
		textPane.setEditable(false);
		lineNums.setBreakpointSource(step);
		repaint();
	}

	public DebugTab(CodeArea codeArea, FileHandler fh, StyleSet s) {
		super(codeArea, fh, s);
		this.step = new DebugStepper(this);
		textPane.setEditable(false);
		lineNums.setBreakpointSource(step);
		repaint();
	}

}

