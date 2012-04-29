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
		setStyle(TokenType.DEFAULT, new TextStyle(Color.WHITE, Font.PLAIN));
		setStyle(TokenType.COMMENT, new TextStyle(new Color(138, 128, 117), Font.ITALIC) );
		setStyle(TokenType.KEYWORD, new TextStyle(new Color(255, 79, 101), Font.PLAIN));
		setStyle(TokenType.IDENTIFIER, new TextStyle(new Color(215, 166, 255), Font.PLAIN));
		setStyle(TokenType.NUMBER, new TextStyle(new Color(28, 238, 231), Font.PLAIN));
		setStyle(TokenType.STRING, new TextStyle(new Color(201, 22, 166), Font.PLAIN));
		setStyle(TokenType.SPECIAL_VALUE, new TextStyle(new Color(81, 232, 134), Font.PLAIN));
		setStyle(TokenType.OPERATOR, new TextStyle(Color.WHITE, Font.PLAIN));
		setStyle(TokenType.TYPE, new TextStyle(new Color(100, 232, 214), Font.PLAIN));
		setStyle(TokenType.DELIMETER, new TextStyle(new Color(225, 216, 232), Font.PLAIN));

		setBackground( new Color(35, 35, 35) );

	}

}