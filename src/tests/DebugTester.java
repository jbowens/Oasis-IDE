package camel.tests;

import java.io.*;
import camel.debug.*;

public class DebugTester{
	public static void main(String[] args) throws IOException, InterruptedException{
		Debug myDebug = new Debug("/test-ml");
		assert(myDebug != null);
		//if(myDebug != null){
		//	System.out.println("Not null");
		//}
		myDebug.getStartInfo();
		myDebug.callDebug();
		myDebug.runDebug();
		myDebug.Quit();

	}
}
