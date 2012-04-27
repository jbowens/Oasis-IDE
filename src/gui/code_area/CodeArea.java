package camel.gui.code_area;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import camel.gui.controller.FileHandler;
import camel.gui.menus.MenuBar;

import java.awt.GridBagLayout;
import java.io.File;

public class CodeArea extends JPanel{

	protected JTabbedPane tabs;
	
	
	public CodeArea()
	{
		super(new GridBagLayout());
		tabs = new JTabbedPane();
		GridBagConstraints fullFill = new GridBagConstraints();
		fullFill.weighty = fullFill.weightx = 1.0; 
		fullFill.fill = GridBagConstraints.BOTH; 
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabs,fullFill);
		setSize(600,600);
	}
	public void makeTab(FileHandler fh) {
		Tab t = new Tab(fh.getFile(), fh);
		tabs.addTab(fh.getName(), t);
	}
	public Tab getCurTab()
	{
		return (Tab) tabs.getSelectedComponent();
	}
	
	
	
	
	public static void main(String[] args)
	{
		//FileHandler fh = new FileHandler();
		CodeArea ca = new CodeArea();
		JFrame frame = new JFrame();
		frame.add(ca);
		JPanel panel = new JPanel();
		JTextArea ta = new JTextArea();
		ta.append("testing");
		JScrollPane sc = new JScrollPane(ta);
		panel.setLayout(new BorderLayout());
		panel.add(sc);
		ca.tabs.addTab("test", panel);
		frame.pack();
		frame.setVisible(true);
	}


}
