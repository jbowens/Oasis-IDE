package camel.interactions;

import java.util.List;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ServerSocket;

/**
 * An InteractionsServer handles waiting for the client to connect.
 * It servers as a liason between the actual client socket and the
 * RemoteInteractions class. It must be in it's own thread because
 * ServerSocket.accept() blocks.
 */
public class InteractionsServer extends Thread {
	
	/* The port number this server should listen on */
	protected int port;

	/* The handle of this interactions instance */
	protected int handle;

	/* Observers for this interactions instance */
	protected List<TextOutputListener> observers;

	/* The ServerSocket used by the server to connect to the client(s). */
	protected ServerSocket serverSocket;

	/* The socket connection to the client */
	protected Socket clientSocket;

	/* A buffered writer for the socket output stream */
	protected BufferedWriter writer;

	/* The writing buffer */
	protected StringBuilder writeBuffer;

	/* The REPL listener */
	protected ReplListener stdoutListener;

	/* Marks whether or not this server has died/should be dead */
	protected boolean dead;

	/**
	 * Creates a new interactions server that listens on the
	 * specified port.
	 */
	public InteractionsServer(int port, int handle, List<TextOutputListener> listeners) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		observers = listeners;
		this.handle = handle;
	}

	/**
	 * Sends data to the client's stdin.
	 *
	 * @param message the data to send
	 */
	public synchronized void send(String message) throws IOException {
		writeBuffer.append(message);
		flush();
	}

	/**
	 * Begin listening for clients.
	 */
	public void run() {

		try {
			
			while( ! dead ) {

				// Connect to the client
				clientSocket = serverSocket.accept();

				// Spawn listener thread
				stdoutListener = new ReplListener(clientSocket.getInputStream(),
															   observers,
															   handle);
				stdoutListener.start();

				writer = new BufferedWriter( new OutputStreamWriter( clientSocket.getOutputStream() ) );

				// Give it a flush in case input was built up before we connected
				flush();

			}

		} catch(IOException ex) {
			// Just eat the exception
		} finally {
			close();
		}

	}

	/**
	 * Flushes the output buffer, sending everything
	 * to the client socket.
	 *
	 * @throws IOException if there was an error writing
	 */
	protected synchronized void flush() throws IOException {
		if( writer != null && clientSocket.isConnected() && !clientSocket.isOutputShutdown() ) {
			
			writer.write(writeBuffer.toString());
			writer.flush();
			// Remove the buffered text
			writeBuffer.delete(0, writeBuffer.length());
		
		}
	}

	/**
	 * This method should be called when the server is no
	 * longer running and no more input/output can be
	 * distributed through it.
	 */
	public void close() {
		dead = true;

		try {
			if( clientSocket != null ) {
				clientSocket.shutdownInput();
				clientSocket.shutdownOutput();
				close();
			}
		} catch(IOException ex) {}

		try {
			if( serverSocket != null ) {
				serverSocket.close();
			}
		} catch(IOException ex) {}

		stdoutListener.kill();
		stdoutListener.interrupt();

		interrupt();
	}


}