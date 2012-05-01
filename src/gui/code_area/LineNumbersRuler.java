package camel.gui.code_area;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JEditorPane;

import camel.syntaxhighlighter.OCamlView;
import camel.syntaxhighlighter.OCamlDocument;
import camel.syntaxhighlighter.StyleSet;

/**
 * Displays line numbers on a text component.
 */
public class LineNumbersRuler extends JPanel {

  protected static final int MIN_WIDTH = 3;
  
  /* The style that defines how the ruler should present itself */
  protected StyleSet style;

  /* The pane the ruler is currently applied to */
  protected JEditorPane pane;

  protected String numbersFormat = "%" + MIN_WIDTH + "d";

  /**
   * Creates a new line numbers ruler with the given style set.
   *
   * @param style the style for the line numbers rulers
   */
  public LineNumbersRuler(StyleSet style) {
    this.style = style;
    setBorder( BorderFactory.createEmptyBorder( 0, 4, 0, 10) );
  }

  /**
   * Installs this line numbers ruler on a text component.
   *
   * @param pane the pane to install the ruler on
   */
  public void install(JEditorPane pane) {
    this.pane = pane;

    /* How large should we be? */
    OCamlDocument doc = (OCamlDocument) pane.getDocument();
    int lineCount = doc.countLines();

    int charWidth = Math.max(String.valueOf(lineCount).length(), MIN_WIDTH);
    FontMetrics editorFont = pane.getFontMetrics(pane.getFont());
    Insets insets = getInsets();

    int pixelWidth = editorFont.charWidth('0') * charWidth;
    int newWidth = insets.left + pixelWidth + insets.right;

    Dimension d = getPreferredSize();
    d.setSize(newWidth, 10000);
    setPreferredSize(d);
    setMinimumSize(d);
    setSize(d);

  }

  /**
   * Uninstalls this line numbers ruler from a text component.
   */
  public void uninstall() {
    this.pane = null;
  }

  /**
   * Paints the component with its lines.
   */
  @Override
  public void paintComponent(Graphics g) {

    setBackground( style.getBackgroundColor() );
    super.paintComponent(g);

    if( pane == null )
      return;

    OCamlDocument doc = (OCamlDocument) pane.getDocument();
    FontMetrics editorFont = pane.getFontMetrics(pane.getFont());
    Insets insets = getInsets();

    int lineHeight = editorFont.getHeight();
    int lineCount = doc.countLines();

    OCamlView.setRenderingHints(g);

    g.setFont( pane.getFont() );
    g.setColor( style.getLineNumbersColor() );

    // print the lines
    for( int lineNum = 1; lineNum <= lineCount; lineNum++ ) {
      String lineString = String.format(numbersFormat, lineNum);
      int verticalOffset = lineHeight * lineNum;
      g.drawString(lineString, insets.left, verticalOffset);
    }

  }

}