package camel.syntaxhighlighter;

import javax.swing.text.*;

public class OCamlViewFactory implements ViewFactory {

  protected StyleSet style;

  public OCamlViewFactory(StyleSet style) {
    this.style = style;
  }

	@Override
  	/**
     * @see javax.swing.text.ViewFactory#create(javax.swing.text.Element)
     */
    public View create(Element element) {
 
        return new OCamlView(element, style);
    }

}