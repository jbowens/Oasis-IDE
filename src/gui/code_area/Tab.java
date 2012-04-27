package camel.gui.code_area;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import camel.gui.controller.FileHandler;

public class Tab extends JPanel{

	JEditorPane textPane;
	File f;
	public Tab(File f, FileHandler fh) {
		textPane = new JEditorPane();
		String input = fh.nextLine();
		String output = "";
		while(input != null)
		{
			output += input + "\n";
			input = fh.nextLine();
		}
		textPane.setText(output);
		JScrollPane sc = new JScrollPane(textPane);
		setLayout(new BorderLayout());
		add(sc);
		this.f = f;
	}
	
	public String getPath()
	{
		return f.getAbsolutePath();
	}
	public String getText()
	{
		return textPane.getText();
	}
	
	
}
