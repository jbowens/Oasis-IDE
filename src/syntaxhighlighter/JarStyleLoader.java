package camel.syntaxhighlighter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A style loader that loads styles from within the jar.
 *
 * @author jbowens
 * @since September 2012
 */
public class JarStyleLoader implements StyleSource {

    /* The style parser used by this loader */
    protected StyleParser styleParser;

    protected String dirPath;

    protected List<StyleSet> styles;

    public JarStyleLoader(String styleListing) throws StyleParserException
    {
        styleParser = StyleParser.createParser();

        try {
            InputStream is = getClass().getResourceAsStream(styleListing);
            if( is == null )
            {
                System.err.println("is is null");
                return;
            }
            BufferedReader br = new BufferedReader( new InputStreamReader( is ) );

            List<String> styleFilenames = new ArrayList<String>();
            String line;
            while( (line = br.readLine()) != null )
            {
                styleFilenames.add( line );
                System.out.println(line);
            }

            styles = new ArrayList<StyleSet>();
            for(String file : styleFilenames)
            {
                InputStream styleInputStream = getClass().getResourceAsStream(file);
                StyleSet set = styleInputStream == null ? null : styleParser.parseStyle(styleInputStream);
                if( set == null )
                {
                    System.err.println("Unable to parse style " + file);
                } else
                {
                    styles.add(set);
                }
            }

        } catch(IOException ex)
        {
            System.err.println("Error loading jar styles");
        }
    }

    public Collection<StyleSet> getAvailableStyles()
    {
        return styles;
    }


}
