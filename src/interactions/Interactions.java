package camel.interactions;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/*
 * Represents an individual interactions/REPL instance. There is no need for this
 * class to be accessed outside the package. Instead, you should use the 
 * InteractionsManager which will delegate requests to the appropriate 
 * Interactions instances.
 */
public abstract class Interactions {

    List<TextOutputListener> observers;

    /*
     * Adds an output listener to the list of listeners to be notified
     * when new text is available from the REPL. Listeners are not necessarily
     * immediately notified. Output is buffered and sent to listeners only when
     * no additional output is expected soon.
     */
    void registerOutputListener(TextOutputListener o) {
        synchronized(observers) {
            if( observers.contains( o ) )
                return;
            observers.add( o );
        }
    }

    /*
     * Desubsribes an output listener.
     */
    void removeOutputListener(TextOutputListener o) {
        synchronized(observers) {
            observers.remove( o );
        }
    }

    /**
     * Passes on user input to the standard in of the remote
     * interactions instance.
     *
     * @param str the string to send
     */
    public abstract void processUserInput( String str );

    /**
     * Passes on user input to the REPL process.
     *
     * @param c the character to send
     */
    public abstract void processUserInput( char c );

    /**
     * Closes down the interactions instance.
     */
    public abstract void close();

}
