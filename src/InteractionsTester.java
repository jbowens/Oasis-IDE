package Camel;

import java.io.*;
import Camel.Interactions.*;

public class InteractionsTester implements TextOutputListener  {

    public void receiveOutput(TextOutputEvent evt) {

	System.out.print(evt.getText());

    } 

    public static void main(String[] args) throws Exception {

	InteractionsManager manager = new InteractionsManager("ocaml");
	InteractionsTester tester = new InteractionsTester();

	int handle = manager.newInteractionsInstance( "../test-ml/listlength.ml" );
	manager.registerOutputListener(tester, handle);

	BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
	String line = reader.readLine();
	while( line != null ) {
	    for( char c : line.toCharArray() ) {
		manager.processUserInput( handle, c );
	    }
	    manager.processUserInput( handle, '\r' );
	    manager.processUserInput( handle, '\n' );
	    line = reader.readLine();
	}
	
    }

}
