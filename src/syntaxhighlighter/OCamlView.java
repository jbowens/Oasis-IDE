package camel.syntaxhighlighter;

import java.awt.Font;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.text.ViewFactory;
import javax.swing.text.PlainView;
import javax.swing.text.PlainDocument;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

import java.util.Iterator;

/**
 * The view to be used when viewing OCaml documents.
 */
public class OCamlView extends PlainView {

  protected StyleSet styling;

  /**
   * Constructs a new OCaml view with the given element.
   */
  public OCamlView(Element element) {

    super(element);

    // For now just assume the simple styling
    styling = new SimpleStyleSet();

    // TODO: Change this to use the value loaded from the settings file.
    getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);

  }

  @Override
  protected int drawUnselectedText(Graphics graphics, int x, int y, int p0, int p1)
                                  throws BadLocationException {

    // Draw the text, but with the appropriate syntax highlighting

    setRenderingHints(graphics);
    Font saveFont = graphics.getFont();
    Color saveColor = graphics.getColor();
    
    OCamlDocument doc = (OCamlDocument) getDocument();
    Segment segment = getLineBuffer();

    try {
      
      System.out.println("Going to attemp to highlight from " + p0 + " to " + p1);

      Iterator<Token> i = doc.getTokens(p0, p1);

      int start = p0;
      while (i.hasNext()) {
        Token t = i.next();

        System.out.println("Handling token " + t);

        // Look for a gap because it's possible for text to not have a token (for example,
        // whitespace is often tokenless)
        if( start < t.getStart() ) {
          doc.getText(start, t.getStart() - start, segment);
          x = styling.getStyle(TokenType.DEFAULT).drawText(segment, x, y, graphics, this, start);
        }

        int length = t.getLength();
        int tmpStart = t.getStart();
        if( tmpStart < p0 ) {
          length -= (p0 - tmpStart);
          tmpStart = p0;
        }

        if( tmpStart + length > p1 ) {
          length = p1 - tmpStart;
        }
        doc.getText(tmpStart, length, segment);
        x = styling.getStyle(t.getType()).drawText(segment, x, y, graphics, this, t.getStart());
        start = t.getStart() + t.getLength();
      }

      // There might be leftover untokenized text
      if( start < p1 ) {
        doc.getText(start, p1 - start, segment);
        x = styling.getStyle(TokenType.DEFAULT).drawText(segment, x, y, graphics, this, start);
      }

    } catch( BadLocationException ex ) {
      // eat it
    } finally {
      graphics.setFont(saveFont);
      graphics.setColor(saveColor);
    }
    
    return x;
  }

  @Override
  public void paint(Graphics g, Shape s) {
    g.setColor(styling.getBackgroundColor());
    g.fillRect(0, 0, s.getBounds().width, s.getBounds().height);
    super.paint(g, s);
  }

  @Override
  protected void updateDamage(javax.swing.event.DocumentEvent changes,
          Shape a,
          ViewFactory f) {
      super.updateDamage(changes, a, f);
      java.awt.Component host = getContainer();
      host.repaint();
  }

  /** 
   * Sets the rendering hints, specifically including text antialiasing.
   *
   * @param graphics a Graphics object to apply the hints to
   */
  protected void setRenderingHints(Graphics graphics) {
    Graphics2D g = (Graphics2D) graphics;
    RenderingHints currentHints = g.getRenderingHints();
    currentHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  }

}