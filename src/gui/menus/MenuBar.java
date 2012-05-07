package camel.gui.menus;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import camel.gui.controller.FileHandler;
import camel.gui.main.MainWindow;
import camel.Application;

public class MenuBar extends JMenuBar {

	FileMenu _fMenu;
	EditMenu _eMenu;
	OCamlMenu _ocamlMenu;
	ViewMenu _vMenu;

	public MenuBar(Application app, MainWindow window)
	{

		_fMenu = new FileMenu(window.getCodeArea(), window.getFileHandler());

		_eMenu = new EditMenu(this, window.getCodeArea());

		_ocamlMenu = new OCamlMenu(this, window.getCodeArea(),app);

		_vMenu = new ViewMenu(this, app.getConfig(), app.getStyleLoader(), window.getCodeArea());

		add(_vMenu, FlowLayout.LEFT);
		add(_ocamlMenu, FlowLayout.LEFT);
		add(_eMenu, FlowLayout.LEFT);
		add(_fMenu, FlowLayout.LEFT);
		
	
	}

}
