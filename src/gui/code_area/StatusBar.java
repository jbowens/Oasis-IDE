package camel.gui.code_area;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

import camel.syntaxhighlighter.OCamlDocument;
import camel.gui.main.MainWindow;

/**
 * A status bar for displaying line and column number, etc.
 */
public class StatusBar extends JPanel implements CaretListener, ActionListener {
	
	/* How long a status should be visible */
	protected static final int STATUS_DELAY = 4000;

	/* The main window attached to this status bar */
	protected MainWindow mainWindow;

	/* The inner jpanel */
	protected JPanel innerPanel;

	/* JLabel for displaying where the caret is */
	protected JLabel caretPositionLabel;

	/* JLabel for displaying status events */
	protected JLabel statusLabel;

	/* The font to use when displaying text on this status bar */
	protected Font font;

	/* Timer for removing status updates when expired */
	protected Timer timer;

	/**
	 * Creates a new status bar.
	 *
	 * @param parent the parent tab of this status bar
	 */
	public StatusBar(MainWindow mainWindow) {
		this.mainWindow = mainWindow;

		setLayout(new BorderLayout());

		setBackground(new Color(0, 0, 0));

		innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());

		// Set the background
		innerPanel.setBackground(new Color(0, 0, 0, 190));
		innerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		caretPositionLabel = new JLabel("Line 1, Column 1", SwingConstants.LEFT);
		caretPositionLabel.setForeground(Color.WHITE);
		font = caretPositionLabel.getFont();
		font = font.deriveFont(Font.PLAIN, 10);
		caretPositionLabel.setFont(font);

		statusLabel = new JLabel("", SwingConstants.RIGHT);
		statusLabel.setForeground(Color.WHITE);
		statusLabel.setFont(font);

		innerPanel.add(caretPositionLabel, BorderLayout.WEST);
		innerPanel.add(statusLabel, BorderLayout.EAST);

		add(innerPanel, BorderLayout.CENTER);
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

		Tab currentTab = mainWindow.getCodeArea().getCurTab();
		if( currentTab == null )
			return;

		if( ! (currentTab.getTextPane().getDocument() instanceof OCamlDocument) )
			return;

		OCamlDocument doc = (OCamlDocument) currentTab.getTextPane().getDocument();
		int linePos = doc.getLinePosition( pos );
		int columnPos = doc.getColumnPosition( pos );
		caretPositionLabel.setText("Line "+linePos+", Column " + columnPos);
		
		redraw();

	}

	/**
	 * Display the given status on the status bar.
	 *
	 * @param status the status to display on the status bar
	 */
	public void displayStatus(String status) {

		if( timer != null && timer.isRunning() )
			timer.stop();

		statusLabel.setText(status);

		redraw();

		timer = new Timer(STATUS_DELAY, this);
		timer.setRepeats(false);
		timer.start();
	}

	/**
	 * Respond to the timer going off (an expired status)
	 */
	public void actionPerformed(ActionEvent evt) {
		statusLabel.setText("");

		redraw();

		timer = null;
	}

	/**
	 * Redraws the status bar.
	 */
	protected void redraw() {
		if( getParent() != null )
			getParent().repaint();
		else
			repaint();
	}

}