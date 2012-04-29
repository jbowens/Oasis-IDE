package camel.gui.code_area;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import camel.gui.controller.FileHandler;
import camel.gui.menus.MenuBar;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;

import java.awt.GridBagLayout;
import java.io.File;

public class CodeArea extends JPanel {

	/* The tabbed pane that holds all the existing tabs */
	protected JTabbedPane tabs;

	/* The current style set being used */
	protected StyleSet style;
	
	public CodeArea() {
		super(new GridBagLayout());
		tabs = new JTabbedPane();
		GridBagConstraints fullFill = new GridBagConstraints();
		fullFill.weighty = fullFill.weightx = 1.0; 
		fullFill.fill = GridBagConstraints.BOTH; 
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabs,fullFill);
		setSize(600,600);

		this.style = new SimpleStyleSet();
	}

	/**
	 * Make a new tab not yet associated with any file.
	 *
	 * @param fh - the associated FileHandler
	 */
	public void makeTab(FileHandler fh) {
		Tab t = new Tab(fh, style);
		tabs.addTab("Untitled", t);
	}

	/**
	 * Make a tab from an already given filename.
	 *
	 * @param fh - the associated FileHandler
	 * @param filename - the file path of the file to open
	 */
	public void makeTab(FileHandler fh, String filename) {
		Tab t = new Tab(new File(filename), fh, style);
		tabs.addTab(fh.getName(), t);
	}

	/**
	 * Makes a tab from an already given File object.
	 *
	 * @param fh - the associated FileHandler
	 * @param file - the file to create the tab from
	 */
	public void makeTab(FileHandler fh, File f) {
		Tab t = new Tab(f, fh, style);
		tabs.addTab(fh.getName(), t);
	}

	/**
	 * Makes a new tab from a user-inputted file.
	 * 
	 * Note: Prompts for user input of the file.
	 *
	 * @param fh - the associated FileHandler
	 */
	public void makeTabFromFile(FileHandler fh) {
		Tab t = new Tab(fh.getFile(), fh, style);
		tabs.addTab(fh.getName(), t);
	}

	/**
	 * Gets the tab that currently has focus.
	 *
	 * @return the current tab
	 */
	public Tab getCurTab() {
		return (Tab) tabs.getSelectedComponent();
	}
	
	public static void main(String[] args) {
		//FileHandler fh = new FileHandler();
		CodeArea ca = new CodeArea();
		JFrame frame = new JFrame();
		frame.add(ca);
		JPanel panel = new JPanel();
		JTextArea ta = new JTextArea();
		ta.append("testing");
		JScrollPane sc = new JScrollPane(ta);
		panel.setLayout(new BorderLayout());
		panel.add(sc);
		ca.tabs.addTab("test", panel);
		frame.pack();
		frame.setVisible(true);
	}


}
