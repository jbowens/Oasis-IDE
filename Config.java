package Camel;

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/*
 * The config class handles all settings for the application that need to persist between
 * sessions. Settings are stored as XML between sessions.
 */
public class Config {

    // The location of the settings .xml file
    protected String fileLocation;

    // The mapping of keys to values
    HashMap<String,String> settings;

    /*
     * Loads data from the xml file into the hashmap.
     */
    protected void loadDataFromFile(String file) throws NoSettingsException {

        try {

            /* Create the DOM document from the file */  
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            factory.setValidating(false);    
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));

            NodeList kvpairs = doc.getElementsByTagName("kvpair");

            for( int i = 0; i < kvpairs.getLength(); i++ ) {
                Node pairEl = kvpairs.item(i);
                String key = pairEl.getAttributes().getNamedItem("key").getNodeValue().trim();
                /* Doesn't make sense to not have a value child node. */
                if( ! pairEl.hasChildNodes() )
                    continue;

                String value = null;

                NodeList children = pairEl.getChildNodes();
                for( int j = 0; j < children.getLength(); j++ )  {
                    Node valueNode = children.item(j);
                    if( ! valueNode.getNodeName().equals("value") )
                        continue;
                    if( ! valueNode.hasChildNodes() )
                        continue;
                    value = valueNode.getFirstChild().getNodeValue();
                    break;
                }
                
                /* Insert the mapping into the table */
                settings.put(key, value);

            }

        } catch(ParserConfigurationException e ) {
            throw new NoSettingsException();        
        } catch( SAXException e ) {
            throw new NoSettingsException();
        } catch( IOException e ) {
            throw new NoSettingsException();
        }

    }

    /*
     * Constructs a new config object with the values in file.
     */
    public Config(String file) throws NoSettingsException {
        settings = new HashMap<String,String>();
        this.fileLocation = file;
        loadDataFromFile(file);
    }

    /*
     * Gets the setting with the provided key as a string value.
     */
    public String getSetting(String key) {
        return settings.get(key);
    }

    /*
     * Gets the setting with the provided key as an int value.
     */
    public int getSettingAsInt(String key) {
        try {
            return Integer.valueOf( settings.get(key) );
        } catch( NumberFormatException e ) {
            return -1;
        }
    }

    /*
     * Sets the provided key to map to the provided value.
     */
    public void setSetting(String key, String val) {
        settings.put(key, val);
    }

    /*
     * Sets the provided key to map to the integer value provided.
     */
    public void setIntegerSetting(String key, int val) {
        settings.put(key, String.valueOf(val));
    }

    /*
     * Saves the current state of the settings object to the XML file
     * it was constructed from.
     */
    public void save() throws SettingsSaveException {
        try {
             /* Create the DOM document from the file */  
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            factory.setValidating(false);    
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document newDoc = builder.newDocument();

            Element settingsRoot = newDoc.createElement("settings");
            newDoc.getDocumentElement().appendChild(settingsRoot);
            
            Element infoEl = newDoc.createElement("info");
            settingsRoot.appendChild(infoEl);

            Element settingData = newDoc.createElement("settingData");
            settingsRoot.appendChild(settingData);

            /* Add in the actual setting values. */
            for( Map.Entry<String,String> entry : settings.entrySet() ) {
                Element kvpair = newDoc.createElement("kvpair");
                kvpair.setAttribute( "key", entry.getKey() );
                Element valNode = newDoc.createElement("value");
                Text valText = newDoc.createTextNode(entry.getValue());
                valNode.appendChild(valText);
                kvpair.appendChild(valNode);
                settingData.appendChild(kvpair);
            }

            /* The document is ready to save to the disk. */
            TransformerFactory transformFactory = TransformerFactory.newInstance();
            Transformer transformer = transformFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource( newDoc );
            transformer.transform(source, result);
            String xmlString = sw.toString();

            System.out.println(xmlString);

        } catch( ParserConfigurationException e ) {
            throw new SettingsSaveException();
        } catch( TransformerConfigurationException e ) {
            throw new SettingsSaveException();
        } catch( TransformerException e ) {
            throw new SettingsSaveException();
        }
    }

    /*
     * Test cases
     */
    public static void main(String args[]) {

        try {
            Config c = new Config("./Camel/settings.xml");

            String val = c.getSetting("tabwidth");
            if( val != null )
                System.out.println( val.equals("4") );
            System.out.println( c.getSettingAsInt( "tabwidth" ) == 4 );
        } catch( NoSettingsException e ) {
            System.out.println( "Unable to load settings from file" );
        }
    }

}
