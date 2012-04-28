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
		setStyle(TokenType.DEFAULT, new TextStyle(Color.WHITE, Font.BOLD));
		setStyle(TokenType.COMMENT, new TextStyle(new Color(196, 196, 196), Font.ITALIC) );
		setStyle(TokenType.KEYWORD, new TextStyle(new Color(242, 111, 127), Font.BOLD));
		setStyle(TokenType.IDENTIFIER, new TextStyle(new Color(111, 129, 242), Font.BOLD));
		setStyle(TokenType.NUMBER, new TextStyle(new Color(104, 56, 128), Font.BOLD));
		setStyle(TokenType.STRING, new TextStyle(new Color(201, 22, 166), Font.BOLD));
		setStyle(TokenType.SPECIAL_VALUE, new TextStyle(new Color(77, 163, 28), Font.BOLD));
		setStyle(TokenType.OPERATOR, new TextStyle(new Color(88, 163, 163), Font.BOLD));
		setStyle(TokenType.TYPE, new TextStyle(new Color(217, 104, 28), Font.BOLD));

		setBackground( new Color(48, 48, 48) );

	}

}