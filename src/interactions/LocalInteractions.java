package camel.interactions;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Handles local interactions by starting an OCaml process
 * locally on the user's system.
 */
public class LocalInteractions extends Interactions {
	
	int handle;
    File definitionsFile;
    String ocamlPath;
    Process replProcess;
    ReplListener readProcess;
    OutputStreamWriter replWriter;

	/*
     * Creates a new Interactions backend. It takes in the path to the OCaml runnable and the filename
     * of a definitions file. If the definitions filename is NULL, no initial definitions
     * are loaded.
     */
    public LocalInteractions(String ocamlPath, String filePath, int handle) throws FileNotFoundException, InteractionsUnavailableException {
        observers = new ArrayList<TextOutputListener>();
        this.ocamlPath = ocamlPath;
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
        cmd = new String[1];
        cmd[0] = "ocaml";

        /* Start the OCaml process */
        Runtime runtime = Runtime.getRuntime();
        try {
            replProcess = runtime.exec(cmd);
        } catch(IOException e) {
            throw new InteractionsUnavailableException();
        }
        replWriter = new OutputStreamWriter( replProcess.getOutputStream() );

        InputStream processInputStream = replProcess.getInputStream();
        readProcess = new ReplListener( processInputStream, this.observers, handle );

        /* Begin the read process for this Interactions REPL */
        readProcess.start();        

        /* Load the defintions */
        try {
            if( definitionsFile != null ) {
                BufferedWriter writer = new BufferedWriter(replWriter);
                writer.write( "#use \"" + definitionsFile.getAbsolutePath() + "\";;\n" );
                writer.flush();
            }
        } catch(IOException e) {
            throw new InteractionsUnavailableException();
        }

    }

	/**
     * Passes on user input to the standard in of the remote
     * interactions instance.
     *
     * @param str the string to send
     */
	@Override
    public void processUserInput( String str ) {
        try {
            replWriter.write( str );
            replWriter.flush();
        } catch(IOException e) {
            // Eat and continue.
        }
    }

    /*
     * Passes on user input to the REPL process.
     */
    @Override
    public void processUserInput( char c ) {
        try {
            replWriter.write( c );
            replWriter.flush();
        } catch(IOException e) {
            // Silently catch, and continue,
        }
    }

    /*
     * Closes any existing links to the filepath or other processes. This
     * may be called multiple times without harm or errors.
     */
    @Override
    public void close() {
        if( replWriter != null ) {
            try {
                replWriter.close();
                readProcess.kill();
                readProcess.interrupt();
                replProcess.destroy();
            } catch(IOException e) {
                // just eat the exception
            }
            replWriter = null;
        }
    }

}