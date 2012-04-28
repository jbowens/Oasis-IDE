package camel.syntaxhighlighter;

import java.awt.Color;
import java.awt.Font;

/**
 * A simple, first style set.
 */
public class SimpleStyleSet extends StyleSet {
	
	/**
	 * Creates a new simple style set.
	 */
	public SimpleStyleSet() {
		super("Simplicity");

		/* Set the styles associated with this style set */
		setStyle(TokenType.COMMENT, new TextStyle(new Color(50, 50, 50), Font.PLAIN) );
		setStyle(TokenType.KEYWORD, new TextStyle(Color.RED, Font.BOLD));

	}

}