package camel.syntaxhighlighter;

import java.util.Collection;

/**
 * Represents a source of style sets.
 *
 * @author jbowens
 * @since September 2012
 */
public interface StyleSource {

   /**
    * Returns the styles available through this style source.
    *
    * @return styles available to the user through this style source
    */
   public Collection<StyleSet> getAvailableStyles();

}
