package camel.interactions;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;

/**
 * Represents a remote connection with an OCaml REPL. This is used when
 * debugging programs to connect to the debugged program's stdin/stdout
 * via a socket.
 */
public class RemoteInteractions extends Interactions {

	/* The interactions server that handles this interaction's connections */
	protected InteractionsServer interactionsServer;

	/* The port number this interactions should listen on */
	protected int port;

	/**
	 * Creates a new remote Interactions backend. It takes in the port the
	 * remote interactions instance should listen on and the handle of this
	 * interactions instance.
	 *
	 * @param port the port to listen on
	 * @param handle the handle of this interactions instance
	 */
	public RemoteInteractions(int port, int handle) throws InteractionsUnavailableException {
		this.port = port;

		try {
			interactionsServer = new InteractionsServer(port, handle, observers);
			interactionsServer.start();
		} catch(IOException e) {
			// If we can't start the server, there's nothing we can do.
			throw new InteractionsUnavailableException();
		}
	}

	/**
	 * Passes on user input to the standard in of the remote
	 * interactions instance.
	 *
	 * @param c the character to send
	 */
	@Override
	public void processUserInput( char c ) {
		try {
			interactionsServer.send( String.valueOf( c ) );
		} catch(IOException e) {
			// Eat and continue.
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
			interactionsServer.send( str );
		} catch(IOException e) {
			// Eat and continue.
		}
	}

	/**
	 * Severs the connection to the client if the connection still exists, and 
	 * frees any resources currently in use.
	 */
	@Override
	public void close() {
		if( interactionsServer != null )
			interactionsServer.close();
	}

}