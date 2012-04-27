package camel;

import camel.interactions.*;
import java.util.*;

/**
 * Primary application class. Instantation of this class creates
 * a new instance of the application, including a new GUI and a 
 * new backend.
 */
public class Application {

    /* The configurations object for this instance of the application. */
    protected Config config;

    /* The interactions manager that organizes all interactions with the ocaml REPL */
    protected InteractionsManager interactionsManager;

    /**
     * Creates a new Application from the given settings file.
     *
     * @param settingsFile the xml file to load settings from
     *
     * @throws NoSettingsException when the settings file cannot be found
     */
    public Application(String settingsFile) throws NoSettingsException {
        // TODO: Add test to ensure OCaml is installed / find the ocaml executable
        this.config = new Config( settingsFile );
        this.interactionsManager = new InteractionsManager("ocaml");
        setupGui();
    }

    /**
     * Constructs the front-end gui for the application.
     */
    protected void setupGui() {
        // TODO: Connect with Raymond's GUI code
    }

    /**
     * Opens a file.
     *
     * @param filename a filename to open
     */ 
    public void openFile( String filename ) {
        // TODO: Implement
    }

    /**
     * Opens several files at once.
     *
     * @param filenames a list of filenames to open
     */
    public void openFiles( List<String> filenames ) {
        for( String file : filenames )
            this.openFile( file );
    }

    /**
     * Creates a new blank file.
     */
    public void openBlankProgram() {
        // TODO: Implement
    }

    /**
     * Cleans up any system resources held by the application. This
     * needs to be called when the program exists.
     */
    public void close() {
        interactionsManager.close();
        try {
            config.save();
        } catch( SettingsSaveException e ) {
            try {
                // Try again at the backup filename
                config.save(config.getFileLocation()+"~");
                System.err.println("Unable to save settings to " + config.getFileLocation()+ " so settings saved to " + config.getFileLocation()+"~" );
            }
            catch( SettingsSaveException e2 ) {
                // Completely unsuccessful, so we have to just ditch the settings.
                System.err.println("Unable to save settings.")
            }
        }
    }

    /**
     * Last ditch close() in case close() isn't called earlier.
     */
    public void finalize() {
        close();
    }

}

