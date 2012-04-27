package camel.gui.interactions;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import camel.interactions.*;
import java.awt.GridBagLayout;

public class InteractionsWindow extends JPanel {

	protected JTabbedPane tabs;
	protected InteractionsManager _im;

	public InteractionsWindow(InteractionsManager im)
	{
		super(new GridBagLayout());
		tabs = new JTabbedPane();
		GridBagConstraints fullFill = new GridBagConstraints();
		fullFill.weighty = fullFill.weightx = 1.0; 
		fullFill.fill = GridBagConstraints.BOTH; 
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabs,fullFill);
		setSize(600,600);
		_im = im;
		makeTab(null);
	}
	public void makeTab(String filePath) {
		Tab t = new Tab(_im,filePath);
		tabs.addTab("int window", t);
	}
	/*
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		InteractionsManager im = new InteractionsManager("ocaml");
		InteractionsWindow iw = new InteractionsWindow(im);
		f.add(iw);
		f.pack();
		f.setVisible(true);
	}
	*/
	
}
