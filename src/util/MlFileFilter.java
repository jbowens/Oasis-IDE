package camel.util;

import java.io.FilenameFilter;
import java.io.File;

public class MlFileFilter implements FilenameFilter {
  
  /**
   * Returns true if the given filename is a .ml file or a file
   * otherwise supported by the IDE.
   */
  public boolean accept(File dir, String name) {
    return name.endsWith(".ml") || name.endsWith(".txt") || name.endsWith(".xml");
  }

}