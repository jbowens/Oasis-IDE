package camel.syntaxhighlighter;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * This class is responsibile for parsing xml style documents,
 * construct StyleSet objects from them.
 *
 * @author jbowens
 * @since September 2012
 */
public class StyleParser
{

    protected DocumentBuilder builder;

    /**
     * Constructor for the StyleParser
     *
     * @throws StyleParserException
     */
    public StyleParser() throws StyleParserException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        try {
            builder = factory.newDocumentBuilder();
        } catch( ParserConfigurationException ex )
        {
            throw new StyleParserException();
        }
    }

    /**
     * Factory method.
     *
     * @throws StyleParserException
     */
    public static StyleParser createParser() throws StyleParserException
    {
        return new StyleParser();
    }

    /**
     * Parses the given file, returning the style set represented by the file.
     *
     * @param f  the File to parse
     * @return the StyleSet represented by the file or null if the file is malformed
     */
    public StyleSet parseStyle( File f )
    {
        try {
            Document doc = builder.parse(f);
            return parseStyle(doc);
        } catch( SAXException ex )
        {
            return null;
        } catch( IOException ex )
        {
            return null;
        }
    }

    /**
     * Parses the xml input stream, returning the style set represented by the stream.
     *
     * @param is  the input stream to parse
     * @return the StyleSet represented by the stream or null if the stream is malformed
     */
    public StyleSet parseStyle( InputStream is )
    {
        try {
            Document doc = builder.parse(is);
            return parseStyle(doc);
        } catch( SAXException ex )
        {
            return null;
        } catch( IOException ex )
        {
            return null;
        }
    }

    /**
     * Parses the document, returning the style set represented by the Document
     *
     * @param doc  the document to parse
     * @return the StyleSet represented by the document; null if the document is malformed
     */
    public StyleSet parseStyle(Document doc)
    {
        doc.normalize();

        StyleSet set = new StyleSet("Untitled");

        try {
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

        } catch (Exception ex) {
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

}
