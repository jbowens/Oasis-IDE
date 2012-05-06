package camel.tests;

import java.io.*;
import camel.debug.*;
import camel.interactions.*;

public class DebugTester implements TextOutputListener{

	public void receiveOutput(TextOutputEvent evt) {
		System.out.println(evt.getText());
	}

	public static void main(String[] args) throws Exception {
		
		//dbman instance
		DebugManager dbMan = new DebugManager("");

		//new debugger instance
		int handle = dbMan.newDebuggerInstance("../test-ml/test.ml");

		//new output listener
		DebugTester listener = new DebugTester();
		dbMan.registerOutputListener(listener,handle);

		dbMan.processGUIInput(handle,"break @ Test 1\n");
		//dbMan.processGUIInput(handle, "run\n");

		dbMan.processGUIInput(handle, "run\n");
		dbMan.processGUIInput(handle, "next\n");
		dbMan.processGUIInput(handle, "next\n");
		dbMan.processGUIInput(handle, "next\n");

	}

		

}
