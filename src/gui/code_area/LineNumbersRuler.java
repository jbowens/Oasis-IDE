package camel.gui.code_area;

import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import camel.syntaxhighlighter.OCamlView;
import camel.syntaxhighlighter.OCamlDocument;
import camel.syntaxhighlighter.StyleSet;

/**
 * Displays line numbers on a text component.
 */
public class LineNumbersRuler extends JPanel implements DocumentListener {

  protected static final int MIN_WIDTH = 3;
  protected static final Color BREAKPOINT_COLOR = new Color(255, 79, 79);
  
  /* The style that defines how the ruler should present itself */
  protected StyleSet style;

  /* The pane the ruler is currently applied to */
  protected JEditorPane pane;

  /* How we should format line strings */
  protected String numbersFormat = "%" + MIN_WIDTH + "d";

  /* The last recorded height */
  protected int height;

  /* Breakpoints highlighting */
  protected HashSet<Integer> breakPointLines;

  /**
   * Creates a new line numbers ruler with the given style set.
   *
   * @param style the style for the line numbers rulers
   */
  public LineNumbersRuler(StyleSet style) {
    this.style = style;
    setBorder( BorderFactory.createEmptyBorder( 0, 4, 0, 10) );
    breakPointLines = new HashSet<Integer>();
    breakPointLines.add( new Integer(6) );
  }

  /**
   * Installs this line numbers ruler on a text component.
   *
   * @param pane the pane to install the ruler on
   */
  public void install(JEditorPane pane) {
    this.pane = pane;

    /* How large should we be? */
    height = pane.getPreferredSize().height;

    OCamlDocument doc = (OCamlDocument) pane.getDocument();

    doc.addDocumentListener( this );

    int lineCount = doc.countLines();

    int charWidth = Math.max(String.valueOf(lineCount).length(), MIN_WIDTH);
    FontMetrics editorFont = pane.getFontMetrics(pane.getFont());
    Insets insets = getInsets();

    int pixelWidth = editorFont.charWidth('0') * charWidth;
    int newWidth = insets.left + pixelWidth + insets.right;

    Dimension d = getPreferredSize();
    d.setSize(newWidth, height);
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
   * Toggles the breakpoint on the given line.
   *
   * @param line the line number the breakpoint should be toggled.
   */
  public void toggleBreakpoint(int line) {
    if( breakPointLines.contains(new Integer(line)) )
      breakPointLines.remove(new Integer(line));
    else
      breakPointLines.add(new Integer(line));
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

      // Set the breakpoint styling if this is a breakpoint
      if( breakPointLines.contains(lineNum) ) {
        g.setColor(BREAKPOINT_COLOR);
        g.setFont( pane.getFont().deriveFont(Font.BOLD) );
      }

      g.drawString(lineString, insets.left, verticalOffset);

      // Remove the breakpoint styling if necessary
      if( breakPointLines.contains(lineNum) ) {
        g.setColor(style.getLineNumbersColor());
        g.setFont( pane.getFont().deriveFont(Font.PLAIN) );
      }

    }

  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    respondToDocumentChange();
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    respondToDocumentChange();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    respondToDocumentChange();
  }

  /**
   * The ruler needs to update when the document changes.
   */
  protected void respondToDocumentChange() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        int newHeight = pane.getPreferredSize().height;
        if( height != newHeight ) {
          repaint();
          height = newHeight;
          Dimension d = getPreferredSize();
          d.height = height;
          setPreferredSize(d);
        }
      }
    });
  }

}