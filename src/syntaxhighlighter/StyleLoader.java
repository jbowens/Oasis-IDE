package camel.syntaxhighlighter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import camel.util.StyleFileFilter;

/**
 * A StyleLoader can be used to create StyleSets from their
 * xml files.
 */
public class StyleLoader {
    
    /* This installation's style directory */
    protected String styleDirectory;

    /* The styles loaded into memory */
    protected List<StyleSet> styles;

    /**
     * Create a new style loader.
     *
     * @param the style directory to load styles from
     */
    public StyleLoader(String styleDirectory) {
        if( styleDirectory != null )
        {
            this.styleDirectory = styleDirectory;
            styles = getStyles(styleDirectory);
            Collections.sort(styles);
        }
    }

    /**
     * Get available styles
     */
    public List<StyleSet> getAvailableStyles() {
        return styles;
    }

    /**
     * Loads the style file into a StyleSet
     */
    public StyleSet loadStyle(File f) {

        StyleSet set = new StyleSet("Untitled");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        factory.setValidating(false);    
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(f);

        doc.normalize();

        /* Get the style information node */
        NodeList infoList = doc.getElementsByTagName("info");
        if( infoList.getLength() == 0 )
            return null;
        Node info = infoList.item(0);

        /* Get the style name */
        NodeList children = info.getChildNodes();
        for( int i = 0; i < children.getLength(); i++ ) {
            Node n = children.item(i);

            if( n.getFirstChild() == null || n.getFirstChild().getNodeValue() == null )
                continue;

            if( n.getNodeName().equals("name") )
                set.setName(n.getFirstChild().getNodeValue());
        }

        /* Get the main styling data */
        NodeList mainStylingList = doc.getElementsByTagName("mainStyling");
        if( mainStylingList.getLength() == 0 )
            return null;
        Node mainStyling = mainStylingList.item(0);

        /* Get main styling elements */
        children = mainStyling.getChildNodes();
        for( int i = 0; i < children.getLength(); i++ ) {
            Node n = children.item(i);

            // Skip any unparsable ones
            if( n.getFirstChild() == null || n.getFirstChild().getNodeValue() == null )
                continue;

            try {
                if( n.getNodeName().equalsIgnoreCase("background") )
                    set.setBackground( Color.decode( "0x" + n.getFirstChild().getNodeValue() ) );
                else if( n.getNodeName().equalsIgnoreCase("selectedBackground") )
                    set.setSelectedBackground( Color.decode( "0x" + n.getFirstChild().getNodeValue() ) );
                else if( n.getNodeName().equalsIgnoreCase("caret") )
                    set.setCaretColor( Color.decode( "0x" + n.getFirstChild().getNodeValue() ) );
                else if( n.getNodeName().equalsIgnoreCase("lineNumbers") )
                    set.setLineNumbersColor( Color.decode( "0x" + n.getFirstChild().getNodeValue() ) );
            } catch( NumberFormatException ex ) {
                continue;
            }
        }

        /* Get token styles */
        NodeList tokens = doc.getElementsByTagName("token");
        for( int i = 0; i < tokens.getLength(); i++ ) {
            Node n = tokens.item(i);
            NamedNodeMap attrs = n.getAttributes();
            Node kind = attrs.getNamedItem("kind");
            
            if( kind == null )
                continue;
            TokenType type = getTokenType( kind.getNodeValue() );
            if( type == null )
                continue;

            Color c = Color.BLACK;
            int fontStyle = Font.PLAIN;
            NodeList childProps = n.getChildNodes();
            for( int j = 0; j < childProps.getLength(); j++ ) {
                Node prop = childProps.item(j);

                if( prop.getFirstChild() == null || prop.getFirstChild().getNodeValue() == null )
                    continue;

                try {
                    if( prop.getNodeName().equals("color") ) {
                        if( prop.getFirstChild() != null && prop.getFirstChild().getNodeValue() != null ) {
                            c = Color.decode( "0x" + prop.getFirstChild().getNodeValue() );
                        }
                    } else if( prop.getNodeName().equals( "style" ) )
                        fontStyle = getFontStyle( prop.getFirstChild().getNodeValue() );
                } catch(NumberFormatException ex) {
                    continue;
                }
            }

            TextStyle textStyle = new TextStyle( c, fontStyle );
            set.setStyle( type, textStyle );
        }

      } catch (ParserConfigurationException ex) {
        return null;
      } catch (SAXException ex) {
        return null;
      } catch (IOException ex) {
        return null;
      }

        return set;

    }

    protected TokenType getTokenType( String id ) {
        try {
            return TokenType.valueOf( id.toUpperCase() );
        } catch( IllegalArgumentException e ) {
            return null;
        }
    }

    protected int getFontStyle( String style ) {
        if( "plain".equalsIgnoreCase(style) )
            return Font.PLAIN;
        if( "bold".equalsIgnoreCase(style) )
            return Font.BOLD;
        else
            return Font.PLAIN;
    }

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
