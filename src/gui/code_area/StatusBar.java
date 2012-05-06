package camel.gui.code_area;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

/**
 * A status bar for displaying line and column number, etc.
 */
public class StatusBar extends JPanel {
	
	/* The parent tab that this status bar belongs to */
	protected Tab parentTab;

	/* JLabel for displaying where the caret is */
	protected JLabel caretPositionLabel;

	/* The font to use when displaying text on this status bar */
	protected Font font;

	/**
	 * Creates a new status bar.
	 *
	 * @param parent the parent tab of this status bar
	 */
	public StatusBar(Tab parent) {
		parentTab = parent;

		setLayout(new BorderLayout());

		// Set the background
		setBackground(new Color(0, 0, 0, 100));
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		caretPositionLabel = new JLabel("Line 24, Column 46", SwingConstants.LEFT);
		caretPositionLabel.setForeground(Color.WHITE);
		font = caretPositionLabel.getFont();
		font = font.deriveFont(Font.PLAIN, 9);
		caretPositionLabel.setFont(font);

		add(caretPositionLabel, BorderLayout.WEST);
	}

}