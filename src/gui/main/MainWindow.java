package camel.gui.main;


import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import camel.gui.interactions.InteractionsWindow;
import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
import camel.gui.menus.MenuBar;
import camel.interactions.*;

public class MainWindow extends JFrame {

	CodeArea ca;
	MenuBar mb;
	FileHandler fh;
	FileTree ft;
	InteractionsManager im;
	InteractionsWindow iw;
	
	public MainWindow()
	{
		super();
		ca = new CodeArea();
		fh = new FileHandler(ca);
		mb = new MenuBar(fh);
		ft = new FileTree(new File("."));
		im = new InteractionsManager("ocaml");
		iw = new InteractionsWindow(im);
		add(mb,BorderLayout.NORTH);
		add(ca,BorderLayout.CENTER);
		add(ft,BorderLayout.WEST);
		add(iw,BorderLayout.SOUTH);
		setVisible(true);
		setSize(600,600);
		pack();
	}
	
	public static void main(String[] args)
	{
		MainWindow m = new MainWindow();
	}
	
	
}
