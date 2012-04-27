package camel;

import camel.interactions.*;
import java.util.*;

/*
 * Primary application class. Instantation of this class creates
 * a new instance of the application, including a new GUI and a 
 * new backend.
 */
public class Application {

    protected Config config;
    protected InteractionsManager interactionsManager;

    public Application(String settingsFile) throws NoSettingsException {
        // TODO: Add test to ensure OCaml is installed / find the ocaml executable
        this.config = new Config( settingsFile );
        this.interactionsManager = new InteractionsManager("ocaml");
        setupGui();
    }

    /*
     * Constructs the front-end gui for the application.
     */
    protected void setupGui() {
        // TODO: Connect with Raymond's GUI code
    }

    /*
     * Opens a file.
     */ 
    public void openFile( String filename ) {
        // TODO: Implement
    }

    /*
     * Opens several files at once.
     */
    public void openFiles( List<String> filenames ) {
        for( String file : filenames )
            this.openFile( file );
    }

    /*
     * Creates a new blank file.
     */
    public void openBlankProgram() {
        // TODO: Implement
    }

}

