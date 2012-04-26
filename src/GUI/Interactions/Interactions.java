package Camel.GUI.Interactions;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Camel.Interactions.*;
import java.awt.GridBagLayout;

public class Interactions extends JPanel{
	protected JTabbedPane tabs;
	InteractionsManager _im;
	public Interactions(InteractionsManager im)
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

	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		InteractionsManager im = new InteractionsManager("ocaml");
		Interactions iw = new Interactions(im);
		f.add(iw);
		f.pack();
		f.setVisible(true);
	}
	
}
