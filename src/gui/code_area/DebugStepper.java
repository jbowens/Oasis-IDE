package camel.gui.code_area;


import java.util.Hashtable;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import camel.syntaxhighlighter.OCamlDocument;

public class DebugStepper implements MouseListener, CaretListener, BreakpointSource {

	protected DebugTab tab;
	protected JToolBar jtb;
	protected int lastClick;

	protected Hashtable<Integer,Boolean> breakpoints;


	protected class StepNext extends JButton implements ActionListener {

		DebugTab tab;
		String text;
		JEditorPane tp;
		LinePainter lp;
		//TODO move this to a more "global" location
		int curr_line = 0;

		public StepNext(DebugTab tab) {
			super("Step forward");
			this.tab = tab;
			this.text = tab.getText();
			this.tp = tab.getTextPane();
			this.lp = new LinePainter(this.tp);	
			super.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("Forward!!!!!");
			curr_line++;
			setLineNumber(tp, curr_line);
		}	

	
		private void setLineNumber(JEditorPane text, int line) {
			int currentLine = 0;
		    int currentSelection = 0;
		    String textContent = text.getText();
			String seperator = "\n";
		    int seperatorLength = seperator.length();
		    while (currentLine < line) {
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
		}


	}

	private StepNext _next;	



	public DebugStepper(DebugTab dtb) {

		this.tab = dtb;
		
		this.jtb = new JToolBar();
		this.jtb.setFloatable(false);
		this.breakpoints = new Hashtable<Integer,Boolean>();
	
		this._next = new StepNext(this.tab);
		
		this.jtb.add(_next);	

		this.tab.add(this.jtb, BorderLayout.NORTH);

		this.tab.getTextPane().addMouseListener(this);
		this.tab.getTextPane().addCaretListener(this);



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
			}
			else {
				//Add new breakpoint
				breakpoints.put(linePos,true);
			}

			this.lastClick = -1;
		}
		else {
			this.lastClick = linePos;
		}

		/*System.out.print("Breakpoints:");
		Integer[] keys = (Integer[]) this.breakpoints.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			if (this.breakpoints.get(keys[i])) {
				System.out.print(keys[i] + ",");
			}
		}*/

	}
	
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

}
