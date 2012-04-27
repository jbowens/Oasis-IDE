package Camel.GUI.Interactions;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.*;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import Camel.Interactions.*;

public class Tab extends JPanel implements TextOutputListener {

	JEditorPane textPane;
	InteractionsManager _im;
	int _handle;
	String query;

	public Tab(InteractionsManager im, String filePath) {
		textPane = new JEditorPane();
		JScrollPane sc = new JScrollPane(textPane);
		setLayout(new BorderLayout());
		add(sc);
		this._im = im;
		textPane.addKeyListener(new KListener());
		try {
			_handle = _im.newInteractionsInstance(filePath);
			_im.registerOutputListener(this, _handle);
		} catch(Exception e) {}

	}
	public void receiveOutput(TextOutputEvent evt)
	{
		textPane.setText(textPane.getText() + evt.getText());
        textPane.setCaretPosition(textPane.getText().length());	
	}
	public String getText()
	{
		return textPane.getText();
	}
	public class KListener implements KeyListener
	{
		public KListener()
		{
			
		}
		public void keyTyped(KeyEvent e) {
			try {
				System.out.println("Sending " + e.getKeyChar());
				if(e.getKeyChar() == '\b' && counter > 0)
        		{
        			
        		}
        		_im.processUserInput(_handle, e.getKeyChar());
        		

        	} catch(Exception e2) {}
   		}

	    /** Handle the key-pressed event from the text field. */
	    public void keyPressed(KeyEvent e) {
	    }

	    /** Handle the key-released event from the text field. */
	    public void keyReleased(KeyEvent e) {
	    }

	}

	
}
