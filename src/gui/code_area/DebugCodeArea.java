package camel.gui.code_area;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import camel.gui.controller.FileHandler;
import camel.gui.controller.DebugFileHandler;
import camel.gui.menus.MenuBar;
import camel.gui.main.MainWindow;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;
import camel.syntaxhighlighter.StyleWrapper;
import camel.Application;

import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DebugCodeArea extends CodeArea {

	DebugTab onlyTab;
	
	public DebugCodeArea(Application app, MainWindow mainwindow) {
		super(app, mainwindow);
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
		DebugTab t = new DebugTab(this, f, fh, super.style);
		this.onlyTab = t;
		super.tabs.addTab(f.getName(), t);
	}

	public DebugTab getDebugTab() {
		return this.onlyTab;
	}	
}
