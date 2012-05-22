package camel;

import camel.debug.*;
import camel.interactions.*;
import camel.gui.main.*;
import camel.gui.code_area.*;
import camel.syntaxhighlighter.StyleLoader;
import java.util.*;
import javax.swing.JOptionPane;

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

    /* The debug manager that organizes all interactions with the ocamldebug backend */
    protected DebugManager debugManager;

    /* A style loader to load styles from the file system */
    protected StyleLoader styleLoader;

    /* The main GUI class */
    protected MainWindow gui; 

    /* The number of open windows */
    protected ArrayList<MainWindow> windows;

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
	    this.debugManager = new DebugManager( "" );
        styleLoader = new StyleLoader( "./styles" );

        windows = new ArrayList<MainWindow>();
        setupGui();

        /* Make sure we always clean up after starting up */
         Runtime.getRuntime().addShutdownHook( new ShutdownCleaner(this) );
    }

    /**
     * Constructs the front-end gui for the application.
     */
    protected void setupGui() {
        // set some mac-specific properties
        System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Oasis IDE");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        windows.add(new MainWindow(this, config, interactionsManager, debugManager));
    }

    /**
     * Creates another window.
     */
    public void createNewWindow() {
        windows.add(new MainWindow(this, config, interactionsManager, debugManager));
    }

    /**
     * Opens a file.
     *
     * @param filename a filename to open
     */ 
    public void openFile( String filename ) {
        windows.get(0).open(filename);
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
        windows.get(0).newBlank();
    }

    /**
     * Gets the style loader for this application.
     */
    public StyleLoader getStyleLoader() {
        return styleLoader;
    }

    /**
     * Gets this applications config object.
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Tells the application that a gui (usually a MainWindow) has been closed.
     * If all the guis are closed, we should quit.
     */
    public void guiClosed(MainWindow gui) {
        windows.remove(gui);
        if( windows.size() <= 0 )
            close();
    }

    /**
     * Cleans up any system resources held by the application. This
     * needs to be called when the program exists.
     */
    public void close() {
        /* First try to close the guis. There may be unsaved changes! */
        try {
            for(MainWindow w : windows)
                w.close();
        } catch(CloseDeniedException ex) {
            // They no longer want to close, so just stop closing.
            return;
        }

        /* Now close everything else down */
        interactionsManager.close();
	    debugManager.close();
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
                System.err.println("Unable to save settings.");
            }
        }
        System.exit(0);
    }

    /**
     * Last ditch close() in case close() isn't called earlier.
     */
    public void finalize() {
        close();
    }



}

