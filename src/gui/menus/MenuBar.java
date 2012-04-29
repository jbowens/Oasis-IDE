package camel.gui.menus;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import camel.gui.controller.FileHandler;
import camel.gui.main.MainWindow;
import camel.Application;

public class MenuBar extends JMenuBar{
	FileMenu _fMenu;
	
	public MenuBar(Application app, MainWindow window)
	{
		_fMenu = new FileMenu(window.getCodeArea(), window.getFileHandler());
		add(_fMenu,FlowLayout.LEFT);
	}

}
