package camel.interactions;

import java.io.Reader;
import java.io.IOException;

/**
 * This class may be used to strip ANSI escape sequences from code.
 * Given a reader, it defines a new reader that will never output an
 * ANSI escape sequence.
 *
 * Note: If the ESC character may otherwise appear within your input,
 *       this class will not work as desired, stripping more if not all
 *       of the remaining input.
 */
public class ANSIStripperReader extends Reader {

  /* The source for this reader's data */
  protected Reader source;

  /* Whether or not the reader is currently in an Ansi tag. */
  protected boolean inAnsiTag = false;

  /**
   * Constructs a new ANSIStripperReader from a given
   * reader object.
   *
   * @param source the source of this reader's data
   */
  public ANSIStripperReader(Reader source) {
    this.source = source;
  }

  @Override
  public int read() throws IOException {
    
    int c;
    
    do {

      // Get a character from the original source
      c = source.read();

      // If it's -1, quit immediately.
      if( c == -1 )
        return c;


      if( inAnsiTag ) {

        // Is this the end of the ANSI sequence?
        if( c == (int) 'm')
          inAnsiTag = false;

        continue;

      } else {

        // Is this the start of an ANSI escape sequence?
        if( c == 27 ) {
          inAnsiTag = true;
          continue;
        }
        // If it's not, just exit the loop and
        // return the character.
        else
          break;
      }

    } while( true );
    // (Loop as long as we don't break)

    return c;

  }

  @Override
  public boolean ready() throws IOException {
    // WARNING: DOESN'T ACTUALLY OBEY THE SPECIFICATION FOR READY IN
    //          ALL CASES
    return false;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    int i = 0;
    for( ; i < len; i++ ) {
      int c = read();
      if( c == -1 )
        return -1;
      cbuf[off + i] = (char) c;
    }
    return i;
  }

  @Override
  public void reset() throws IOException {
    inAnsiTag = false;
    source.reset();
  }

  @Override
  public void close() throws IOException {
    source.close();
  }

}