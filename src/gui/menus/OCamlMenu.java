package camel.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import camel.gui.controller.FileHandler;

/**
 * The OCaml menu.
 */
public class OCamlMenu extends JMenu  {

	/* The menu bar this menu is a part of */
	protected MenuBar parentBar;

	protected JMenuItem _run;
	protected JMenuItem _debug;

	/**
	 * Create a new menu 
	 *
	 * @param parentBar the parent menubar of this menu
	 */
	public OCamlMenu(MenuBar parentBar) {
		
		super("OCaml");

		this.parentBar = parentBar;

		_run = new JMenuItem("Run", KeyEvent.VK_R);
		_run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		_debug = new JMenuItem("Debug", KeyEvent.VK_D);
		_debug.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));

		add(_run);
		add(_debug);

	}

}
