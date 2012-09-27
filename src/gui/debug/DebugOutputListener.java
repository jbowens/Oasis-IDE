package camel.gui.debug;

import camel.interactions.*;

/**
*Listens for input from the debugger and parses it for the GUI
*/
public class DebugOutputListener implements TextOutputListener{

	/* The name of the module that is being debugged */
	String mName;

	/**
	*Instantiates the class
	*
	*@param mName - the name of the module being debugged
	*/
	public DebugOutputListener(String mName){
		this.mName = mName;

	}

	/**
	*The implementation of the TextOutputListener. Recieves input from the ocaml
	*debug. Calls updateLineNumber to update the debug GUI.
	*
	*@param evt - the TextOutputEvent from which we get the text
	*/
	public void receiveOutput(TextOutputEvent evt){
		String input = evt.getText();
		String[] inputArray = input.split("\\s+");
		//try{
			for(int i = 0; i< inputArray.length; i++){
				if(inputArray[i].equals("module")){
					if(((i + 2) < inputArray.length) && (inputArray[i+1].equals(mName))) {
						try{
							int lineNum = Integer.parseInt(inputArray[i+2]);
							//updateLineNumber(lineNum);
						}catch(NumberFormatException nmf){
							/*Eat it */
						}
					}
					//System.out.println(inputArray[i]);
					//System.out.println(inputArray[i+1]);
					//System.out.println(inputArray[i+2]);
				}
			}
	}

}
