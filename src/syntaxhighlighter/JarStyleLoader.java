package camel.syntaxhighlighter;

/**
 * A style loader that loads styles from within the jar.
 *
 * @author jbowens
 * @since September 2012
 */
class JarStyleLoader {

    /* The style parser used by this loader */
    protected StyleParser styleParser;

    public JarStyleLoader() throws StyleParserException
    {
        styleParser = StyleParser.createParser();
    }



}
