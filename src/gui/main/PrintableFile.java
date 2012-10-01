package camel.gui.main;

import java.io.File;

/**
 * A File whose toString() method is overriden to print out
 * just the file's filename and not its entire path.
 *
 * @author jbowens
 * @since September 2012
 */
public class PrintableFile extends File
{

    public PrintableFile( String filename )
    {
        super(filename);
    }

    @Override
    public String toString()
    {
        return getName();
    }

    /**
     * Creates a PrintableFile from a regular file.
     *
     * @param f  the file to convert
     * @return a PrintableFile that represents the same file
     */
    public static PrintableFile createFromFile(File f)
    {
        return new PrintableFile( f.getAbsolutePath() );
    }

}
