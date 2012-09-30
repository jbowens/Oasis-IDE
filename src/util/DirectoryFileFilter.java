package camel.util;

import java.io.File;
import java.io.FileFilter;

/**
 * A file filter that only accepts directories.
 *
 * @author jbowens
 * @since September 2012
 */
public class DirectoryFileFilter implements FileFilter
{

    /**
     * Returns true if the file is a directory.
     */
    public boolean accept(File pathname)
    {
        return pathname.isDirectory();
    }

}
