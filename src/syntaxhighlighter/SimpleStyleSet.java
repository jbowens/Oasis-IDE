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
		setStyle(TokenType.COMMENT, new TextStyle(new Color(105, 105, 105), Font.ITALIC) );
		setStyle(TokenType.KEYWORD, new TextStyle(Color.RED, Font.BOLD));
		setStyle(TokenType.IDENTIFIER, new TextStyle(new Color(77, 108, 140), Font.PLAIN));
		setStyle(TokenType.NUMBER, new TextStyle(new Color(104, 56, 128), Font.PLAIN));
		setStyle(TokenType.STRING, new TextStyle(new Color(201, 22, 166), Font.PLAIN));
		setStyle(TokenType.SPECIAL_VALUE, new TextStyle(new Color(77, 163, 28), Font.PLAIN));
		setStyle(TokenType.OPERATOR, new TextStyle(new Color(88, 163, 163), Font.PLAIN));

	}

}