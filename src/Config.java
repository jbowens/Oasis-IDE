package camel;

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

    /**
     * Loads data from the xml file into the hashmap.
     *
     * @param file - the filename of the settings file to load data from
     */
    protected void loadDataFromFile(String file) throws NoSettingsException {

        try {

            /* Create the DOM document from the file */  
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            factory.setValidating(false);    
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));

            NodeList pairCont = doc.getElementsByTagName("settingData");

            if( pairCont.getLength() == 0 )
                return;

            Node settingsData = pairCont.item(0);

            // get all the kvpairs
            NodeList kvpairs = settingsData.getChildNodes();

            for( int i = 0; i < kvpairs.getLength(); i++ ) {
                Node pairEl = kvpairs.item(i);

                // Make sure it's an element
                if( pairEl.getNodeType() != Node.ELEMENT_NODE ) {
                    continue;
                }

                String key = pairEl.getNodeName();

                String value = "";

                // Concatenate child node values together
                NodeList children = pairEl.getChildNodes();
                for( int j = 0; j < children.getLength(); j++ )  {
                    Node valueNode = children.item(j);
                    if( valueNode.getNodeValue() != null )
                        value += valueNode.getNodeValue();
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

    /**
     * Constructs a new config object with the values in file.
     */
    public Config(String file) throws NoSettingsException {
        settings = new HashMap<String,String>();
        this.fileLocation = file;
        loadDataFromFile(file);
    }

    /**
     * Gets the setting with the provided key as a string value.
     */
    public String getSetting(String key) {
        return settings.get(key);
    }

    /**
     * Gets the setting with the provided key as an int value.
     */
    public int getSettingAsInt(String key) {
        try {
            return Integer.valueOf( settings.get(key) );
        } catch( NumberFormatException e ) {
            return -1;
        }
    }

    /**
     * Returns true if the setting exists.
     *
     * @param key - the key to lookup
     *
     * @return true if the key exists
     */
    public boolean settingExists(String key) {
        return settings.containsKey(key);
    }

    /*
     * Sets the provided key to map to the provided value.
     */
    public void setSetting(String key, String val) {
        if( val != null )
            settings.put(key, val);
    }

    /*
     * Sets the provided key to map to the integer value provided.
     */
    public void setIntegerSetting(String key, int val) {
        settings.put(key, String.valueOf(val));
    }

    /*
     * Gets the filename attached to this settings instance.
     */
    public String getFileLocation() {
        return this.fileLocation;
    }

    /*
     * Saves the current state of the settings object to the XML file
     * it was constructed from.
     */
    public void save() throws SettingsSaveException {
        save(this.fileLocation);
    }

    /**
     * Saves the current state of the settings object to the XML file
     * at the filename specified.
     *
     * @param filename - the filename of the file to save the settings to
     */
    public void save(String filename) throws SettingsSaveException {
        try {
             /* Create the DOM document from the file */  
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            factory.setValidating(false);    
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document newDoc = builder.newDocument();

            Element settingsRoot = newDoc.createElement("settings");
            newDoc.appendChild(settingsRoot);
            
            Element infoEl = newDoc.createElement("info");
            settingsRoot.appendChild(infoEl);

            Element settingData = newDoc.createElement("settingData");
            settingsRoot.appendChild(settingData);

            /* Add in the actual setting values. */
            for( Map.Entry<String,String> entry : settings.entrySet() ) {
                Element kvpair = newDoc.createElement(entry.getKey());
                Text valText = newDoc.createTextNode(entry.getValue());
                kvpair.appendChild(valText);
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

            FileWriter writer = new FileWriter( new File( filename ) );
            writer.write( xmlString );
            writer.flush();
            writer.close();

        } catch( ParserConfigurationException e ) {
            throw new SettingsSaveException();
        } catch( TransformerConfigurationException e ) {
            throw new SettingsSaveException();
        } catch( TransformerException e ) {
            throw new SettingsSaveException();
        } catch( IOException e ) {
            throw new SettingsSaveException();
        }
    }

    /**
     * Test cases
     */
    public static void main(String args[]) {

        try {
            Config c = new Config("./Camel/settings.xml");

            String val = c.getSetting("tabwidth");
            if( val != null )
                System.out.println( val.equals("4") );
            System.out.println( c.getSettingAsInt( "tabwidth" ) == 4 );
            c.save();
        } catch( NoSettingsException e ) {
            System.out.println( "Unable to load settings from file" );
        } catch( SettingsSaveException e ) {
            System.out.println(" Unable to save settings to file" );
        }
    }

}
