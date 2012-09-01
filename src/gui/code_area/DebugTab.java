package camel.gui.code_area;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;

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
import camel.debug.*;
import camel.gui.interactions.*;

import java.io.IOException;
import camel.interactions.InteractionsUnavailableException;


/**
 * A tab in the Debug GUI. A tab has an associated text pane, and optionally, file that
 * the content of the tab is associated with.  The debug tab is NOT editable.
 */

public class DebugTab extends Tab {

	protected DebugStepper step;

	public DebugTab(CodeArea codeArea, File f, FileHandler fh, StyleSet s) {
		super(codeArea, f, fh, s);
		this.step = new DebugStepper(this, codeArea.getWindow().getDebugManager(), f);
		this.interactionsPanel.setDebug(this.step.getDM(), this.step.getHandle());
		textPane.setEditable(false);
		lineNums.setBreakpointSource(step);
		repaint();
	}

	public DebugTab(CodeArea codeArea, FileHandler fh, StyleSet s) {
		super(codeArea, fh, s);
		this.step = new DebugStepper(this, codeArea.getWindow().getDebugManager(), f);
		this.interactionsPanel.setDebug(this.step.getDM(), this.step.getHandle());
		textPane.setEditable(false);
		lineNums.setBreakpointSource(step);
		repaint();
	}

	protected InteractionsPanel createInteractionsPanel() {
		try {
			int handle = codeArea.getWindow().getInteractionsManager().newInteractionsInstance(null);
			return new InteractionsPanel(codeArea.getWindow().getInteractionsManager(), handle, codeArea.getFont(), style, null);
		}  catch( InteractionsUnavailableException ex ) {
			return null;
		} catch( FileNotFoundException ex ) {
            return null;
        }
	}


	@Override
	public void close() {
		step.close();
		super.close();
	}

}

