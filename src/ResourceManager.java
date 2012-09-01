package camel;

import java.io.File;

/**
 * A class for managing the application's resources on the file system.
 *
 * @author jbowens
 * @since September 2012
 */
public class ResourceManager {
    
    protected static final String SETTINGS_FILENAME = "settings.xml";
    protected static final String STYLES_DIRNAME = "styles";
    protected static final String INSTALLATION_STYLES_DIR = "./styles";

    protected String userHome;

    /**
     * Constructs a new ResourceManager.
     *
     * @param userHomeDir  a string representing the path to the user's oasis 
     *                     directory
     */
    public ResourceManager( String userHomeDir )
    {
        this.userHome = userHomeDir;
    }

    /**
     * Returns the path of the user's settings file.
     */
    public String getUserSettingsPath()
    {
        return this.userHome + File.separator + SETTINGS_FILENAME;
    }

    /**
     * Returns the path of the user's styles directory.
     */
    public String getUserStylesPath()
    {
        return this.userHome + File.separator + STYLES_DIRNAME;
    }

    /**
     * Returns the path of the installation's default styles
     * directory that contains styles shipped with the jar.
     */
    public String getInstallationStylesPath()
    {
        return INSTALLATION_STYLES_DIR;
    }

    /**
     * Initializes the user's oasis directory by creating
     * the necessary files if they haven't yet been created.
     *
     * @throws ResourceLoadingException when unable to setup necessary files
     */
    public void initializeUserDirectory() throws ResourceLoadingException
    {
        File userOasisDir = new File( this.userHome );
        if( ! userOasisDir.exists() )
        {
            // Create the directory
            if( ! userOasisDir.mkdir() )
                throw new ResourceLoadingException();
        }

        // Setup the settings file
        File settingsFile = new File( getUserSettingsPath() );
        if( ! settingsFile.exists() )
        {
            // TODO: Create a settings file with the default values
        }

        // Setup the styles directory
        File stylesDir = new File( getUserStylesPath() );
        if( ! stylesDir.exists() )
        {
            if( ! stylesDir.mkdir() )
                throw new ResourceLoadingException();
        }

    }

}
