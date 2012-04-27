package camel.debug;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Debug {
	private Process proc;
    private InputStream args;
    private OutputStream output;
    private String filename;
    private String[] breakpoints;
    
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
    		debugArgs[0] = "ocamldebug ";
    		debugArgs[1] = filename;
    		proc = Runtime.getRuntime().exec(debugArgs);
    		args = proc.getInputStream();
    		output = proc.getOutputStream();
    		String line; //= output.readLine();
    		//while(line != null){
    		//	line = line + output.readLine();
    		//}
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

	public String getStepInfo() {
		// TODO Auto-generated method stub
		//THis method gets the next step command from the gui
		return null;
	}

	public void displayInput(String line) {
		// TODO Auto-generated method stub
		//This method parses the output for thd gui to use
	}
    
}
