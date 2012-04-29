package camel.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import camel.gui.controller.FileHandler;

//This class essentially represents the "file" menu in the menu bar
public class FileMenu extends JMenu implements ActionListener {
	JMenuItem _new;
	JMenuItem _open;
	JMenuItem _save;
	FileHandler _fh;
	
	public FileMenu(FileHandler fh)
	{
		super("File");
		setMnemonic('F');
		_new = new JMenuItem("New", KeyEvent.VK_N);
		_open = new JMenuItem("Open", KeyEvent.VK_O);
		_save = new JMenuItem("Save", KeyEvent.VK_S);
		add(_new);
		add(_open);
		add(_save);
		_open.addActionListener(this);
		_save.addActionListener(this);
		this._fh = fh;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == _open) {
			_fh.openFile();
		}

		if(e.getSource() == _save) {
			_fh.saveFile();
		}
		
	}

}
