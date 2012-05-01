package camel.gui.code_area;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import camel.gui.controller.FileHandler;
import camel.gui.menus.MenuBar;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;
import camel.syntaxhighlighter.StyleWrapper;
import camel.Application;

import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

public class CodeArea extends JPanel {

	/* The tabbed pane that holds all the existing tabs */
	protected JTabbedPane tabs;

	/* The current style set being used */
	protected StyleWrapper style;
	
	/* The application this Code Area is tied to */
	protected Application app;

	public CodeArea(Application app) {
		super(new GridBagLayout());
		tabs = new JTabbedPane();
		GridBagConstraints fullFill = new GridBagConstraints();
		fullFill.weighty = fullFill.weightx = 1.0; 
		fullFill.fill = GridBagConstraints.BOTH; 
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabs,fullFill);
		setSize(600,600);

		this.app = app;

		/* Load the user's preferred default style if it's available */
		StyleSet initialStyle = new SimpleStyleSet();

		String configStyle = app.getConfig().getSetting("styleset");

		for( StyleSet s : app.getStyleLoader().getAvailableStyles() ) {
			if( s.getName() != null && s.getName().equals(configStyle) ) {
				initialStyle = s;
				break;
			}
		}

		this.style = new StyleWrapper( initialStyle );
	}

	/**
	 * Switch the style.
	 */
	public void switchStyle(StyleSet newStyle) {
		style.setStyle(newStyle);
		this.repaint();
		if( newStyle != null && newStyle.getName() != null )
			app.getConfig().setSetting("styleset", newStyle.getName());
	}

	/**
	 * Make a new tab not yet associated with any file.
	 *
	 * @param fh - the associated FileHandler
	 */
	public void makeTab(FileHandler fh) {
		Tab t = new Tab(fh, style);
		tabs.addTab("Untitled", t);
		tabs.setSelectedComponent(t);
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
		tabs.setSelectedComponent(t);
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
		tabs.setSelectedComponent(t);
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
		tabs.setSelectedComponent(t);
	}

	/**
	 * Gets the tab that currently has focus.
	 *
	 * @return the current tab
	 */
	public Tab getCurTab() {
		return (Tab) tabs.getSelectedComponent();
	}

}
