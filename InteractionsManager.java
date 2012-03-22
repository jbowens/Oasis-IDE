package Camel;

import java.util.*;
import java.io.FileNotFoundException;

/*
 * The InteractionsManager class can be used to manage multiple concurrent
 * instances of the OCaml REPL, for eg. if the user is running multiple programs
 * or even multiple instances of the same program. 
 */
public class InteractionsManager {

    protected ArrayList<Interactions> interactions;
    protected int currIndex;
    Config config;

    /*
     * Constructs a new InteractionsManager, given a Config object.
     */
    public InteractionsManager(Config c) {
        config = c;
        currIndex = 0;
        interactions = new ArrayList<Interactions>();
    }

    /*
     * Creates a new Interactions REPL with the definitions in the file
     * specified by the argument def. This procedure returns the handle
     * to the interactions window, which can be used when registering listeners
     * or sending in input.
     */
    public int newInteractionsInstance( String def ) throws FileNotFoundException, InteractionsUnavailableException {
        int index = currIndex++;
        Interactions newInstance = new Interactions(config, def, index);
	interactions.add(newInstance);
        return index;
    }

    /*
     * Passes on user input to the specified Interactions REPL instance.
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

    /*
     * Registers an output listener for the specified Interactions REPL instance.
     */
    public void registerOutputListener(TextOutputListener o, int id) throws InvalidInteractionsIdException {
        if( id > interactions.size() - 1 )
            throw new InvalidInteractionsIdException();
        if( interactions.get(id) == null )
            throw new InvalidInteractionsIdException();

        /* Pass on the observer */
        interactions.get(id).registerOutputListener( o );

    }

    /*
     * Removes an output listener for the specified Interactions REPL instance.
     */
    public void removeOutputListener(TextOutputListener o, int id) throws InvalidInteractionsIdException {
        if( id > interactions.size() - 1 )
            throw new InvalidInteractionsIdException();
        if( interactions.get(id) == null )
            throw new InvalidInteractionsIdException();

        /* Remove the observer */
        interactions.get(id).removeOutputListener( o );
    
    }
}
