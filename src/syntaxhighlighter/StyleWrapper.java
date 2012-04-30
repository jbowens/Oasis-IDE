package camel.syntaxhighlighter;

import javax.swing.text.JTextComponent;
import java.awt.Color;

/**
 * A wrapper for a style set. A StyleWrapper can be used a style
 * because it extends a styleset, but the underlying style can
 * be swapped out whenever you want.
 */
public class StyleWrapper extends StyleSet {

  /* The currently used style */
  protected StyleSet style;

  public StyleWrapper(String name) {
    super(name);
    style = new SimpleStyleSet();
  }

  /**
   * Creates a new Style Wrapper
   */
  public StyleWrapper(StyleSet otherSet) {
    super(otherSet.getName());
    this.style = otherSet;
  }

  public void setStyle(StyleSet otherSet) {
    if( otherSet == this )
        return;
    this.style = otherSet;
  }

  @Override
  public void apply(JTextComponent component) {
    style.apply(component);
  }

  @Override
  public String getName() {
    return style.getName();
  }

  @Override
  public Color getBackgroundColor() {
    return style.getBackgroundColor();
  }

  @Override
  public Color getCaretColor() {
    return style.getCaretColor();
  }

  @Override
  public Color getSelectedBackground() {
    return style.getSelectedBackground();
  }

  @Override
  public TextStyle getStyle(TokenType type) {
    return style.getStyle(type);
  }

}