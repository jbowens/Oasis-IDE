package Camel;

import java.io.*;

public class InteractionsTester implements TextOutputListener  {

    public void receiveOutput(TextOutputEvent evt) {

	System.out.print(evt.getText());

    } 

    public static void main(String[] args) throws Exception {

	Config config = new Config("./Camel/settings.xml");
	InteractionsManager manager = new InteractionsManager(config);
	InteractionsTester tester = new InteractionsTester();

	int handle = manager.newInteractionsInstance( null );
	manager.registerOutputListener(tester, handle);

	BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
	String line = reader.readLine();
	while( line != null ) {
	    System.out.println("Sending " + line);
	    for( char c : line.toCharArray() ) {
		manager.processUserInput( handle, c );
	    }
	    manager.processUserInput( handle, '\r' );
	    manager.processUserInput( handle, '\n' );
	    line = reader.readLine();
	}
	
    }

}
