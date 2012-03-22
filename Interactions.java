package Camel;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Interactions {

    int handle;
    Config config;
    File definitionsFile;
    Process replProcess;
    OutputStreamWriter replWriter;
    List<TextOutputListener> observers;

    /*
     * Creates a new Interactions backend. It takes in a Config object that is
     * used to get a retrieve various installation-specific settings and the filename
     * of a definitions file. If the definitions filename is NULL, no initial definitions
     * are loaded.
     */
    public Interactions(Config c, String filePath, int handle) throws FileNotFoundException, InteractionsUnavailableException {
        observers = new ArrayList<TextOutputListener>();
        this.config = c;
	this.handle = handle;

        /* Process the given file path. */
        if( filePath == null || filePath.equals("") )
            this.definitionsFile = null;
        else
            this.definitionsFile = new File( filePath );

        if( this.definitionsFile != null && ! this.definitionsFile.exists() )
            throw new FileNotFoundException("The OCaml definitions file " + this.definitionsFile.toString() + " could not be found.");

        String[] cmd;

        /* Create the terminal command to start the OCaml REPL. */
        if( this.definitionsFile != null ) {
            cmd = new String[2];
            cmd[0] = "ocaml";
            cmd[1] = this.definitionsFile.getPath();
        } else {
            cmd = new String[1];
            cmd[0] = "ocaml";
        }
        /* Start the OCaml process */
        Runtime runtime = Runtime.getRuntime();
        try {
            replProcess = runtime.exec(cmd);
        } catch(IOException e) {
            throw new InteractionsUnavailableException();
        }
        replWriter = new OutputStreamWriter( replProcess.getOutputStream() );

        InputStream processInputStream = replProcess.getInputStream();
        Runnable readProcess = new ReplListener( processInputStream, this.observers, handle );

        /* Begin the read process for this Interactions REPL */
        new Thread(readProcess).start();        

    }

    /*
     * Adds an output listener to the list of listeners to be notified
     * when new text is available from the REPL. Listeners are not necessarily
     * immediately notified. Output is buffered and sent to listeners only when
     * no additional output is expected soon.
     */
    void registerOutputListener(TextOutputListener o) {
        if( observers.contains( o ) )
            return;
        observers.add( o );
    }

    /*
     * Desubsribes an output listener.
     */
    void removeOutputListener(TextOutputListener o) {
        observers.remove( o );
    }

    /*
     * Passes on user input to the REPL process.
     */
    void processUserInput( char c ) {
        try {
            replWriter.write( (int) c );
            replWriter.close();
        } catch(IOException e) {
            // Silently catch, and continue,
        }
    }

    /*
     * Closes any existing links to the filepath or other processes. This
     * may be called multiple times without harm or errors.
     */
    public void close() {
        if( replWriter != null ) {
            try {
                replWriter.close();
            } catch(IOException e) {
                // just eat the exception
            }
            replWriter = null;
        }
    }

    /*
     * Last ditch close() in case close() isn't called earlier.
     */
    public void finalize() {
        close();
    }

}
