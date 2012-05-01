package camel.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
/**
 * The 'File' menu in the menu bar.
 */
public class FileMenu extends JMenu implements ActionListener {

	protected JMenuItem _new;
	protected JMenuItem _open;
	protected JMenuItem _save;
	protected JMenuItem _saveAs;
	protected JMenuItem _saveAll;
	protected FileHandler _fh;
	protected CodeArea _codeArea;

	public FileMenu(CodeArea codeArea, FileHandler fh)
	{

		super("File");

		_codeArea = codeArea;
		this._fh = fh;

		setMnemonic('F');

		_new = new JMenuItem("New File", KeyEvent.VK_N);
		_open = new JMenuItem("Open File", KeyEvent.VK_O);
		_save = new JMenuItem("Save", KeyEvent.VK_S);
		_saveAs = new JMenuItem("Save As...");
		_saveAll = new JMenuItem("Save All");
		
		add(_new);
		add(_open);
		add(_save);
		add(_saveAs);
		add(_saveAll);
		
		_open.addActionListener(this);
		_save.addActionListener(this);
		_new.addActionListener(this);

	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == _open) {
			_fh.openFile();
		}

		if(e.getSource() == _save) {
			_fh.saveFile();
		}

		if(e.getSource() == _new) {
			_codeArea.makeTab(_fh);
		}

	}

}
