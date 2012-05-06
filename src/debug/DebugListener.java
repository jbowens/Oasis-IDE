package camel.debug;

import java.io.*;
import java.util.*;
import camel.interactions.TextOutputListener;
import camel.interactions.TextOutputEvent;

/**
 *A DebugListener is a class whose sole purpose is to constantly read in input
 *from a running ocamldebug instance and notify any output listeners of the 
 *debugger
 */
public class DebugListener extends Thread {

	/* The handle that this thread is listening for */
	protected int handle;

	/*The stream to read input from */
	protected BufferedReader debugStreamReader;

	/*List of output listeners */
	protected List<TextOutputListener> observers;

	/*The string buffer for reading input */
	protected StringBuilder buffer;

	/*Whether of not this DebugListener should be open */
	protected boolean alive;

	/**
	*Create a new DebugListener from an input stream and a list of observers.
	*
	*@param debugStream - the input stream to read from
	*@param observers - the listeners to notify when there's output
	*@param handle - the handle associated with this input stream
	*/
	public DebugListener(InputStream debugStream, List<TextOutputListener> observers, int handle){
		this.handle = handle;
		debugStreamReader = new BufferedReader(new InputStreamReader(debugStream));
		this.observers = observers;
		buffer = new StringBuilder();
		alive = true;
	}

	/**
	*Listens to the output of the debugger constantly, and notifies observers when output
	*is recieved.
	*/
	public void run(){
		boolean done = false;

		while (!done){
			try{
				/*make sure we are alive */
				if(!alive){
					close(); return;
				}

				int c;// = debugStreamReader.read();
				char theChar;// = (char) c;
				//buffer.append(theChar);

				while(debugStreamReader.ready()){
					c = debugStreamReader.read();
					theChar = (char) c;
					if((c == -1) || (c == 0)){
						done = true;
					}else{
						buffer.append(theChar);
					}
				}

				if(buffer.length() > 0){
					String output = buffer.toString();
					TextOutputEvent event = new TextOutputEvent(output, handle);
					for(TextOutputListener listener : observers){
						listener.receiveOutput(event);
					}
					buffer.delete(0, buffer.length());
				}
			}catch(IOException e){
				/*We will eventually want to eat this */
				e.printStackTrace();
				return;
			}
		}

		try{
			debugStreamReader.close();
		}catch(IOException e){
			/*Eat it */
		}
	}

	/**
	*Kills the DebugListener
	*/
	public void kill(){
		alive = false;
	}

	/**
	*Closes the listener
	*/
	protected void close(){
		try{
			debugStreamReader.close();
		}catch(IOException e){
			/*Eat it */
		}
	}
	
}
