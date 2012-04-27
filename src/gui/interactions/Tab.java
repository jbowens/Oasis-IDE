package camel.gui.interactions;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.*;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import camel.interactions.*;

public class Tab extends JPanel implements TextOutputListener {

	protected JEditorPane textPane;
	protected JTextField inputBar;
	protected InteractionsManager _im;
	protected int _handle;
	protected String query;

	public Tab(InteractionsManager im, String filePath) {
		textPane = new JEditorPane();
		textPane.setEditable(false);
		JScrollPane sc = new JScrollPane(textPane);
		setLayout(new BorderLayout());
		add(sc,BorderLayout.NORTH);
		this._im = im;
		inputBar = new JTextField();
		inputBar.addKeyListener(new enterListener());
		add(inputBar,BorderLayout.SOUTH);
		textPane.addKeyListener(new KListener());
		try {
			_handle = _im.newInteractionsInstance(filePath);
			_im.registerOutputListener(this, _handle);
		} catch(Exception e) {}

	}

	/**
	 * Required by the TextOutputListener interface. Receives
	 * output from the interactions backend.
	 */
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
	public class enterListener implements KeyListener
	{
		public enterListener()
		{

		}
		public void keyTyped(KeyEvent e) {
			try {
				//System.out.println("Sending " + e.getKeyChar());
				if(e.getKeyChar() == '\n')
        		{
        			_im.processUserInput(_handle, inputBar.getText());
					_im.processUserInput(_handle,'\n');
					inputBar.setText("");
        		}
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
