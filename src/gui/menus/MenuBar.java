package camel.gui.menus;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import camel.gui.controller.FileHandler;

public class MenuBar extends JMenuBar{
	FileMenu _fMenu;
	
	public MenuBar(FileHandler fh)
	{
		_fMenu = new FileMenu(fh);
		add(_fMenu,FlowLayout.LEFT);
	}
	/*
	public static void main(String[] args)
	{
		FileHandler fh = new FileHandler();
		MenuBar mBar = new MenuBar(fh);
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		frame.add(panel);
		panel.add(mBar);
		frame.pack();
		frame.setVisible(true);
	}
	*/

}
