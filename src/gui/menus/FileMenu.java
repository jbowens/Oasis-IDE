package camel.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
import camel.gui.code_area.CloseDeniedException;
/**
 * The 'File' menu in the menu bar.
 */
public class FileMenu extends JMenu implements ActionListener {

	protected JMenuItem _new;
	protected JMenuItem _open;
	protected JMenuItem _save;
	protected JMenuItem _saveAs;
	protected JMenuItem _saveAll;
	protected JMenuItem _closeFile;
	protected JMenuItem _newWindow;
	protected JMenuItem _closeWindow;
	protected FileHandler _fh;
	protected CodeArea _codeArea;

	public FileMenu(CodeArea codeArea, FileHandler fh)
	{

		super("File");

		_codeArea = codeArea;
		this._fh = fh;

		setMnemonic('F');

		_new = new JMenuItem("New File", KeyEvent.VK_N);
		_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		_open = new JMenuItem("Open File", KeyEvent.VK_O);
		_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		_save = new JMenuItem("Save", KeyEvent.VK_S);
		_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		_saveAs = new JMenuItem("Save As...");
		_saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | ActionEvent.SHIFT_MASK));
		_saveAll = new JMenuItem("Save All");
		_closeFile = new JMenuItem("Close File");
		_closeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		_newWindow = new JMenuItem("New Window");
		_closeWindow = new JMenuItem("Close Window");
		
		add(_new);
		add(_open);
		add(_save);
		add(_saveAs);
		add(_saveAll);
		add(_closeFile);
		add(new JSeparator());
		add(_newWindow);
		add(_closeWindow);
		
		_open.addActionListener(this);
		_save.addActionListener(this);
		_saveAs.addActionListener(this);
		_saveAll.addActionListener(this);
		_closeFile.addActionListener(this);
		_new.addActionListener(this);
		_newWindow.addActionListener(this);
		_closeWindow.addActionListener(this);

	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == _open) {
			_fh.openFile();
		}

		if(e.getSource() == _save) {
			_fh.saveFile();
		}

		if(e.getSource() == _saveAs) {
			_fh.saveAs();
		}

		if(e.getSource() == _saveAll) {
			_codeArea.saveAll();
		}

		if(e.getSource() == _closeFile) {
			try {
				_codeArea.closeCurrentTab();
			} catch( CloseDeniedException ex ) {
				// They decided not to close after all
			}
		}

		if(e.getSource() == _new) {
			_codeArea.makeTab(_fh);
		}

		if(e.getSource() == _newWindow) {
			_codeArea.getApplication().createNewWindow();
		}

		if(e.getSource() == _closeWindow) {
			try {
				_codeArea.getWindow().close();
			} catch( CloseDeniedException ex ) {
				// Do nothing
			}
		}

	}
	public void setEnable(Boolean b)
	{
		_save.setEnabled(b);
		_saveAs.setEnabled(b);
		_saveAll.setEnabled(b);
		_closeFile.setEnabled(b);
	}

}
