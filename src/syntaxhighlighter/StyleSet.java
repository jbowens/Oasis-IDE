package camel.syntaxhighlighter;

import java.util.EnumMap;

import java.awt.Color;

/**
 * Represents a collection of assignments of token types to
 * text styles.
 */
public class StyleSet {

	public static final TextStyle DEFAULT_STYLE = new TextStyle();
	
	/* Mapping from token types to textStyles */
	protected EnumMap<TokenType,TextStyle> mapping;

	/* The name of this style set. */
	protected String name;

	/* The background color for the style */
	protected Color background;

	/**
	 * Creates a new style set with the given name.
	 */
	public StyleSet( String name ) {
		this.name = name;
		mapping = new EnumMap<TokenType,TextStyle>(TokenType.class);
		background = Color.WHITE;
	}
	
	/**
	 * Returns the name if the style map.
	 *
	 * @return the name of this style set
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the background color of this style set.
	 *
	 * @return the background color to be used with this style set
	 */
	 public Color getBackgroundColor() {
	 	return background;
	 }

	 /**
	  * Sets the background to be the given color.
	  *
	  * @param bg the new background color for this style
	  */
	 public void setBackground(Color newBg) {
	 	this.background = newBg;
	 }

	/**
	 * Returns the style associated with the given type.
	 * If no style is associated with the type, a default style
	 * will be returned.
	 *
	 * @param type token type to retrieve a style for
	 * @return the associated text style
	 */
	public TextStyle getStyle(TokenType type) {
		if( ! mapping.containsKey(type) )
			return DEFAULT_STYLE;
		else
			return mapping.get(type);
	}

	/**
	 * Sets a text style to be associated with the given token type.
	 *
	 * @param type - the type to associate a style with
	 * @param style - the style to associate a type with
	 */
	public void setStyle(TokenType type, TextStyle style) {
		mapping.put(type, style);
	}

}