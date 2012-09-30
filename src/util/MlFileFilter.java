package camel.util;

import java.io.FilenameFilter;
import java.io.FileFilter;
import java.io.File;

public class MlFileFilter implements FilenameFilter, FileFilter {
  
    /**
    * Returns true if the given filename is a .ml file or a file
    * otherwise supported by the IDE.
    */
    public boolean accept(File dir, String name) {
        return name.endsWith(".ml") || name.endsWith(".txt") || name.endsWith(".xml");
    }

    public boolean accept(File pathname)
    {
        return pathname.getName().endsWith(".ml");
    }

}
