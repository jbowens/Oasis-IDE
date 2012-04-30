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
	 * Loads the style int he 
	 */
	public StyleSet loadStyle(File f) {

		StyleSet set = new StyleSet("Untitled");
/*
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        factory.setValidating(false);    
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
*/
		return set;

	}

}