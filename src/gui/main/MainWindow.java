package camel.gui.main;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;

import java.io.File;

import javax.swing.JFrame;

import camel.gui.interactions.InteractionsWindow;
import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
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
	
	public MainWindow(Application app, Config config, InteractionsManager im)
	{
		super();

		this.app = app;
		this.config = config;
		this.im = im;

		/* Instantiate the rest of the GUI */
		ca = new CodeArea();
		fh = new FileHandler(ca);
		mb = new MenuBar(fh);
		ft = new FileTree(new File("."));
		iw = new InteractionsWindow(im);
		
		add(mb,BorderLayout.NORTH);
		add(ca,BorderLayout.CENTER);
		add(ft,BorderLayout.WEST);
		add(iw,BorderLayout.SOUTH);
		


		pack();

		/* Load the desired width and height for the application */
		int width = config.settingExists("initial_width") ? config.getSettingAsInt("initial_width") : 500;
		int height = config.settingExists("initial_height") ? config.getSettingAsInt("initial_height") : 500;

		// Set the width and height
		setBounds( 100, 100, width, height );

		setVisible(true);

	}
	
	/**
	 * A window listener to listen for when the application should shut down.
	 * This is necessary to make sure all system resources are freed when the
	 * program is exited.
	 */
	public class ShutdownListener implements WindowListener {
	
		/* The relevant application */
		protected Application app;

		/**
		 * Creates a new shutdown listener.
		 *
		 * @param app - the application to close on window close
		 */
		public ShutdownListener(Application app) {
			this.app = app;
		}

		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {
			// Notify the application to shut down as well
			app.close();
		}
		public void windowClosing(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
	}

}
