package camel.gui.code_area;


import camel.debug.*;
import camel.syntaxhighlighter.OCamlDocument;
import camel.interactions.*;
import camel.gui.interactions.*;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

import java.util.Hashtable;
import java.io.*;

public class DebugStepper implements MouseListener,CaretListener,TextOutputListener, BreakpointSource {

	protected DebugTab tab;
	protected JToolBar jtb;
	protected DebugManager dm;
	protected int handle;
	protected String mName;
	protected LinePainter lp;
	int curr_line = 0;

	protected File f;
	protected int lastClick;

	protected Hashtable<Integer,Boolean> breakpoints;


	protected class Next extends JButton implements ActionListener {

		//TODO move this to a more "global" location
		protected DebugManager dm;
		protected int handle;

		public Next(DebugStepper step) {
			super("Next");
			this.dm = step.getDM();
			this.handle = step.getHandle();
			super.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				dm.processGUIInput(handle,"next\n");
			} catch(Exception f) {}
		}	
		
	}

	protected class Run extends JButton implements ActionListener {
		//
		//TODO move this to a more "global" location
		protected DebugManager dm;
		protected int handle;

		public Run(DebugStepper step) {
			super("Run");
			this.dm = step.getDM();
			this.handle = step.getHandle();
			super.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				System.out.println("SENT RUN @ HANDLE: " + handle);
				dm.processGUIInput(handle,"run\n");
			} catch(Exception f) {
				f.printStackTrace();
			}
		}	
		
	}

	/**
	*The step back button
	*/
	protected class StepBack extends JButton implements ActionListener {

		//TODO move this to a more "global" location
		protected DebugManager dm;
		protected int handle;

		public StepBack(DebugStepper step) {
			super("Step back");
			this.dm = step.getDM();
			this.handle = step.getHandle();
			super.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				dm.processGUIInput(handle,"backstep\n");
			} catch(Exception f) {
				f.printStackTrace();
			}
		}	
		
	}

	/**
	*The reverse button
	*/
	protected class Reverse extends JButton implements ActionListener {

		//TODO move this to a more "global" location
		protected DebugManager dm;
		protected int handle;

		public Reverse(DebugStepper step) {
			super("Reverse");
			this.dm = step.getDM();
			this.handle = step.getHandle();
			super.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				dm.processGUIInput(handle,"reverse\n");
			} catch(Exception f) {
				f.printStackTrace();
			}
		}	
		
	}

	/**
	*The step button
	*/
	protected class Step extends JButton implements ActionListener {

		//TODO move this to a more "global" location
		protected DebugManager dm;
		protected int handle;

		public Step(DebugStepper step) {
			super("Step");
			this.dm = step.getDM();
			this.handle = step.getHandle();
			super.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				dm.processGUIInput(handle,"step\n");
			} catch(Exception f) {
				f.printStackTrace();
			}
		}	
		
	}


	private Next _next;	
	private Run _run;
	private StepBack _stepBack;
	private Reverse _reverse;
	private Step _step;



	public DebugStepper(DebugTab dtb, DebugManager dm, File f){

		this.tab = dtb;
		this.dm = dm;
		this.f = f;
		this.lp = new LinePainter(this.tab.getTextPane());	
		this.breakpoints = new Hashtable<Integer,Boolean>();

		this.mName = f.getName().split("\\.")[0].toLowerCase();	


		/*Register listener*/
		try {
			this.handle = this.dm.newDebuggerInstance(f.getAbsolutePath());
			System.out.println("HANDLE: " + this.handle);
			this.dm.registerOutputListener(this,this.handle);
			this.dm.registerOutputListener(this.tab.getInteractionsPanel(),this.handle);
		} catch (Exception e) {}	

		/* Add listeners */
		this.tab.getTextPane().addMouseListener(this);
		this.tab.getTextPane().addCaretListener(this);

		this.jtb = new JToolBar();
		this._next = new Next(this);
		this._run = new Run(this);
		this._stepBack = new StepBack(this);
		this._reverse = new Reverse(this);
		this._step = new Step(this);
		this.jtb.add(_step);
		this.jtb.add(_next);	
		this.jtb.add(_run);
		this.jtb.add(_reverse);
		this.jtb.add(_stepBack);

		this.tab.add(this.jtb, BorderLayout.NORTH);

		/*try {
			dm.processGUIInput(handle,"break @ " + mName + " 1\n");
			Thread.sleep(300);
			dm.processGUIInput(handle,"run\n");
			Thread.sleep(300);
			dm.processGUIInput(handle,"next\n");
			Thread.sleep(300);
			dm.processGUIInput(handle,"next\n");
			Thread.sleep(300);
			dm.processGUIInput(handle,"run\n");

			//dm.processGUIInput(handle, "run\n");
		} catch (Exception e) {
		}*/

	}

	public DebugManager getDM() {
		return this.dm;
	}

	public int getHandle() {
		return this.handle;
	}
	
	private void updateLineNumber(int line) {
		System.out.println("Update Line: " + line);
		JEditorPane text = this.tab.getTextPane();
		int currentLine = 0;
		int currentSelection = 0;
		String textContent = text.getText();
		String seperator = "\n";
		int seperatorLength = seperator.length();
		while (currentLine < line-1) {
			int next = textContent.indexOf(seperator,currentSelection);
			if (next > -1) {
			    currentSelection = next + seperatorLength;
			    currentLine++;
			} else {
			    // set to the end of doc
			    currentSelection = textContent.length();
			    currentLine= line; // exits loop
			}
		    }
		text.setCaretPosition(currentSelection);
		/*if (this.tab == null)
			return;
	
		if (! (this.tab.getTextPane().getDocument() instanceof OCamlDocument) )
			return;

		OCamlDocument doc = (OCamlDocument) this.tab.getTextPane().getDocument();
		int linePos = doc.getLinePosition( line );
		System.out.println("STEP TO: " + linePos);*/
	}



	public void close() {
		dm.close(this.handle);
	}

	/**
	*The implementation of the TextOutputListener. Recieves input from the ocaml
	*debug. Calls updateLineNumber to update the debug GUI.
	*
	*@param evt - the TextOutputEvent from which we get the text
	*/
	public void receiveOutput(TextOutputEvent evt){
		String input = evt.getText();
		String[] inputArray = input.split("\\s+");
		//try{
			for(int i = 0; i< inputArray.length; i++){
				if(inputArray[i].equals("module")){
					if(((i + 2) < inputArray.length) && (inputArray[i+1].toLowerCase().equals(mName))) {
						try{
							int lineNum = Integer.parseInt(inputArray[i+2]);
							System.out.println(inputArray[i]);
							System.out.println(inputArray[i+1]);
							System.out.println(inputArray[i+2]);
							updateLineNumber(lineNum);
						}catch(NumberFormatException nmf){
							/*Eat it */
						}
					}
				}
			}
	}

	/**
	 * Determines whether there is a breakpoint on the given line.
	 *
	 * @param line the line to put the breakpoint on.
	 */
	public boolean breakpointOnLine(int line) {
		if( breakpoints.containsKey(line) )
			return breakpoints.get(line);
		else
			return false;
	}


	/**
	 * Required by the CaretListener interface.
	 *
	 * This method updates the status bar line and col positions whenever the
	 * caret moves.
	 *
	 * @param evt the caret event to respond to
	 */
	public void caretUpdate(CaretEvent evt) {
		int pos = evt.getDot();

		if( this.tab == null )
			return;

		if( ! (this.tab.getTextPane().getDocument() instanceof OCamlDocument) )
			return;

		OCamlDocument doc = (OCamlDocument) this.tab.getTextPane().getDocument();
		int linePos = doc.getLinePosition( pos );
		int columnPos = doc.getColumnPosition( pos );
		System.out.println("Line: " + linePos);

	}

	public void mouseClicked(MouseEvent e) {

		int pos = this.tab.getTextPane().getCaretPosition();
		if (this.tab == null)
			return;
	
		if (! (this.tab.getTextPane().getDocument() instanceof OCamlDocument) )
			return;

		OCamlDocument doc = (OCamlDocument) this.tab.getTextPane().getDocument();
		int linePos = doc.getLinePosition( pos );

		if (linePos == this.lastClick) {

			if (breakpoints.containsKey(linePos)) {
				boolean isBreak = this.breakpoints.get(linePos);
				//Toggle breakpoint
				this.breakpoints.put(linePos,!isBreak);
				breakPoint(linePos,!isBreak);
			}
			else {
				//Add new breakpoint
				breakpoints.put(linePos,true);
				breakPoint(linePos, true);
			}

			this.lastClick = -1;
		}
		else {
			this.lastClick = linePos;
		}


	}
	
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}
	

	public void mouseExited(MouseEvent e) {

	}

	public void breakPoint(int linePos, boolean isBreak) {
		if (isBreak) {
			try {
			System.out.println("SET BP @ HANDLE: " + handle);
			dm.processGUIInput(handle,"break @ " + mName + " "+ linePos + "\n");
			} catch (Exception e) {}
		}
		else {
		}
	}

}
