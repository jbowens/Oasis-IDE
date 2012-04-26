package Interactions;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import BackEnd.FileHandler;

public class Tab extends JPanel implements TextOutputListener {

	JEditorPane textPane;
	InteractionsManager _im;
	int _handle;

	public Tab(InteractionsManager im, String filePath) {
		textPane = new JEditorPane();
		JScrollPane sc = new JScrollPane(textPane);
		setLayout(new BorderLayout());
		add(sc);
		this._im = im;
		_handle = _im.newInteractionsInstance(filePath);

	}
	public void receiveOutput(TextOutputEvent evt)
	{
		textPane.setText(textPane.getText() + evt.getText());	
	}
	public String getText()
	{
		return textPane.getText();
	}
	
	
}
