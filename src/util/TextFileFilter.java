package camel.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * A file filter / filename fitler that only accepts text files.
 *
 * @author jbowens
 * @since September 2012
 */
public class TextFileFilter implements FileFilter, FilenameFilter
{

    public boolean accept(File pathname)
    {
        return pathname.getName().endsWith( ".txt" );
    }

    public boolean accept(File directory, String filename)
    {
        return filename.endsWith( ".txt" );
    }

}
