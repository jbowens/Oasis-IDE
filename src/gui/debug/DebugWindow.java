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

public class DebugWindow extends JFrame {
	
	protected static final String DEFAULT_FONT_LOC = "fonts/DejaVuSansMono.ttf";
	protected static final int DEFAULT_FONT_SIZE = 13;

	protected DebugFileHandler dfh; 

	protected DebugCodeArea dca;

	protected DebugStepper step;

	protected Font font;

	protected Application app;

	protected Config config;

	/**
	 * Creates a new debug window for the application
	 *
	 * @param app - the application associated to this debug window instance
	 * @param config - config object with settings
	 * @param filenmae - file opened with this debug window
	 */

	public DebugWindow(Application app, Config config, String filename) {

		super();

		this.app = app;
		this.config = config;

		loadFont();


		this.dca = new DebugCodeArea(this.app, this);

		/*Init. debugfilehandler*/
		this.dfh = new DebugFileHandler(this.dca);
		
		/*Add code area*/
		this.add(dca, BorderLayout.CENTER);

		this.setTitle("Debug Mode");

		//if we've been passed a valid filename
		if (filename != null) {
			this.open(filename);
		}
		
		/* Load the desired width and height for the application */
		int width = config.settingExists("initial_width") ? config.getSettingAsInt("initial_width") : 500;
		int height = config.settingExists("initial_height") ? config.getSettingAsInt("initial_height") : 500;

		// Set the width and height
		setBounds( 100, 100, width, height );


		//this.step = new DebugStepper(this);
		addWindowListener(new ShutdownListener(this));
		//setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setVisible(true);

	}

	/*Open a tab with the given filename*/
	public void open(String filename) {
		dca.makeDebugTab(dfh, filename);
	}

	public DebugCodeArea getDebugCodeArea() {
		return this.dca;
	}

	/**
	 * A window listener to listen for when the application should shut down.
	 * This is necessary to make sure all system resources are freed when the
	 * program is exited.
	 */
	public class ShutdownListener implements WindowListener {
	
		/* The relevant MainWindow being listenerd to */
		protected DebugWindow debugWindow;

		/**
		 * Creates a new shutdown listener.
		 *
		 * @param app - the application to close on window close
		 */
		public ShutdownListener(DebugWindow debugwindow) {
			this.debugWindow = debugWindow;
		}

		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {
			if (debugWindow != null) {
				System.out.println("Closing..");
				debugWindow.close();
			}
		}
		public void windowClosing(WindowEvent e) {
			if (debugWindow != null) {
				System.out.println("Closing..");
				debugWindow.close();
			}
		}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
	}

	/**
	 * Loads the default font from the file system.
	 */
	protected void loadFont() {

		try {
			Font loadFont = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream( new FileInputStream( DEFAULT_FONT_LOC ) ));
			loadFont = loadFont.deriveFont( (float) DEFAULT_FONT_SIZE );
			font = loadFont;
		} catch( Exception ex ) {
			System.err.println("Unable to load font "  + DEFAULT_FONT_LOC + " " + ex.getClass() );
			font = new Font(Font.MONOSPACED, Font.PLAIN, DEFAULT_FONT_SIZE);
		} 

	}

	public void close() {
		this.app.closeDebug();
	}

}
