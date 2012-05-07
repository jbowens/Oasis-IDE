package camel.tests;

import java.io.*;
import camel.debug.*;
import camel.interactions.*;
import camel.gui.debug.*;

public class DebugTester implements TextOutputListener{

	public void receiveOutput(TextOutputEvent evt) {
		System.out.println(evt.getText());
	}

	public static void main(String[] args) throws Exception {
		
		//dbman instance
		DebugManager dbMan = new DebugManager("/usr/bin/");

		//new debugger instance
		int handle = dbMan.newDebuggerInstance("../test-ml/test.ml");

		//new output listener
		DebugOutputListener listener = new DebugOutputListener("Test");
		dbMan.registerOutputListener(listener,handle);

		dbMan.processGUIInput(handle,"break @ Test 1\n");
		//dbMan.processGUIInput(handle, "run\n");

		dbMan.processGUIInput(handle, "run\n");
		Thread.sleep(300);
		dbMan.processGUIInput(handle, "next\n");
		Thread.sleep(300);
		dbMan.processGUIInput(handle, "next\n");
		Thread.sleep(300);
		dbMan.processGUIInput(handle, "next\n");

		Thread.sleep(2000);
		dbMan.close();

	}

		

}
