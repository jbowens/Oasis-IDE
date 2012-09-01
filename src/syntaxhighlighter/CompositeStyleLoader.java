package camel.syntaxhighlighter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A style loader that can aggregate styles from multiple
 * style loaders.
 *
 * @author jbowens
 * @since Septmeber 2012
 */
public class CompositeStyleLoader extends StyleLoader {

    protected List<StyleLoader> styleLoaders;

    public CompositeStyleLoader() {
        super(null);
        styleLoaders = new ArrayList<StyleLoader>();
    }

    /**
     * Add a style loader whose styles should be included in this object's
     * output.
     *
     * @param loader  the style loader to add
     */
    public void addStyleLoader( StyleLoader loader )
    {
        styleLoaders.add( loader );
    }

    /**
     * @see StyleLoader.getAvailableStyles();
     */
    public List<StyleSet> getAvailableStyles()
    {
        // This hash map is used so that we don't show the same style set twice.
        HashMap<String,StyleSet> availableStyles = new HashMap<String, StyleSet>();
        for( StyleLoader loader : styleLoaders )
        {
            List<StyleSet> styles = loader.getAvailableStyles();
            for( StyleSet style : styles )
            {
                availableStyles.put(style.getName(), style);
            }
        }
        List<StyleSet> styles = new ArrayList(availableStyles.values());
        Collections.sort(styles);
        return styles;
    }

    // TODO: Clean up this hierarchy making StyleLoader an interface without this
    // method. This method should really not be here.
    public StyleSet loadStyle(File f)
    {
        // Unsupported
        return null;
    }

}
