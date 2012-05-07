package camel.gui.main;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.awt.Font;
import java.io.File;

import javax.swing.*;

import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
import camel.gui.code_area.CloseDeniedException;
import camel.gui.menus.MenuBar;
import camel.gui.code_area.StatusBar;
import camel.interactions.*;
import camel.debug.*;
import camel.*;

public class MainWindow extends JFrame {

	protected static final String DEFAULT_FONT_LOC = "fonts/DejaVuSansMono.ttf";
	protected static final int DEFAULT_FONT_SIZE = 13;

	/* The application this GUI is tied to */
	protected Application app;

	/* Configuration object passed in from the application */
	protected Config config;

	/* Interactions manager for getting input from the OCaml REPL */
	protected InteractionsManager im;

	/* Debug manager for getting input from ocamldebug backend */
	protected DebugManager dm;

	/* The status bar for the window */
	protected StatusBar statusBar;

	/* The default font to use */
	protected Font font;


	protected CodeArea ca;
	protected MenuBar mb;
	protected FileHandler fh;
	protected FileTree ft;
	protected JSplitPane s1;

	protected boolean closed = false;
	
	/**
	 * Creates a new GUI for the application.
	 *
	 * @param app - the application the gui is tied to
	 * @param config - a config object with the various settings
	 * @param im - an interactions manager to use when creating interactions
	 */
	public MainWindow(Application app, Config config, InteractionsManager im, DebugManager dm)
	{
		super();

		this.app = app;
		this.config = config;
		this.im = im;
		this.dm = dm;

		loadFont();

		/* Instantiate the rest of the GUI */
		statusBar = new StatusBar(this);
		ca = new CodeArea(app, this);
		fh = new FileHandler(ca);
		mb = new MenuBar(app, this);
		ft = new FileTree(fh);
		s1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ft.sp, ca);
		s1.setDividerSize(5);
		add(mb,BorderLayout.NORTH);
		add(s1,BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);

		pack();

		/* Load the desired width and height for the application */
		int width = config.settingExists("initial_width") ? config.getSettingAsInt("initial_width") : 500;
		int height = config.settingExists("initial_height") ? config.getSettingAsInt("initial_height") : 500;

		// Set the width and height
		setBounds( 100, 100, width, height );

		repaint();

		addWindowListener(new ShutdownListener(this));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setVisible(true);

	}

	/**
	 * Opens the given filename.
	 *
	 * @param filename - the filename to open
	 */
	public void open(String filename) {
		ca.makeTab(fh, filename);
	}
	
	/**
	 * Creates a new blank tab.
	 */
	public void newBlank() {
		ca.makeTab(fh);
	}

	/**
	 * Returns this GUI's file handler.
	 *
	 * @return the file handler used by this GUI
	 */
	public FileHandler getFileHandler() {
		return this.fh;
	}

	/**
	 * Gets this window's code area.
	 *
	 * @return the code area of this window
	 */
	public CodeArea getCodeArea() {
		return this.ca;
	}

	/**
	 * Gets this window's application
	 *
	 * @return the application tired to this window
	 */
	public Application getApplication() {
		return app;
	}

	/**
	 * Gets the status bar
	 */ 
	public StatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * Gets the default font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Gets this window's interactions manager.
	 *
	 * @return the window's interactions manager
	 */
	public InteractionsManager getInteractionsManager() {
		return im;
	}

	/**
	 * Get this window's debug manager.
	 *
	 * @return the window's debug manager
	 */
	public DebugManager getDebugManager() {
		return dm;
	}

	/**
	 * Displays a generic error message.
	 *
	 * @param message the error message to display
	 */
	public void displayErrorMessage(String errorMessage) {

		JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);

	}

	/**
	 * A window listener to listen for when the application should shut down.
	 * This is necessary to make sure all system resources are freed when the
	 * program is exited.
	 */
	public class ShutdownListener implements WindowListener {
	
		/* The relevant MainWindow being listenerd to */
		protected MainWindow mainWindow;

		/**
		 * Creates a new shutdown listener.
		 *
		 * @param app - the application to close on window close
		 */
		public ShutdownListener(MainWindow mainWindow) {
			this.mainWindow = mainWindow;
		}

		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {
			// Notify the application to shut down as well
			
			if( ! mainWindow.isClosed() ) {
				mainWindow.getApplication().guiClosed();
				mainWindow.dispose();
			}
		}
		public void windowClosing(WindowEvent e) {
			try {
				mainWindow.getCodeArea().close();
			} catch( CloseDeniedException ex ) {
				return;
			}
			mainWindow.close();
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


	/**
	 * Returns true if this window has been closed.
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * Tells this window to close itself.
	 */
	public void close() {
		processWindowEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSED ) );
		closed = true;
		
	}

}
