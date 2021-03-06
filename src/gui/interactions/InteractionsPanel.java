package camel.gui.interactions;

import java.util.Stack;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import camel.interactions.*;
import camel.debug.*;
import camel.syntaxhighlighter.StyleSet;
import javax.swing.text.BadLocationException;

public class InteractionsPanel extends JPanel implements TextOutputListener {

	protected JEditorPane textPane;
	protected JTextField inputBar;
	protected InteractionsManager _im;
	protected DebugManager _dm;
	protected int _handle;
	protected int _debugHandle;
	protected String query;
	protected Stack<String> commands;
	protected StyleSet style;
	protected boolean isDebug = false;

	public InteractionsPanel(InteractionsManager im, int handle, Font font, StyleSet style, DebugManager dm) {

		setLayout(new BorderLayout());

		this._handle = handle;
		this.style = style;
		this._im = im;
		commands = new Stack<String>();

		textPane = new JEditorPane();
		textPane.setEditable(false);
		textPane.setFont(font);
		textPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		style.apply(textPane);

		JScrollPane sc = new JScrollPane(textPane);
		sc.setBorder(BorderFactory.createEmptyBorder());
		sc.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		add(sc,BorderLayout.CENTER);

		inputBar = new JTextField();
		inputBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		inputBar.setFont(font);
		inputBar.addKeyListener(new EnterListener());
		add(inputBar,BorderLayout.SOUTH);
		textPane.addKeyListener(new KListener());
		try {
			_im.registerOutputListener(this, _handle);
		} catch(Exception e) {}

	}

	@Override
	public void paint(Graphics g) {
		style.apply(textPane);
		super.paint(g);
	}

	/**
	 * Required by the TextOutputListener interface. Receives
	 * output from the interactions backend.
	 */
	public void receiveOutput(TextOutputEvent evt)
	{
		try {
			textPane.getDocument().insertString(textPane.getDocument().getLength(), evt.getText(), null);
		} catch (BadLocationException ex ) {

		}
		//textPane.setText(textPane.getText() + evt.getText());
        textPane.setCaretPosition(textPane.getText().length());	
	}

	/**
	 * Resets the interactions panel with the given filename as the
	 * definitions file.
	 *
	 * @param defs the OCaml definitions file to load
	 */
	public void reset(String defs) {
		/* Close the existing interactions instance */
		try {
			_im.closeInteractionsInstance( _handle );
			_im.removeOutputListener( this, _handle );
		} catch( InvalidInteractionsIdException ex ) {}

		// Clear the text accumulated from the last instance
		textPane.setText("");

		// Start the new interactions instance
		try {
			_handle = _im.newInteractionsInstance(defs);
			_im.registerOutputListener(this, _handle);
		} catch (InvalidInteractionsIdException ex) {
		} catch (FileNotFoundException ex) {
			// TODO: Alert user
		} catch (InteractionsUnavailableException ex) {
			// TODO: Alert user
		}

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

	public class EnterListener implements KeyListener
	{
		public EnterListener()
		{

		}
		public void keyTyped(KeyEvent e) {
			try {
				//System.out.println("Sending " + e.getKeyChar());
				if(e.getKeyChar() == '\n')
        		{
				if (isDebug) {
					_dm.processGUIInput(_debugHandle, inputBar.getText() + "\n");
				}
				else {
					_im.processUserInput(_handle, inputBar.getText() + "\n");
				}
				commands.push(inputBar.getText());
				inputBar.setText("");
        		}
        	} catch(Exception e2) {}
   		}

	    /** Handle the key-pressed event from the text field. */
	    public void keyPressed(KeyEvent e) {
	    	if( (e.getKeyCode() == KeyEvent.VK_KP_UP || e.getKeyCode() == KeyEvent.VK_UP) && !commands.empty() ) {
        			inputBar.setText(commands.pop());
        	}
	    }

	    /** Handle the key-released event from the text field. */
	    public void keyReleased(KeyEvent e) {
	    }
	}

	public void setDebug(DebugManager dm, int dbHandle) {
		this._debugHandle = dbHandle;
		this._dm = dm;
		this.isDebug = true;
	}

	
}
