package camel.debug;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Debug {
    /* I commented this out because it had compilation errors. - jackson 
	private Process proc;
    private BufferedWriter output;
    private BufferedReader input;
    private String filename;
    private String[] breakpoints = {"1", "3"};
    
    public Debug(String path) throws IOException{
        
    }
    
    public void getStartInfo(){
    	//get the info for the filename
    	//this.filename = 
    	//this.breakpoints = 
    }
    
    public void callDebug() throws IOException{
    	getStartInfo();
    	try{
    		String debugArgs[] = new String[breakpoints.length + 2];
    		for(int i = 0; i < breakpoints.length; i++){
    			debugArgs[i + 2] = breakpoints[i];
    		}
    		debugArgs[0] = "ocamldebug";
    		debugArgs[1] = filename;
    		proc = Runtime.getRuntime().exec(debugArgs);
    		input = new BufferedReader( new InputStreamReader( proc.getInputStream() ));
    		output = new BufferedWriter( new OutputStreamWriter( proc.getOutputStream() ));
    		String line = input.readLine();
    		while(line != null){
    		//	line = line + output.readLine();
    		}
    		//displayInput(line);
    		//while(line != null){
    			//args.write(getStepInfo());
    		//}
        
    	}
    	catch(IOException e){
    		e.printStackTrace();
    		System.out.print("Debug failed to initilize");
    	}
    }
    
    public String runDebug(){
    	output.write("run");
    	output.flush();
    	String line = input.readLine();
    	String outString = "";
    	while(line != null){
    		outString += line;
    		line = input.readLine();
    	}
    	return outString;
    }
    
    public String runStep(){
    	output.write("s");
    	output.flush();
    	String line = input.readLine();
    	String outString = "";
    	while(line != null){
    		outString += line;
    		line = input.readLine();
    	}
    	return outString;
    }
    
    public String runBack(){
    	output.write("bt");
    	output.flush();
    	String line = input.readLine();
    	String outString = "";
    	while(line != null){
    		outString += line;
    		line = input.readLine();
    	}
    	return outString;
    }

	public String getStepInfo() {
		// TODO Auto-generated method stub
		//THis method gets the next step command from the gui
		return null;
	}

	public void displayInput(String line) {
		// TODO Auto-generated method stub
		//This method parses the output for thd gui to use
	}
    */
    
}
