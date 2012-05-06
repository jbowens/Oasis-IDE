package camel.debug;

import java.io.*;
import java.util.*;
import camel.interactions.TextOutputListener;


/**
 *Represents an indivdual debug instance.
 */
public class Debug extends Thread {
	/*The Runtime of the class */
    private Runtime runtime;
    
    /*The ocamldebug Process */
    private Process debugger;

    /*The list of observers to send the output to. Given to Debug Listener */
    protected List<TextOutputListener> observers;

    /*The writer to ocamldebug */
    OutputStreamWriter debugWriter;

    /*The listener from ocamldebug */
    DebugListener debugReader;

    /* The listener from ocamldebug ErrorStream */
    DebugListener debugErrorReader;

    /* The name of the file to compile */
    String filename;

    /*The ocamlc comand */
    String ocamlCompileC;

    /*The ocamldebug command */ 
    String ocamlDebugC;

    /*The name of the compiled outfile */
    String outFile;

    /*The handle number of this debug instance */
    int handle;
    
    /**
    *Creates a new Debug backend.
    *
    *@param ocamlCompile - the command for compiling the ocaml file
    *@parma ocamlDebug - the command for running the debugger
    *@param filename - the name of the file that is being debugged
    *@param handle - the id of the Debug instance
    */
    public Debug(String ocamlCompileC, String ocamlDebugC, String filename, int handle) 
    throws IOException, FileNotFoundException, DebuggerCompilationException{
        observers = new ArrayList<TextOutputListener>();
        this.ocamlCompileC = ocamlCompileC;
        this.ocamlDebugC = ocamlDebugC;

        if(filename.equals("")){
            this.filename = null;
        }else{
            this.filename = filename;
        }
        this.runtime = Runtime.getRuntime();
        compile();
        callDebug();
    }
    
    /**
    *This method is called when the debugger starts. This method attempts to compile
    *the code that is given by the current module. The compiler is called and the 
    *file takes the name of the handle of the Debug instance.
    *
    */ 
    protected void compile() throws FileNotFoundException, DebuggerCompilationException{
        String[] compileArgs = new String[5];
        String cOutFile = Integer.toString(handle);//filename + Integer.toString(handle);
        String outArg = cOutFile;
        compileArgs[0] = "/usr/bin/ocamlc";
        compileArgs[1] = "-g";
        compileArgs[2] = filename;
        compileArgs[3] = "-o";
        compileArgs[4] = cOutFile;
        outFile = cOutFile;

        /* Start the ocamlc compiler */
        Process compileProcess;
        try{

            compileProcess = runtime.exec(compileArgs);
            String tester = "cp " + filename + " ./";
            System.out.println("Move command: " + tester);
            runtime.exec(tester);
            //System.out.println("Compiled");
        }catch(IOException e){
		System.out.println("HERE");
            throw new DebuggerCompilationException();
        //}catch(FileNotFoundException f){
        //    throw new FileNotFoundException();
        }
        OutputStreamWriter compileWriter = new OutputStreamWriter(compileProcess.getOutputStream());

        InputStream processInputStream = compileProcess.getInputStream();

        /* Create a DebugListener to read the output */
        DebugListener compileReader = new DebugListener(processInputStream, this.observers, handle);

        compileReader.start();
        try{
            Thread.sleep(2000);
        }catch(Exception e){

        }
    }

    /**
    *This method starts and instance of the command line debugger for ocaml. The method
    *calls the debugger and throws an exceptioon if it cannot find the file or if the 
    *debugger cannot be called for some reason.
    */
    public void callDebug() throws IOException, FileNotFoundException, DebuggerCompilationException{
    	//try{
    		String debugArgs[] = new String[2];
    		debugArgs[0] = ocamlDebugC;
            debugArgs[1] = outFile;
            try{
                debugger = runtime.exec(debugArgs);
            }catch(IOException e){
		    //e.printStackTrace();
                throw new DebuggerCompilationException();
            }
            debugWriter = new OutputStreamWriter(debugger.getOutputStream());

            InputStream debuggerInputStream = debugger.getInputStream();
            InputStream debuggerErrorStream = debugger.getErrorStream();
            debugReader = new DebugListener(debuggerInputStream, this.observers, handle);
            debugReader.start();
            debugErrorReader = new DebugListener(debuggerErrorStream, this.observers, handle);
            debugErrorReader.start();

        //}catch(IOException e){
        //    throw new FileNotFoundException();
        //}
    }

    /**
    *Adds an output listener to the list of listeners to be notified when new text is 
    *available from the debugger. Listeners are not necessarily immediately notifed. 
    *Output is buffered and sent to listeners oly when no additional output is expected
    *soon.
    *
    *@param o - the TextOutputListener
    */
    void registerOutputListener(TextOutputListener o){
        if(observers.contains(o)){
            return;
        }
        observers.add(o);
    }

    /**
    *Removes an output listener from the list
    *
    *@param o - the TextOutputListener
    */
    void removeOutputListener(TextOutputListener o){
        observers.remove(o);
    }

    /**
    *Sends input from the GUI and to the debugger.
    *
    *@param cmd - the string that we are writing
    */
    void processGUIInput(String cmd){
        System.out.println("ProcessGuiInput Debug");
        try{
	       //char[] cmdArr = cmd.toCharArray();
	       //for (int i = 0; i < cmdArr.length; i++) {
                System.out.println("cmd: " + cmd);
                debugWriter.write(cmd);
                debugWriter.flush();
	       //}
        }catch(IOException e){
            //Eat it
	       e.printStackTrace();
            //throw new InvalidInteractionsException();
        }
    }

    /**
    *Closes the processes and exits the debugger
    */
    void close(){
        try{
            debugWriter.write("quit");
            debugWriter.flush();
            debugWriter.write("yes");
            debugWriter.flush();
            debugWriter.close();
            debugReader.kill();
            debugReader.interrupt();
            debugReader.kill();
            debugger.destroy();
        }catch(IOException e){
            //Eat it
        }
    }
}
