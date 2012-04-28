package camel.syntaxhighlighter;

import java.awt.*;
import javax.swing.text.*;

/**
 * Represents a styling of text. Instances of this class are paired up
 * with token types to style a document.
 */
public class TextStyle {
	
	/* The font style to display text in */
	protected int fontStyle;

	/* The color to display text in */
	protected Color color;

	/**
	 * Create a default initialized text style.
	 */
	public TextStyle() {
		// Load default styles
		color = Color.WHITE;
		fontStyle = Font.PLAIN;
	}

	/**
	 * Create a text style from a color and a font style.
	 */
	public TextStyle(Color color, int fontStyle) {
		this.color = color;
		this.fontStyle = fontStyle;
	}

	/**
	 * Returns the font color.
	 *
	 * @return the font color of this text style
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the font style
	 *
	 * @param the current font style of this text style
	 */
	public int getFontStyle() {
		return fontStyle;
	}

	/**
	 * Draws text on the given graphics object in this text style.
	 *
	 * @param segment - the text to display
	 * @param x - horizontal offset
	 * @param y - vertical offset
	 * @param graphics - the graphics context to draw on
	 * @param e - a tab expander
	 * @param offset - the starting offset of the text in the document
	 */
	 public int drawText(Segment segment, int x, int y, Graphics graphics,
	 					 TabExpander e, int offset) {
	 	// Set the font style
	 	graphics.setFont(graphics.getFont().deriveFont(getFontStyle()));
	 	// Set the color
	 	graphics.setColor(getColor());

	 	// Draw, baby, dray
	 	int newX = Utilities.drawTabbedText(segment, x, y, graphics, e, offset);

	 	return newX;
	 }

}