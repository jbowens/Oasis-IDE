package camel.gui.main;

import java.io.File;

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

    public static PrintableFile createFromFile(File f)
    {
        return new PrintableFile( f.getAbsolutePath() );
    }

}
