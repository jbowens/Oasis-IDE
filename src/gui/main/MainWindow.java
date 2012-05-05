package camel.gui.main;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;

import java.io.File;

import javax.swing.*;

import camel.gui.interactions.InteractionsWindow;
import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
import camel.gui.code_area.CloseDeniedException;
import camel.gui.menus.MenuBar;
import camel.interactions.*;
import camel.*;

public class MainWindow extends JFrame {

	/* The application this GUI is tied to */
	protected Application app;

	/* Configuration object passed in from the application */
	protected Config config;

	/* Interactions manager for getting input from the OCaml REPL */
	protected InteractionsManager im;

	protected CodeArea ca;
	protected MenuBar mb;
	protected FileHandler fh;
	protected FileTree ft;
	protected InteractionsWindow iw;
	protected JSplitPane s1;
	protected JSplitPane s2;

	protected boolean closed = false;
	
	/**
	 * Creates a new GUI for the application.
	 *
	 * @param app - the application the gui is tied to
	 * @param config - a config object with the various settings
	 * @param im - an interactions manager to use when creating interactions
	 */
	public MainWindow(Application app, Config config, InteractionsManager im)
	{
		super();

		this.app = app;
		this.config = config;
		this.im = im;

		/* Instantiate the rest of the GUI */
		ca = new CodeArea(app, this);
		fh = new FileHandler(ca);
		mb = new MenuBar(app, this);
		ft = new FileTree(new File("."));
		iw = new InteractionsWindow(im);
		s1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ft, ca);
		s2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, s1, iw);
		s1.setDividerSize(5);
		s2.setDividerSize(5);
		add(mb,BorderLayout.NORTH);
		add(s2,BorderLayout.CENTER);
		//add(ca,BorderLayout.CENTER);
		//add(ft,BorderLayout.WEST);
		//add(iw,BorderLayout.SOUTH);

		pack();

		/* Load the desired width and height for the application */
		int width = config.settingExists("initial_width") ? config.getSettingAsInt("initial_width") : 500;
		int height = config.settingExists("initial_height") ? config.getSettingAsInt("initial_height") : 500;

		// Set the width and height
		setBounds( 100, 100, width, height );

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
	 * Gets this window's application/
	 *
	 * @return the application tired to this window
	 */
	public Application getApplication() {
		return app;
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
