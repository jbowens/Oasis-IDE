package camel.syntaxhighlighter;

import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import camel.util.StyleFileFilter;

/**
 * A StyleSource that can load styles from the OS filesystem.
 *
 * @author jbowens
 */
public class FileSystemStyleLoader implements StyleSource {
    
    /* The parser for styles */
    protected StyleParser styleParser;

    /* This installation's style directory */
    protected String styleDirectory;

    /* The styles loaded into memory */
    protected Collection<StyleSet> styles;

    /**
     * Create a new style loader.
     *
     * @param styleDirectory the style directory to load styles from
     */
    public FileSystemStyleLoader(String styleDirectory) throws StyleParserException
    {    
        styleParser = StyleParser.createParser();

        if( styleDirectory != null )
        {
            this.styleDirectory = styleDirectory;
            styles = getStyles(styleDirectory);
        }
    }

    /**
     * Get available styles
     */
    public Collection<StyleSet> getAvailableStyles()
    {
        return styles;
    }

    /**
     * Parses the given file, returning the StyleSet represented
     * by the file.
     *
     * @param f  the file to parse
     * @return the StyleSet represented by the file
     */
    public StyleSet loadStyle(File f)
    {
        return styleParser.parseStyle( f );
    }

    /**
     * Returns all the styles contained in the given directory
     *
     * @param dir  the path of the directory
     * @return a list of the style sets contained within the directory
     */
    protected List<StyleSet> getStyles(String dir) {

        ArrayList<StyleSet> styles = new ArrayList<StyleSet>();

        File d = new File(dir);
        if( ! d.isDirectory() )
            return styles;

        File[] files = d.listFiles(new StyleFileFilter());
        for( File f : files ) {
            StyleSet style = loadStyle(f);
            if( style != null )
                styles.add( style );
        }

        return styles;

    }

}
