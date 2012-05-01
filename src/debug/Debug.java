package camel.debug;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Debug {
    // I commented this out because it had compilation errors. - jackson 
	private Process proc;
    private BufferedWriter output;
    private BufferedReader input;
    private String filename;
    private String[] breakpoints = {"1", "3"};
    
    public Debug(String path) throws IOException{
        
    }
    
    public void getStartInfo(){
    	//get the info for the filename
    	this.filename = "./../test-ml/a.out";
    	this.breakpoints = getBreakPoints();
    }

    public String[] getBreakPoints(){
        return breakpoints;
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
            System.out.println("Proc Started");
            assert(proc != null);
    		input = new BufferedReader( new InputStreamReader( proc.getInputStream() ));
    		output = new BufferedWriter( new OutputStreamWriter( proc.getOutputStream() ));
            System.out.println("Before readline");
    		String line = input.readLine();
            System.out.println("After read line: " + line);
            line += input.readLine();
            System.out.println(line);
    		//while(!line.equals("")){
            //    System.out.println("in while loop");
            //	line = line + input.readLine();
            //    System.out.println("line: " + line);
    		//}
    		//displayInput(line);
    		//while(line != null){
    			//args.write(getStepInfo());
    		//}
            System.out.println("passed while loop");
        
    	}
    	catch(IOException e){
    		e.printStackTrace();
    		System.out.print("Debug failed to initilize");
    	}
    }
    
    public String runDebug() throws IOException{
    	output.write("run\n");
    	output.flush();
	output.write("\n");
	output.flush();
        System.out.println("wrote and flushed: run");
    	String line = ""; //= input.readLine();
    	String outString = "";
    	while(input.ready()){
            line = input.readLine();
            System.out.println(line);
            outString += line;
        }
        //System.out.println("Hello");
        //line = input.readLine();
        //System.out.println(line);
        //line = input.readLine();
        //System.out.println(line);
        //while(line != null){
    	//	outString += line;
        //    System.out.println("outString: " + outString);
    	//	line = input.readLine();
    	//}
    	return outString;
    }
    
    public String runStep() throws IOException{
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
    
    public String Back() throws IOException{
    	output.write("back");
    	output.flush();
    	String line = input.readLine();
    	String outString = "";
    	while(line != null){
    		outString += line;
    		line = input.readLine();
    	}
    	return outString;
    }

        public String Quit() throws IOException{
        output.write("quit");
        output.flush();
        output.write("y");
        output.flush();
        String line = input.readLine();
        String outString = "";
        while(line != null){
            outString += line;
            line = input.readLine();
        }
        return outString;
    }

    public String Step() throws IOException{
        output.write("step");
        output.flush();
        String line = input.readLine();
        String outString = "";
        while(line != null){
            outString += line;
            line = input.readLine();
        }
        return outString;
    }

	public String getStepInfo() throws IOException{
		// TODO Auto-generated method stub
		//THis method gets the next step command from the gui
		return null;
	}

	public void displayInput(String line) {
		// TODO Auto-generated method stub
		//This method parses the output for thd gui to use
	}
    //
    
}
