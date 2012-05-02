package camel.gui.menus;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import camel.gui.code_area.Tab;
import camel.gui.code_area.CodeArea;
import camel.gui.controller.FileHandler;

/**
 * The edit menu.
 */
public class EditMenu extends JMenu implements ActionListener {

	/* The menu bar this menu is a part of */
	protected MenuBar parentBar;

	/* The code area */
	protected CodeArea _codeArea;

	protected JMenuItem _copy;
	protected JMenuItem _cut;
	protected JMenuItem _paste;

	protected JMenuItem _undo;

	/**
	 * Create a new menu bar
	 *
	 * @param parentBar the parent menubar of this menu
	 */
	public EditMenu(MenuBar parentBar, CodeArea codeArea) {
		
		super("Edit");

		this.parentBar = parentBar;
		this._codeArea = codeArea;

		setMnemonic('E');

		_copy = new JMenuItem("Copy", KeyEvent.VK_C);
		_cut = new JMenuItem("Cut", KeyEvent.VK_X);
		_paste = new JMenuItem("Paste", KeyEvent.VK_V);
		_undo = new JMenuItem("Undo", KeyEvent.VK_Z);

		add(_copy);
		add(_cut);
		add(_paste);
		add(_undo);

		_copy.addActionListener( this );
		_cut.addActionListener( this );
		_paste.addActionListener( this );

	}

	public void actionPerformed(ActionEvent evt) {

		if( evt.getSource() == _copy ) {
			String text = _codeArea.getCurTab().getTextPane().getSelectedText();
			StringSelection ss = new StringSelection(text);
			getToolkit().getSystemClipboard().setContents(ss, null);
		}

		if( evt.getSource() == _cut ) {
			String text = _codeArea.getCurTab().getTextPane().getSelectedText();
			_codeArea.getCurTab().getTextPane().replaceSelection("");
			StringSelection ss = new StringSelection(text);
			getToolkit().getSystemClipboard().setContents(ss, null);
		}

		if( evt.getSource() == _paste ) {
			Transferable t = getToolkit().getSystemClipboard().getContents(null);
			String text = "";
    	try {
        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            text = (String)t.getTransferData(DataFlavor.stringFlavor);
        }
    	} catch (UnsupportedFlavorException e) {
			} catch (IOException e) { }
    	
			_codeArea.getCurTab().getTextPane().replaceSelection(text);

		}

	}

}
