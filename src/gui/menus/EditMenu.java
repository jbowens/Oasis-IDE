package camel.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import camel.gui.controller.FileHandler;

/**
 * The edit menu.
 */
public class EditMenu extends JMenu  {

	/* The menu bar this menu is a part of */
	protected MenuBar parentBar;

	protected JMenuItem _copy;
	protected JMenuItem _cut;
	protected JMenuItem _paste;

	protected JMenuItem _undo;

	/**
	 * Create a new menu bar
	 *
	 * @param parentBar the parent menubar of this menu
	 */
	public EditMenu(MenuBar parentBar) {
		
		super("Edit");

		this.parentBar = parentBar;

		setMnemonic('E');

		_copy = new JMenuItem("Copy", KeyEvent.VK_C);
		_cut = new JMenuItem("Cut", KeyEvent.VK_X);
		_paste = new JMenuItem("Paste", KeyEvent.VK_V);
		_undo = new JMenuItem("Undo", KeyEvent.VK_Z);

		add(_copy);
		add(_cut);
		add(_paste);
		add(_undo);

	}

}
