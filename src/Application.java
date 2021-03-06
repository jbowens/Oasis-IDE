package camel;

import camel.debug.*;
import camel.interactions.*;
import camel.gui.main.*;
import camel.gui.code_area.*;
import camel.syntaxhighlighter.CompositeStyleSource;
import camel.syntaxhighlighter.FileSystemStyleLoader;
import camel.syntaxhighlighter.JarStyleLoader;
import camel.syntaxhighlighter.StyleParserException;
import camel.syntaxhighlighter.StyleSource;
import java.io.File;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Primary application class. Instantation of this class creates
 * a new instance of the application, including a new GUI and a 
 * new backend.
 */
public class Application {

    protected static final String APPLICATION_NAME = "Oasis IDE";

    /* The location of the user's application files */
    protected String userHome;

    /* The resource manager for application files */
    protected ResourceManager resourceManager;

    /* The configurations object for this instance of the application. */
    protected Config config;

    /* The interactions manager that organizes all interactions with the ocaml REPL */
    protected InteractionsManager interactionsManager;

    /* The debug manager that organizes all interactions with the ocamldebug backend */
    protected DebugManager debugManager;

    /* The primary style source used by the system */
    protected StyleSource styleSource;

    /* The main GUI class */
    protected MainWindow gui; 

    /* The number of open windows */
    protected ArrayList<MainWindow> windows;

    /**
     * Creates a new Application from the given settings file.
     *
     * @param userHome the location of the user's app files, setings, etc.
     *
     * @throws NoSettingsException when the settings file cannot be found
     */
    public Application(String userHome) throws NoSettingsException, ResourceLoadingException {
        // TODO: Add test to ensure OCaml is installed / find the ocaml executable
        this.userHome = userHome;
        this.resourceManager = new ResourceManager( userHome );
        this.resourceManager.initializeUserDirectory();

        this.config = new Config( resourceManager.getUserSettingsPath() );
        this.interactionsManager = new InteractionsManager("ocaml");
	    this.debugManager = new DebugManager( "" );
        
        CompositeStyleSource compStyleSource = new CompositeStyleSource();
        try {
            compStyleSource.addStyleSource( new FileSystemStyleLoader( resourceManager.getUserStylesPath() ) );
            JarStyleLoader jarStyleLoader = new JarStyleLoader( resourceManager.getInstallationStylesListPath() );
            if( jarStyleLoader != null )
                compStyleSource.addStyleSource( jarStyleLoader );
        } catch( StyleParserException ex )
        {
            System.err.println("Unable to load styles from the file system.");
        }
        this.styleSource = compStyleSource;

        windows = new ArrayList<MainWindow>();
        setupGui();

        /* Make sure we always clean up after starting up */
         Runtime.getRuntime().addShutdownHook( new ShutdownCleaner(this) );
    }

    /**
     * Retrieves the resource manager used by the application.
     */
    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }

    /**
     * Constructs the front-end gui for the application.
     */
    protected void setupGui() {
        // set some mac-specific properties
        System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", APPLICATION_NAME);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch( Exception ex ) {
            // Do nothing.
        }
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
     * Gets the style source for this application.
     */
    public StyleSource getStyleSource() {
        return styleSource;
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
        
    }

}

