package camel.gui.code_area;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import camel.gui.controller.FileHandler;
import camel.gui.controller.DebugFileHandler;
import camel.gui.menus.MenuBar;
import camel.gui.debug.DebugWindow;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;
import camel.syntaxhighlighter.StyleWrapper;
import camel.Application;

import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DebugCodeArea extends JPanel{

	protected DebugTab onlyTab;
	
	/*Holds debugtabs*/
	protected JTabbedPane dtabs;

	protected ArrayList<DebugTab> tabList;

	protected StyleWrapper style;

	protected DebugWindow debugWindow;

	protected Application app;

	protected DebugFileHandler dfh;


	
	public DebugCodeArea(Application app, DebugWindow debugWindow) {

		super(new GridBagLayout());

		this.app = app;
		this.debugWindow = debugWindow;
		this.dfh = new DebugFileHandler(this);

		tabList = new ArrayList<DebugTab>();
		dtabs = new JTabbedPane();
		GridBagConstraints fullFill = new GridBagConstraints();
		fullFill.weighty = fullFill.weightx = 1.0; 
		fullFill.fill = GridBagConstraints.BOTH; 
		dtabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(dtabs,fullFill);
		setSize(600,600);
		
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
	 * Make a debug tab from an already given filename
	 *
	 * @param fh - the associated FileHandler
	 * @param filename - the file path of the file to open
	 */
	public void makeDebugTab(DebugFileHandler fh, String filename) {
		fh.setBuffRead(filename);
		File f = new File(filename);
		//DebugTab t = new DebugTab(this, f, fh, this.style);
		DebugTab t = null;
		this.onlyTab = t;
		this.dtabs.addTab(f.getName(), t);
		dtabs.setSelectedComponent(t);
		this.tabList.add(t);
	}

	public DebugTab getDebugTab() {
		return this.onlyTab;
	}	

	public Application getApplication() {
		return this.app;
	}
}
