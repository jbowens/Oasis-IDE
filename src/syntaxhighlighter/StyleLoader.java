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

/**
 * A StyleLoader can be used to create StyleSets from their
 * xml files.
 */
public class StyleLoader {
	
	/**
	 * Create a new style loader.
	 */
	public StyleLoader() {
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

	    /* Get the style information node */
	    NodeList infoList = doc.getElementsByTagName("info");
	    if( infoList.getLength() == 0 )
	    	return null;
	    Node info = infoList.item(0);

	    /* Get the style name */
	    NodeList children = info.getChildNodes();
	    for( int i = 0; i < children.getLength(); i++ ) {
	    	Node n = children.item(i);
	    	if( n.getNodeName().equals("name") )
	    		set.setName(n.getNodeValue());
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
	    	if( n.getNodeName().equals("background") )
	    		set.setBackground( Color.decode( n.getNodeValue() ) );
	    	else if( n.getNodeName().equals("selectedBackground") )
	    		set.setSelectedBackground( Color.decode( n.getNodeValue() ) );
	    	else if( n.getNodeName().equals("caret") )
	    		set.setCaretColor( Color.decode( n.getNodeValue() ) );
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
	    		if( prop.getNodeName().equals("color") )
	    			c = Color.decode( prop.getNodeValue() );
	    		else if( prop.getNodeName().equals( "style" ) )
	    			fontStyle = getFontStyle( prop.getNodeValue() );
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

}