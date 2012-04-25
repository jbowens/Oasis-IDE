package Camel.Interactions;

import java.io.*;
import java.util.*;

/*
 * A ReplListener is a class whose sole purpose is to constantly read in input from a
 * running OCaml REPL and notify any output listeners of the REPL's output.
 */
public class ReplListener implements Runnable {

    protected int handle;
    protected BufferedReader replStreamReader;
    protected List<TextOutputListener> observers;
    protected StringBuilder buffer;

    /*
     * Constructs a new ReplListener from an input stream and a list of observers.
     */
    public ReplListener( InputStream replStream, List<TextOutputListener> observers, int handle ) {
	this.handle = handle;
        replStreamReader = new BufferedReader( new InputStreamReader(replStream) );
        this.observers = observers;
        buffer = new StringBuilder();
    }

    /*
     * Listens to the output of the REPL constantly, and notifies observers when output
     * is received.
     */
    public void run() {

        boolean done = false;

        while( ! done ) {
            
            try {
                int c = replStreamReader.read();
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
		    for( TextOutputListener listener : observers ) {
		        listener.receiveOutput( event );
		    }
		    buffer.delete(0, buffer.length());
		}

            } catch( IOException e) {
                // Eat the exception and end execution
		e.printStackTrace();
                return;
            }
        }

	try {
	    replStreamReader.close();
	} catch( IOException e ) {
	    // eat it and return
	}
    }

}
