package camel.gui.debug;

import camel.gui.code_area.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

public class DebugStepper {

	protected DebugWindow dw;
	protected JToolBar jtb;


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



	public DebugStepper(DebugWindow dw) {

		this.dw = dw;
		
		this.jtb = new JToolBar();
	
		this._next = new StepNext(dw.getDebugCodeArea().getDebugTab());
		
		this.jtb.add(_next);	

		this.dw.add(this.jtb, BorderLayout.SOUTH);


	}


}
