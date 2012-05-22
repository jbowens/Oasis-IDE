package camel;

import javax.swing.UIManager;
import java.util.ArrayList;

/*
 * Contains the main() method for the entire application. This is the class that
 * should be run to launch execution.
 */
public class Runner {

    public static void main(String args[]) throws Exception {

        // Mac-specific properties:
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        /* Look for files to open */
        ArrayList<String> filesToOpen = new ArrayList<String>();
        for( int i = 0; i < args.length; i++ ) {
            filesToOpen.add( args[i] );
        }

        /* Create the new instance of the application */
        try {
            String settingsFile = "settings.xml";
            Application app = new Application( settingsFile );

            /* Load the files, or a blank one */
            if( ! filesToOpen.isEmpty() )
                app.openFiles( filesToOpen );
            else
                app.openBlankProgram();

        } catch(NoSettingsException e) {
            // TODO: Initialize a new settings file with default settings.
            System.err.println("Unable to find or load application settings.");
            System.exit(1);
        }
    }

}
