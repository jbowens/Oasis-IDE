package camel.interactions;

import java.util.*;
import java.io.FileNotFoundException;

/**
 * The InteractionsManager class can be used to manage multiple concurrent
 * instances of the OCaml REPL, for eg. if the user is running multiple programs
 * or even multiple instances of the same program. 
 */
public class InteractionsManager {

    /* The command to run the OCaml REPL */
    String ocamlLoc;

    /* All existing child interactions instances */
    protected ArrayList<Interactions> interactions;

    /* The current handle index */
    protected int currIndex;

    /**
     * Constructs a new InteractionsManager, given the location of the OCaml runnable
     *
     * @param ocamlLocation the location of OCaml on the system.
     */
    public InteractionsManager(String ocamlLocation) {
        ocamlLoc = ocamlLocation;
        currIndex = 0;
        interactions = new ArrayList<Interactions>();
    }

    /**
     * Creates a new Interactions REPL with the definitions in the file
     * specified by the argument def. This procedure returns the handle
     * to the interactions window, which can be used when registering listeners
     * or sending in input.
     *
     * @param def the filename of the OCaml file definitions to load or null if there are none.
     *
     * @return a new handle
     *
     * @throws FileNotFoundException when the given definitions file does not exist
     * @throws InteractionsUnavailableException if unable to create a new interactions instance
     */
    public int newInteractionsInstance( String def ) throws FileNotFoundException, InteractionsUnavailableException {
        int index = currIndex++;
        Interactions newInstance = new LocalInteractions(ocamlLoc, def, index);
        interactions.add(newInstance);
        return index;
    }

    /**
     * Closes an existing Interactions REPL instance. This is often called when
     * a user closes tab or window.
     *
     * @param id the handle for the interactions instance that should be closed
     *
     * @throws InvalidInteractionsIdException if the given handle is invalid
     */
    public void closeInteractionsInstance( int id ) throws InvalidInteractionsIdException {
        if( id < 0 || id > interactions.size() - 1)
            throw new InvalidInteractionsIdException();

        // Tell the interactions instance to close
        // Note: We can't remove it because we rely on the arraylist indexing.
        interactions.get(id).close();
    }

    /**
     * Passes on user input to the specified Interactions REPL instance.
     *
     * @param id the handle for the interactions instance
     * @param c the character to send to the interactions 
     */
    public void processUserInput( int id, char c ) throws InvalidInteractionsIdException {
        /* Make sure the interactions session exists. */
        if( id > interactions.size()-1 )
            throw new InvalidInteractionsIdException();
        if( interactions.get(id) == null )
            throw new InvalidInteractionsIdException();

        /* Pass on the input */
        interactions.get(id).processUserInput( c );
        
    }

    /**
     * Passes on a string of user input to the specified Interactions REPL instance.
     *
     * @param id the handle of the interactions instance
     * @param str the string to send to the interactions
     */
    public void processUserInput(int id, String str ) throws InvalidInteractionsIdException {
        /* Make sure the interactions session exists. */
        if( id > interactions.size()-1 )
            throw new InvalidInteractionsIdException();
        if( interactions.get(id) == null )
            throw new InvalidInteractionsIdException();

        /* Pass on the input */
        interactions.get(id).processUserInput( str );
    }

    /**
     * Registers an output listener for the specified Interactions REPL instance.
     *
     * @param o The TextOutputListener to subscribe
     * @param id the handle to subscribe the listener to
     */
    public void registerOutputListener(TextOutputListener o, int id) throws InvalidInteractionsIdException {
        if( id > interactions.size() - 1 )
            throw new InvalidInteractionsIdException();
        if( interactions.get(id) == null )
            throw new InvalidInteractionsIdException();

        /* Pass on the observer */
        interactions.get(id).registerOutputListener( o );

    }

    /**
     * Removes an output listener for the specified Interactions REPL instance.
     *
     * @param o The TextOutputListener to remove
     * @param id The handle to remove the TextOutputListener from
     */
    public void removeOutputListener(TextOutputListener o, int id) throws InvalidInteractionsIdException {
        if( id > interactions.size() - 1 )
            throw new InvalidInteractionsIdException();
        if( interactions.get(id) == null )
            throw new InvalidInteractionsIdException();

        /* Remove the observer */
        interactions.get(id).removeOutputListener( o );
    
    }

    /**
     * Releases any system resources held by the interactions manager, including killing
     * any child threads started by the interactions manager.
     */
    public void close() {
        for( Interactions i : interactions )
            i.close();
        interactions.clear();
    }

}
