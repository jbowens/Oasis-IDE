package camel.gui.debug;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.awt.Font;
import java.io.File;

import javax.swing.*;

import camel.gui.controller.FileHandler;
import camel.gui.code_area.*;
import camel.gui.code_area.DebugCodeArea;
import camel.gui.code_area.CloseDeniedException;
import camel.gui.menus.MenuBar;
import camel.gui.code_area.StatusBar;
import camel.gui.controller.DebugFileHandler;
import camel.interactions.*;
import camel.gui.main.*;
import camel.*;

public class DebugWindow extends MainWindow {
	
	protected DebugFileHandler dfh; 
	protected DebugCodeArea dca;
	protected DebugStepper step;

	public DebugWindow(Application app, Config config, InteractionsManager im, String filename) {
		super(app, config, im);
		this.setTitle("Debug Mode");
		this.remove(super.s1);
		this.remove(super.statusBar);

		this.dca = new DebugCodeArea(super.app, this);
		this.dfh = new DebugFileHandler(this.dca);
		
		this.add(dca, BorderLayout.NORTH);


		//if we've been passed a valid filename
		if (filename != null) {
			this.open(filename);
		}

		this.step = new DebugStepper(this);
		this.setVisible(true);
		this.pack();
		
	}

	@Override
	public void open(String filename) {
		dca.makeDebugTab(dfh, filename);
	}

	public DebugCodeArea getDebugCodeArea() {
		return this.dca;
	}

}
