package camel.syntaxhighlighter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * A style source that can aggregate styles from multiple
 * style sources.
 *
 * @author jbowens
 * @since Septmeber 2012
 */
public class CompositeStyleSource implements StyleSource {

    // The style sources that this source uses
    protected Collection<StyleSource> styleSources;

    /**
     * Constructs a bare bones, empty composite style source.
     */
    public CompositeStyleSource()
    {
        styleSources = new ArrayList<StyleSource>();
    }

    /**
     * Constructs a composite style source from a collection of style sources.
     *
     * @param initialSources  a collection of style sources
     */
    public CompositeStyleSource(Collection<StyleSource> initialSources)
    {
        styleSources = initialSources;
    }

    /**
     * Add a style source whose styles should be included in this object's
     * output.
     *
     * @param loader  the style source to add
     */
    public void addStyleSource( StyleSource source )
    {
        styleSources.add( source );
    }

    /**
     * @see StyleSource.getAvailableStyles();
     */
    public Collection<StyleSet> getAvailableStyles()
    {
        // This hash map is used so that we don't show the same style set twice.
        HashMap<String,StyleSet> availableStyles = new HashMap<String, StyleSet>();
        for( StyleSource source : styleSources )
        {
            Collection<StyleSet> styles = source.getAvailableStyles();
            for( StyleSet style : styles )
            {
                availableStyles.put(style.getName(), style);
            }
        }
        Collection<StyleSet> styles = new ArrayList(availableStyles.values());
        return styles;
    }

}
