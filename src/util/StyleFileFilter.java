package camel.util;

import java.io.FilenameFilter;
import java.io.File;

public class StyleFileFilter implements FilenameFilter {
  
  /**
   * Returns true if the given filename could be a style file.
   */
  public boolean accept(File dir, String name) {
    return name.endsWith(".xml");
  }

}