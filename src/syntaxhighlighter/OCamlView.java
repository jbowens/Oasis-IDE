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

/**
 * The view to be used when viewing OCaml documents.
 */
public class OCamlView extends PlainView {

  /**
   * Constructs a new OCaml view with the given element.
   */
  public OCamlView(Element element) {

    super(element);

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
    
    Document doc = getDocument();
    String text = doc.getText(p0, p1 - p0);
    
    return 0;
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