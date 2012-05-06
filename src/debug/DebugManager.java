package camel.debug;

import java.io.*;
import java.util.*;
import camel.interactions.TextOutputListener;
import camel.interactions.TextOutputEvent;

public class DebugManager{

	/* The command to compile ocaml */
	String ocamlCompileC;

	/*The command to run the ocamldebug */
	String ocamlDebugC;

	/*All existing child debug instances */
	protected ArrayList<Debug> debuggers;

	/* The current handle index */
	protected int currIndex;

	/**
	*Constructs a new DebugManager, given the location if the OCaml compiler and debugger
	*
	*@param ocamlLocation - the location of OCaml on the system
	*/
	public DebugManager(String ocamlLocation){
		ocamlCompileC = ocamlLocation + "ocamlc";
		ocamlDebugC = ocamlLocation + "ocamldebug";
		debuggers = new ArrayList<Debug>();
		currIndex = 0;
	}

	/**
	*Creates a new Debug Instance with the filename specified in the arguments. This proedure
	*returns the handle to the debugger, which can be used when registering listeners or
	*in sending in input.
	*
	*@param filename  - the name of the module that we are compiling
	*
	*@return a new handle
	*/
	public int newDebuggerInstance(String filename) throws FileNotFoundException, DebuggerCompilationException, IOException {
		int index = currIndex++;
		Debug newInstance = new Debug(ocamlCompileC, ocamlDebugC, filename, index);
		debuggers.add(newInstance);
		return index;
	}

	/**
	*Passes on input from the debugger GUI to the specified instance.
	*
	*@param id  - the handle for the interactions instance
	*@param cmd  - the command from the GUI
	*/
	public void processGUIInput(int id, String cmd) throws InvalidInteractionsException {
		System.out.println("ProcessGuiInput DebugManager");
		
		/* Make sure the debugger instance exists */
		if(id > debuggers.size() - 1){
			throw new InvalidInteractionsException();
		}
		if(debuggers.get(id) == null){
			throw new InvalidInteractionsException();
		}
		/*Pass on the input */

		debuggers.get(id).processGUIInput(cmd);
	}

	/**
	*Registers and output listener for the specified Debugger Instance
	*
	*@param o - the TextOutputListener to subscribe
	*@param id - the handle to subscribe the listener to
	*/
	public void registerOutputListener(TextOutputListener o, int id) throws InvalidDebuggerException{
		if(id > debuggers.size() - 1){
			throw new InvalidDebuggerException();
		}
		if(debuggers.get(id) == null){
			throw new InvalidDebuggerException();
		}

		/* Pass on the observer */
		debuggers.get(id).registerOutputListener(o);
	}

	/**
	*Removes an output listener for the specified Debug instance.
	*
	*@param o - the TextOutputListener to remove
	*@param id - the handle from which to remove the TextOutputListener
	*/
	public void removeOutputListener(TextOutputListener o, int id) throws InvalidDebuggerException{
		if(id > debuggers.size() -1){
			throw new InvalidDebuggerException();
		}
		if(debuggers.get(id) == null){
			throw new InvalidDebuggerException();
		}

		/* Remove the observer */
		debuggers.get(id).removeOutputListener(o);
	}

	/**
	*Releases any system resources held by the debug manager, including all child threads
	*started by the debug manager.
	*/
	public void close(){
		for(Debug db : debuggers){
			db.close();
		}
		debuggers.clear();
	}

}
