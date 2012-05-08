package camel.interactions;

import java.io.*;
import java.util.*;

/*
 * A ReplListener is a class whose sole purpose is to constantly read in input from a
 * running OCaml REPL and notify any output listeners of the REPL's output.
 */
public class ReplListener extends Thread {

    /* The handle this thread is listening for */
    protected int handle;

    /* The stream to read from */
    protected Reader replStreamReader;

    /* List of output listeners */
    protected List<TextOutputListener> observers;

    /* The string buffer for reading input */
    protected StringBuilder buffer;

    /* Whether or not this ReplListener should be open */
    protected boolean alive;

    /**
     * Create a new ReplListener from an input stream and a list of observers.
     *
     * @param replStream - the input stream to read from
     * @param observers - the listeners to notify when there's output
     * @param handle - the handle associated with this input stream
     */
    public ReplListener( InputStream replStream, List<TextOutputListener> observers, int handle ) {
	    this.handle = handle;
        replStreamReader = new ANSIStripperReader( new InputStreamReader(replStream) );
        this.observers = observers;
        buffer = new StringBuilder();
        alive = true;
    }

    /**
     * Listens to the output of the REPL constantly, and notifies observers when output
     * is received.
     */
    public void run() {

        boolean done = false;

        while( ! done ) {
            
            try {
                /* Make sure we're still supposed to be alive. */
                if( ! alive ) {
                    close();
                    return;
                }

                int c = replStreamReader.read();
                if( c == -1 )
                    break;

		        char theChar = (char) c;
                buffer.append( theChar );

                while( replStreamReader.ready() ) {
                    c = replStreamReader.read();
		            theChar = (char) c;
                    if( c == -1 || c == 0 )
                        done = true;
                    else
                        buffer.append( theChar );
                }

        		if( buffer.length() > 0 ) {
        		    String output = buffer.toString();
        		    TextOutputEvent event = new TextOutputEvent( output, handle );
                    synchronized(observers) {
            		    for( TextOutputListener listener : observers ) {
                            if( listener != null )
            		          listener.receiveOutput( event );
            		    }
                    }
        		    buffer.delete(0, buffer.length());
        		}

            } catch( IOException e) {
                // Eat the exception and end execution
                return;
            }
        }

	   try {
	       replStreamReader.close();
	   } catch( IOException e ) {
	    // eat it and return
	   }
    }

    /**
     * Kills down the REPL Listener
     */
    public void kill() {
        alive = false;
    }

    /**
     * Closes the listener
     */
    protected void close() {
        try {
            replStreamReader.close();
        } catch (IOException e) {
            // eat the exception
        }
    }
}
